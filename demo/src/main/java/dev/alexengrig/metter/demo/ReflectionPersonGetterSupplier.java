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

package dev.alexengrig.metter.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReflectionPersonGetterSupplier implements Supplier<Map<String, Function<Person, Object>>> {
    protected static final Set<String> excludedFields = Collections.singleton("excluded");

    protected final Map<String, Function<Person, Object>> getterByField;

    public ReflectionPersonGetterSupplier() {
        Field[] fields = Person.class.getDeclaredFields();
        this.getterByField = new HashMap<>(fields.length);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (excludedFields.contains(fieldName)) {
                continue;
            }
            String capitalizedFieldName = getCapitalized(fieldName);
            for (Method method : Person.class.getDeclaredMethods()) {
                String methodName = method.getName();
                if (isGetter(methodName, capitalizedFieldName)) {
                    this.getterByField.put(fieldName, createGetter(method));
                    break;
                }
            }
        }
    }

    private String getCapitalized(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private boolean isGetter(String methodName, String lastMethodNamePart) {
        return (methodName.startsWith("get") && methodName.equals("get" + lastMethodNamePart))
                || (methodName.startsWith("is") && methodName.equals("is" + lastMethodNamePart));
    }

    private Function<Person, Object> createGetter(Method method) {
        return instance -> {
            try {
                return method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(instance.toString());
            }
        };
    }

    @Override
    public Map<String, Function<Person, Object>> get() {
        return getterByField;
    }
}
