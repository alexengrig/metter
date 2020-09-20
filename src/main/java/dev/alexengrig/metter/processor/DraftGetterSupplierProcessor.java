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
import dev.alexengrig.metter.processor.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.processor.element.descriptor.TypeDescriptor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
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
        TypeDescriptor type = new TypeDescriptor(typeElement);
        String className = type.getQualifiedName();
        Map<Object, Object> field2Getter = new HashMap<>();
        boolean hasAllGetters = type.hasAnnotation("lombok.Data") || type.hasAnnotation("lombok.Getter");
        for (FieldDescriptor field : type.getFields()) {
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
            error("Exception of source file writing", e);
        }
    }

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception of source file creation for:" + className);
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
