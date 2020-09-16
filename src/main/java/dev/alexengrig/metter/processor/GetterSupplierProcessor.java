/*
 * Copyright 2020 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.metter.processor;

import com.google.auto.service.AutoService;
import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.processor.element.BaseElementVisitor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import static java.lang.String.format;

@AutoService(Processor.class)
public class GetterSupplierProcessor extends BaseProcessor {
    public GetterSupplierProcessor() {
        super(GetterSupplier.class);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                ClassVisitor classVisitor = new ClassVisitor();
                element.accept(classVisitor, null);
                String className = classVisitor.className;
                String sourceClassName = classVisitor.sourceClassName;
                Map<String, String> field2Getter = classVisitor.field2Getter;
                JavaFileObject sourceFile = createSourceFile(sourceClassName);
                try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
                    String source = generateSource(sourceClassName, className, field2Getter);
                    sourcePrinter.println(source);
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }
        return true;
    }

    private JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException(className);
        }
    }

    private String generateSource(String className, String domainClassName, Map<String, String> field2Getter) {
        String packageName = getPackageName(className);
        String simpleClassName = getSimpleName(className);
        String simpleDomainClassName = getSimpleName(domainClassName);
        StringJoiner joiner = new StringJoiner("\n");
        if (packageName != null) {
            joiner.add("package " + packageName + ";").add("");
        }
        joiner
                .add("import javax.annotation.Generated;")
                .add("import java.util.HashMap;")
                .add("import java.util.Map;")
                .add("import java.util.function.Function;")
                .add("import java.util.function.Supplier;")
                .add("")
                .add(format("@Generated(value = \"%s\", date = \"%s\")",
                        getClass().getName(), LocalDateTime.now().toString()))
                .add(format("public class %s implements Supplier<Map<String, Function<%s, Object>>> {",
                        simpleClassName, simpleDomainClassName))
                .add(format("    protected final Map<String, Function<%s, Object>> getterByField;",
                        simpleDomainClassName))
                .add("")
                .add(format("    public %s() {", simpleClassName))
                .add("        this.getterByField = createMap();")
                .add("    }")
                .add("")
                .add(format("    protected Map<String, Function<%s, Object>> createMap() {",
                        simpleDomainClassName))
                .add(format("        Map<String, Function<%s, Object>> map = new HashMap<>(%d);",
                        simpleDomainClassName, field2Getter.size()));
        field2Getter.forEach((field, getter) -> joiner.add(format("        map.put(\"%s\", %s::%s);",
                field, simpleDomainClassName, getter)));
        return joiner
                .add("        return map;")
                .add("    }")
                .add("")
                .add("    @Override")
                .add(format("    public Map<String, Function<%s, Object>> get() {",
                        simpleDomainClassName))
                .add("        return getterByField;")
                .add("    }")
                .add("}")
                .toString();
    }

    protected String getPackageName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return className.substring(0, lastIndexOfDot);
        }
        return null;
    }

    private String getSimpleName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        return className.substring(lastIndexOfDot + 1);
    }

    protected static class Field2GetterVisitor extends BaseElementVisitor {
        protected static final String GET = "get";
        protected static final String IS = "is";
        protected final Map<String, String> field2Type = new HashMap<>();
        protected final Map<String, String> getter2Type = new HashMap<>();

        public Map<String, String> getField2GetterMap() {
            Map<String, String> map = new HashMap<>();
            for (String getter : getter2Type.keySet()) {
                String field = getFieldName(getter);
                if (field2Type.containsKey(field)) {
                    map.put(field, getter);
                }
            }
            return map;
        }

        protected String getFieldName(String getter) {
            if (getter.startsWith(GET)) {
                return getter.substring(3, 4).toLowerCase() + getter.substring(4);
            } else if (getter.startsWith(IS)) {
                return getter.substring(2, 3).toLowerCase() + getter.substring(3);
            }
            throw new IllegalArgumentException("Unknown getter name construction (no get/is): " + getter);
        }

        @Override
        public void visitVariable(VariableElement variableElement) {
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
            field2Type.put(name, type);
        }

        @Override
        public void visitExecutable(ExecutableElement executableElement) {
            if (executableElement.getParameters().isEmpty()) {
                String name = executableElement.getSimpleName().toString();
                if (name.startsWith(GET) || name.startsWith(IS)) {
                    TypeMirror returnType = executableElement.getReturnType();
                    if (!(returnType instanceof NoType)) {
                        getter2Type.put(name, returnType.toString());
                    }
                }
            }
        }
    }

    protected class ClassVisitor extends BaseElementVisitor {
        protected static final String DEFAULT_PREFIX = "GetterSupplier";
        protected String className;
        protected String sourceClassName;
        protected Map<String, String> field2Getter;

        @Override
        public void visitType(TypeElement typeElement) {
            this.className = typeElement.getQualifiedName().toString();
            GetterSupplier annotation = typeElement.getAnnotation(GetterSupplier.class);
            this.sourceClassName = annotation.value().isEmpty()
                    ? defaultSourceClassName(className)
                    : customSourceClassName(className, annotation.value());
            Field2GetterVisitor field2GetterVisitor = new Field2GetterVisitor();
            for (Element element : typeElement.getEnclosedElements()) {
                element.accept(field2GetterVisitor, null);
            }
            this.field2Getter = field2GetterVisitor.getField2GetterMap();
        }

        private String defaultSourceClassName(String className) {
            return className.concat(DEFAULT_PREFIX);
        }

        private String customSourceClassName(String className, String name) {
            String packageName = getPackageName(className);
            if (packageName != null) {
                return packageName.concat(".").concat(name);
            }
            return name;
        }
    }
}
