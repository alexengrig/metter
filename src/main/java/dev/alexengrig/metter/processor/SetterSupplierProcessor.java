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
import dev.alexengrig.metter.annotation.SetterSupplier;
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
public class SetterSupplierProcessor extends BaseProcessor {
    public SetterSupplierProcessor() {
        super(SetterSupplier.class);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                ClassVisitor classVisitor = new ClassVisitor();
                element.accept(classVisitor, null);
                String className = classVisitor.className;
                String sourceClassName = classVisitor.sourceClassName;
                Map<String, String> field2Setter = classVisitor.field2Setter;
                JavaFileObject sourceFile = createSourceFile(sourceClassName);
                try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
                    String source = generateSource(sourceClassName, className, field2Setter);
                    sourcePrinter.println(source);
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }
        return true;
    }

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException(className);
        }
    }

    protected String generateSource(String className, String domainClassName, Map<String, String> field2Setter) {
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
                .add("import java.util.function.BiConsumer;")
                .add("import java.util.function.Supplier;")
                .add("")
                .add(format("@Generated(value = \"%s\", date = \"%s\")",
                        getClass().getName(), LocalDateTime.now().toString()))
                .add(format("public class %s implements Supplier<Map<String, BiConsumer<%s, Object>>> {",
                        simpleClassName, simpleDomainClassName))
                .add(format("    protected final Map<String, BiConsumer<%s, Object>> setterByField;",
                        simpleDomainClassName))
                .add("")
                .add(format("    public %s() {", simpleClassName))
                .add("        this.setterByField = createMap();")
                .add("    }")
                .add("")
                .add(format("    protected Map<String, BiConsumer<%s, Object>> createMap() {",
                        simpleDomainClassName))
                .add(format("        Map<String, BiConsumer<%s, Object>> map = new HashMap<>(%d);",
                        simpleDomainClassName, field2Setter.size()));
        field2Setter.forEach((field, setter) -> joiner.add(format("        map.put(\"%s\", %s);",
                field, setter)));
        return joiner
                .add("        return map;")
                .add("    }")
                .add("")
                .add("    @Override")
                .add(format("    public Map<String, BiConsumer<%s, Object>> get() {",
                        simpleDomainClassName))
                .add("        return setterByField;")
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

    protected String getSimpleName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        return className.substring(lastIndexOfDot + 1);
    }

    protected static class Field2SetterVisitor extends BaseElementVisitor {
        protected static final String SET = "set";
        protected final Map<String, String> field2Type = new HashMap<>();
        protected final Map<String, String> setter2Type = new HashMap<>();

        public Map<String, String> getField2SetterMap() {
            Map<String, String> map = new HashMap<>();
            for (String setter : setter2Type.keySet()) {
                String field = getFieldName(setter);
                if (field2Type.containsKey(field)) {
                    String fieldType = field2Type.get(field);
                    map.put(field, format("(instance, value) -> instance.%s((%s) value)", setter, fieldType));
                }
            }
            return map;
        }

        protected String getFieldName(String setter) {
            if (setter.startsWith(SET)) {
                return setter.substring(3, 4).toLowerCase() + setter.substring(4);
            }
            throw new IllegalArgumentException("Unknown setter name construction (no set): " + setter);
        }

        @Override
        public void visitVariable(VariableElement variableElement) {
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
            field2Type.put(name, type);
        }

        @Override
        public void visitExecutable(ExecutableElement executableElement) {
            if (executableElement.getParameters().size() == 1) {
                String name = executableElement.getSimpleName().toString();
                if (name.startsWith(SET)) {
                    TypeMirror returnType = executableElement.getReturnType();
                    if (returnType instanceof NoType) {
                        setter2Type.put(name, returnType.toString());
                    }
                }
            }
        }
    }

    protected class ClassVisitor extends BaseElementVisitor {
        protected static final String DEFAULT_PREFIX = "SetterSupplier";
        protected String className;
        protected String sourceClassName;
        protected Map<String, String> field2Setter;

        @Override
        public void visitType(TypeElement typeElement) {
            this.className = typeElement.getQualifiedName().toString();
            SetterSupplier annotation = typeElement.getAnnotation(SetterSupplier.class);
            this.sourceClassName = annotation.value().isEmpty()
                    ? defaultSourceClassName(className)
                    : customSourceClassName(className, annotation.value());
            Field2SetterVisitor field2SetterVisitor = new Field2SetterVisitor();
            for (Element element : typeElement.getEnclosedElements()) {
                element.accept(field2SetterVisitor, null);
            }
            this.field2Setter = field2SetterVisitor.getField2SetterMap();
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
