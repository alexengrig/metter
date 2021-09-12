/*
 * Copyright 2020-2021 Alexengrig Dev.
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

package dev.alexengrig.metter.benchmark.util;

import lombok.SneakyThrows;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;

public class HandlingUtils {
    private HandlingUtils() {
    }

    @SneakyThrows({NoSuchMethodException.class, IllegalAccessException.class})
    public static <T> Function<T, Object> getMethod(Class<T> type, String name, Class<?> returnType) {
        MethodHandles.Lookup lookup = MethodHandles.lookup().in(type);
        MethodHandle method = lookup.findVirtual(type, name, MethodType.methodType(returnType));
        return instance -> {
            try {
                return method.invoke(instance);
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }
}
