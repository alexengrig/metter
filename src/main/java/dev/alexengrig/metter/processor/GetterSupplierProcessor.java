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
import dev.alexengrig.metter.processor.element.MethodSupplierProcessor;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.StringJoiner;

import static java.lang.String.format;

@AutoService(Processor.class)
public class GetterSupplierProcessor extends MethodSupplierProcessor {
    public GetterSupplierProcessor() {
        super(GetterSupplier.class);
    }

    protected GetterSupplierClassVisitor getMethodSupplierClassVisitor() {
        return new GetterSupplierClassVisitor();
    }

    protected String generateSource(String className, String domainClassName, Map<String, String> field2Getter) {
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
        field2Getter.forEach((field, getter) -> joiner.add(format("        map.put(\"%s\", %s);",
                field, getter)));
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

    protected class GetterSupplierClassVisitor extends MethodSupplierClassVisitor {
        protected static final String DEFAULT_PREFIX = "GetterSupplier";

        @Override
        protected String defaultSourceClassName(String className) {
            return className.concat(DEFAULT_PREFIX);
        }

        @Override
        protected String customSourceClassName(TypeElement typeElement) {
            GetterSupplier annotation = typeElement.getAnnotation(GetterSupplier.class);
            String name = annotation.value();
            if (name.isEmpty()) {
                return name;
            }
            String packageName = getPackageName(className);
            if (packageName != null) {
                return packageName.concat(".").concat(name);
            }
            return name;
        }

        @Override
        protected Field2GetterVisitor getField2MethodVisitor() {
            return new Field2GetterVisitor(className);
        }

    }

    protected class Field2GetterVisitor extends Field2MethodVisitor {
        protected static final String GET = "get";
        protected static final String IS = "is";

        protected final String className;

        public Field2GetterVisitor(String className) {
            this.className = getSimpleName(className);
        }

        @Override
        protected String getFieldFromMethod(String method) {
            if (method.startsWith(GET)) {
                return method.substring(3, 4).toLowerCase() + method.substring(4);
            } else if (method.startsWith(IS)) {
                return method.substring(2, 3).toLowerCase() + method.substring(3);
            }
            throw new IllegalArgumentException("Unknown getter name construction (no get/is): " + method);
        }

        @Override
        protected String getMethodForField(String field, String method) {
            return this.className.concat("::").concat(method);
        }

        @Override
        protected boolean isTargetMethod(ExecutableElement executableElement) {
            String name = executableElement.getSimpleName().toString();
            return (name.startsWith(GET) || name.startsWith(IS))
                    && executableElement.getParameters().isEmpty()
                    && !(executableElement.getReturnType() instanceof NoType);
        }
    }
}
