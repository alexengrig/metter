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

package dev.alexengrig.metter.motivation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class ReflectionManChangeLogGenerator extends MapManChangeLogGenerator {
    @Override
    protected Map<String, Function<Man, Object>> createMap() {
        Field[] fields = Man.class.getDeclaredFields();
        HashMap<String, Function<Man, Object>> target = new HashMap<>(fields.length);
        for (Field field : fields) {
            for (Method method : Man.class.getDeclaredMethods()) {
                if (isGetter(field, method)) {
                    target.put(field.getName(), createGetter(method));
                    break;
                }
            }
        }
        return target;
    }

    private boolean isGetter(Field field, Method method) {
        if (method.getParameterCount() != 0 || !field.getType().equals(method.getReturnType())) {
            return false;
        }
        String fieldName = field.getName();
        String capitalizedFieldName = getCapitalized(fieldName);
        String methodName = method.getName();
        return isGetter(methodName, capitalizedFieldName);
    }

    private String getCapitalized(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private boolean isGetter(String methodName, String lastMethodNamePart) {
        return (methodName.startsWith("get") && methodName.equals("get" + lastMethodNamePart))
                || (methodName.startsWith("is") && methodName.equals("is" + lastMethodNamePart));
    }

    private Function<Man, Object> createGetter(Method method) {
        return instance -> {
            try {
                return method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(instance.toString(), e);
            }
        };
    }
}
