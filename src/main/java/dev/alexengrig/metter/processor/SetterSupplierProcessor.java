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
public class SetterSupplierProcessor extends MethodSupplierProcessor {
    protected static final Class<SetterSupplier> ANNOTATION_TYPE = SetterSupplier.class;

    public SetterSupplierProcessor() {
        super(ANNOTATION_TYPE);
    }

    @Override
    protected MethodSupplierClassVisitor getMethodSupplierClassVisitor() {
        return new SetterSupplierClassVisitor();
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

    protected static class Field2SetterVisitor extends Field2MethodVisitor {
        protected static final String SET = "set";

        @Override
        protected String getFieldFromMethod(String method) {
            if (method.startsWith(SET)) {
                return method.substring(3, 4).toLowerCase() + method.substring(4);
            }
            throw new IllegalArgumentException("Unknown setter name construction (no set): " + method);
        }

        @Override
        protected String getMethodForField(String field, String method) {
            String fieldType = this.field2Type.get(field);
            return format("(instance, value) -> instance.%s((%s) value)", method, fieldType);
        }

        @Override
        protected boolean isTargetMethod(ExecutableElement executableElement) {
            return executableElement.getParameters().size() == 1
                    && executableElement.getSimpleName().toString().startsWith(SET)
                    && executableElement.getReturnType() instanceof NoType;
        }
    }

    protected class SetterSupplierClassVisitor extends MethodSupplierClassVisitor {
        protected static final String DEFAULT_PREFIX = "SetterSupplier";

        @Override
        protected String defaultSourceClassName(String className) {
            return className.concat(DEFAULT_PREFIX);
        }

        @Override
        protected String customClassName(TypeElement typeElement) {
            SetterSupplier annotation = typeElement.getAnnotation(SetterSupplier.class);
            return annotation.value();
        }

        @Override
        protected Field2SetterVisitor getField2MethodVisitor() {
            return new Field2SetterVisitor();
        }
    }
}
