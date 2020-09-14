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
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ReflectionPersonSetterSupplier implements Supplier<Map<String, BiConsumer<Person, Object>>> {
    protected final Map<String, BiConsumer<Person, Object>> setterByField;

    public ReflectionPersonSetterSupplier() {
        Field[] fields = Person.class.getDeclaredFields();
        this.setterByField = new HashMap<>(fields.length);
        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = getCapitalized(fieldName);
            for (Method method : Person.class.getDeclaredMethods()) {
                if (method.getParameterCount() == 1 && isSetter(method.getName(), capitalizedFieldName)) {
                    this.setterByField.put(fieldName, createSetter(method));
                    break;
                }
            }
        }
    }

    private String getCapitalized(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private boolean isSetter(String methodName, String lastMethodNamePart) {
        return (methodName.startsWith("set") && methodName.equals("set" + lastMethodNamePart));
    }

    private BiConsumer<Person, Object> createSetter(Method method) {
        return (instance, value) -> {
            try {
                method.invoke(instance, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException("Instance: " + instance + "; value: " + value);
            }
        };
    }

    @Override
    public Map<String, BiConsumer<Person, Object>> get() {
        return setterByField;
    }
}
