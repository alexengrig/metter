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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class DraftGetterSupplierProcessor extends DraftBaseProcessor<GetterSupplier, TypeElement> {
    public DraftGetterSupplierProcessor() {
        super(GetterSupplier.class);
    }

    @Override
    protected void process(TypeElement typeElement) {
        GetterSupplier annotation = typeElement.getAnnotation(annotationClass);
        Set<String> includedFields = new HashSet<>(Arrays.asList(annotation.includedFields()));
        Set<String> excludedFields = new HashSet<>(Arrays.asList(annotation.excludedFields()));
        TypeInfo type = new TypeInfo(typeElement);
        String className = type.getQualifiedName();
        Map<Object, Object> field2Getter = new HashMap<>();
        boolean hasAllGetters = type.hasAnnotation("lombok.Data") || type.hasAnnotation("lombok.Getter");
        for (FieldInfo field : type.getFields()) {
            String name = field.getName();
            if ((!includedFields.isEmpty() && includedFields.contains(name))
                    || (!excludedFields.isEmpty() && !excludedFields.contains(name))) {
                String getter = field.getGetterMethodName();
                if (hasAllGetters || field.hasAnnotation("lombok.Getter") || type.hasMethod(getter)) {
                    field2Getter.put(name, className.concat("::").concat(getter));
                }
            }
        }

        String sourceClassName = className + "GetterSupplier";
        JavaFileObject sourceFile = createSourceFile(sourceClassName);
        try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
            GetterSupplierSourceGenerator sourceGenerator = new GetterSupplierSourceGenerator();
            String source = sourceGenerator.generate(sourceClassName, className, field2Getter);
            sourcePrinter.println(source);
        } catch (IOException e) {
            error("Exception of source creation", e);
        }
    }

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException(className);
        }
    }

    static class TypeInfo {
        protected final TypeElement typeElement;
        protected transient String qualifiedName;
        protected transient String simpleName;
        protected transient Set<FieldInfo> fields;
        protected transient Set<MethodInfo> methods;
        protected transient Set<AnnotationInfo> annotations;

        public TypeInfo(TypeElement typeElement) {
            this.typeElement = typeElement;
        }

        public String getQualifiedName() {
            if (qualifiedName == null) {
                qualifiedName = typeElement.getQualifiedName().toString();
            }
            return qualifiedName;
        }

        public String getSimpleName() {
            if (simpleName == null) {
                simpleName = typeElement.getSimpleName().toString();
            }
            return simpleName;
        }

        public Set<FieldInfo> getFields() {
            if (fields == null) {
                fields = FieldInfo.of(typeElement);
            }
            return fields;
        }

        public Set<MethodInfo> getMethods() {
            if (methods == null) {
                methods = MethodInfo.of(typeElement);
            }
            return methods;
        }

        public boolean hasMethod(String methodName) {
            return getMethods().stream()
                    .map(MethodInfo::getName)
                    .anyMatch(methodName::equals);
        }

        public Set<AnnotationInfo> getAnnotations() {
            if (annotations == null) {
                annotations = AnnotationInfo.of(typeElement);
            }
            return annotations;
        }

        public boolean hasAnnotation(String annotationQualifiedName) {
            return getAnnotations().stream()
                    .map(AnnotationInfo::getQualifiedName)
                    .anyMatch(annotationQualifiedName::equals);
        }
    }

    static class FieldInfo {
        protected final VariableElement variableElement;
        protected transient String name;
        protected transient String className;
        protected transient Set<AnnotationInfo> annotations;

        public FieldInfo(VariableElement variableElement) {
            this.variableElement = variableElement;
        }

        static Set<FieldInfo> of(TypeElement typeElement) {
            VariableElementCollector fieldCollector = new VariableElementCollector(typeElement);
            return fieldCollector.getVariableElements().stream()
                    .map(FieldInfo::new)
                    .collect(Collectors.toSet());
        }

        public String getName() {
            if (name == null) {
                name = variableElement.getSimpleName().toString();
            }
            return name;
        }

        public String getClassName() {
            if (className == null) {
                className = variableElement.asType().toString();
            }
            return className;
        }

        public Set<AnnotationInfo> getAnnotations() {
            if (annotations == null) {
                annotations = AnnotationInfo.of(variableElement);
            }
            return annotations;
        }

        public boolean hasAnnotation(String annotationQualifiedName) {
            //FIXME: TypeInfo#hasAnnotation
            return getAnnotations().stream()
                    .map(AnnotationInfo::getQualifiedName)
                    .anyMatch(annotationQualifiedName::equals);
        }

        public String getGetterMethodName() {
            String methodNamePrefix = "boolean".equals(getClassName()) ? "is" : "get";
            String name = getName();
            return methodNamePrefix + name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        public String getSetterMethodName() {
            String name = getName();
            return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    static class MethodInfo {
        protected final ExecutableElement executableElement;
        protected transient String name;

        MethodInfo(ExecutableElement executableElement) {
            this.executableElement = executableElement;
        }

        static Set<MethodInfo> of(TypeElement typeElement) {
            ExecutableElementCollector methodCollector = new ExecutableElementCollector(typeElement);
            return methodCollector.getExecutableElements().stream().map(MethodInfo::new).collect(Collectors.toSet());
        }

        public String getName() {
            if (name == null) {
                name = executableElement.getSimpleName().toString();
            }
            return name;
        }
    }

    static class AnnotationInfo {
        protected final AnnotationMirror annotationMirror;
        private transient String qualifiedName;

        public AnnotationInfo(AnnotationMirror annotationMirror) {
            this.annotationMirror = annotationMirror;
        }

        static Set<AnnotationInfo> of(Element element) {
            return element.getAnnotationMirrors().stream()
                    .map(AnnotationInfo::new)
                    .collect(Collectors.toSet());
        }

        public String getQualifiedName() {
            if (qualifiedName == null) {
                qualifiedName = annotationMirror.getAnnotationType().toString();
            }
            return qualifiedName;
        }
    }

    static class VariableElementCollector extends BaseElementVisitor {
        protected final TypeElement typeElement;
        protected transient Set<VariableElement> variableElements;

        public VariableElementCollector(TypeElement typeElement) {
            this.typeElement = typeElement;
        }

        public Set<VariableElement> getVariableElements() {
            if (variableElements == null) {
                variableElements = new HashSet<>();
                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    enclosedElement.accept(this, null);
                }
            }
            return variableElements;
        }

        @Override
        public void visitVariable(VariableElement variableElement) {
            variableElements.add(variableElement);
        }
    }

    static class ExecutableElementCollector extends BaseElementVisitor {
        protected final TypeElement typeElement;
        protected transient Set<ExecutableElement> executableElements;

        public ExecutableElementCollector(TypeElement typeElement) {
            this.typeElement = typeElement;

        }

        public Set<ExecutableElement> getExecutableElements() {
            if (executableElements == null) {
                executableElements = new HashSet<>();
                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    enclosedElement.accept(this, null);
                }
            }
            return executableElements;
        }

        @Override
        public void visitExecutable(ExecutableElement executableElement) {
            executableElements.add(executableElement);
        }
    }

    static class GetterSupplierSourceGenerator {
        public String generate(String className, String domainClassName, Map<Object, Object> field2Getter) {
            String packageName = getPackageName(className);
            String simpleClassName = getSimpleName(className);
            return new LineJoiner()
                    .ftIf(packageName != null, "package %s;\n", packageName)
                    .ln("@javax.annotation.Generated(")
                    .ft("        value = \"%s\",", getClass().getName())
                    .ft("        date = \"%s\")", LocalDateTime.now().toString())
                    .ft("public class %s implements", simpleClassName)
                    .ln("        java.util.function.Supplier<")
                    .ln("                java.util.Map<")
                    .ln("                        java.lang.String,")
                    .ln("                        java.util.function.Function<")
                    .ft("                                %s,", domainClassName)
                    .ln("                                java.lang.Object>>> {")
                    .ln("    protected final java.util.Map<")
                    .ln("            java.lang.String,")
                    .ln("            java.util.function.Function<")
                    .ft("                    %s,", domainClassName)
                    .ln("                    java.lang.Object>> getterByField;")
                    .ln()
                    .ft("    public %s() {", simpleClassName)
                    .ln("        this.getterByField = createMap();")
                    .ln("    }")
                    .ln()
                    .ln("    protected java.util.Map<")
                    .ln("            java.lang.String,")
                    .ln("            java.util.function.Function<")
                    .ft("                    %s,", domainClassName)
                    .ln("                    java.lang.Object>>")
                    .ln("    createMap() {")
                    .ln("        java.util.HashMap<")
                    .ln("                java.lang.String,")
                    .ln("                java.util.function.Function<")
                    .ft("                        %s,", domainClassName)
                    .ln("                        java.lang.Object>>")
                    .ft("                map = new java.util.HashMap<>(%d);", field2Getter.size())
                    .mp("        map.put(\"%s\",\n                %s);", (f, g) -> new Object[]{f, g}, field2Getter)
                    .ln("        return map;")
                    .ln("    }")
                    .ln()
                    .ln("    @Override")
                    .ln("    public java.util.Map<")
                    .ln("            java.lang.String,")
                    .ln("            java.util.function.Function<")
                    .ft("                    %s,", domainClassName)
                    .ln("                    java.lang.Object>>")
                    .ln("    get() {")
                    .ln("        return getterByField;")
                    .ln("    }")
                    .ln("}")
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
    }

    static class LineJoiner {
        protected static final String NL = "\n";
        protected final StringBuilder builder = new StringBuilder();

        public LineJoiner ln() {
            builder.append(NL);
            return this;
        }

        public LineJoiner ln(String line) {
            builder.append(line);
            return ln();
        }

        public LineJoiner ft(String line, Object... args) {
            return ln(String.format(line, args));
        }

        public LineJoiner ftIf(boolean condition, String line, Object... args) {
            return condition ? ln(String.format(line, args)) : this;
        }

        public <K, V> LineJoiner mp(String template,
                                    BiFunction<? super K, ? super V, Object[]> mapper,
                                    Map<? extends K, ? extends V> map) {
            map.forEach(mapper.andThen(args -> ft(template, args))::apply);
            return this;
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }
}
