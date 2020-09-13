package dev.alexengrig.metter.processor;

import com.google.auto.service.AutoService;
import dev.alexengrig.metter.processor.element.GetterTypeElementVisitor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class MetaGenerationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotationTypeElement : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotationTypeElement);
            for (Element annotatedElement : annotatedElements) {
                GetterTypeElementVisitor typeVisitor = new GetterTypeElementVisitor();
                annotatedElement.accept(typeVisitor, null);
                Map<String, String> getterByField = typeVisitor.getMap();
                if (!getterByField.isEmpty()) {
                    note("Field to getter method:");
                    getterByField.forEach((k, v) -> note(k + " - " + v));
                    try {
                        generateFile(typeVisitor.getClassName(), getterByField);
                    } catch (IOException e) {
                        error(e.getMessage());
                    }
                }
            }
        }
        return true;
    }

    private void generateFile(String className, Map<String, String> getterByField) throws IOException {
        String packageName = null;
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            packageName = className.substring(0, lastIndexOfDot);
        }
        String simpleClassName = className.substring(lastIndexOfDot + 1);
        String holderSimpleClassName = "Generated" + simpleClassName + "MetadataHolder";
        String holderClassName = (packageName != null ? packageName + "." : "") + holderSimpleClassName;
        note("Package: " + packageName);
        note("Class: " + simpleClassName + " (" + className + ")");
        note("Holder: " + holderSimpleClassName + " (" + holderClassName + ")");
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(holderClassName);
        try (PrintWriter printer = new PrintWriter(builderFile.openWriter())) {
            if (packageName != null) {
                printer.print("package ");
                printer.print(packageName);
                printer.println(";");
                printer.println();
            }
            printer.println("import javax.annotation.Generated;");
            printer.println("import java.util.HashMap;");
            printer.println("import java.util.Map;");
            printer.println("import java.util.function.Function;");
            printer.println();
            printer.println(String.format("@Generated(value = \"%s\", date = \"%s\")",
                    getClass().getName(), LocalDateTime.now().toString()));
            printer.println(String.format("public class %s implements Function<String, Function<%s, Object>> {",
                    holderSimpleClassName, simpleClassName));
            printer.println(String.format("    private final Map<String, Function<%s, Object>> getterByField;",
                    simpleClassName));
            printer.println();
            printer.println(String.format("    public %s() {",
                    holderSimpleClassName));
            printer.println(String.format("        this.getterByField = new HashMap<>(%d);",
                    getterByField.size()));
            getterByField.forEach((field, method) ->
                    printer.println(String.format("        this.getterByField.put(\"%s\", %s);",
                            field, simpleClassName + "::" + method)));
            printer.println("    }");
            printer.println();
            printer.println("    @Override");
            printer.println(String.format("    public Function<%s, Object> apply(String field) {",
                    simpleClassName));
            printer.println("        if (!getterByField.containsKey(field)) {");
            printer.println("            throw new IllegalArgumentException(\"No getter for field: \" + field);");
            printer.println("        }");
            printer.println("        return getterByField.get(field);");
            printer.println("    };");
            printer.println("}");
        }
    }

    private void note(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void error(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(MetaAnnotation.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
