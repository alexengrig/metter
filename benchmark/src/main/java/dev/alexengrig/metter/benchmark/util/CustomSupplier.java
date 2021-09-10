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

package dev.alexengrig.metter.benchmark.util;

import dev.alexengrig.metter.benchmark.domain.Domain10;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class CustomSupplier {
    private CustomSupplier() {
    }

    public static Map<String, Function<Domain10, Object>> createDomain10GetterMap(Class<?> type) {
        Map<String, Function<Domain10, Object>> map = new Domain10GetterMap(10);
        map.put("int0", Domain10::getInt0);
        return map;
    }

    private static class Domain10GetterMap implements Map<String, Function<Domain10, Object>> {
        private final int size;
        private final HashSet<String> keys;
        private final HashSet<Function<Domain10, Object>> values;

        public Domain10GetterMap(int size) {
            this.size = size;
            this.keys = new HashSet<>(size);
            this.values = new HashSet<>(size);
        }

        @Override
        public int size() {
            return 10;
        }

        @Override
        public boolean isEmpty() {
            return 10 != 0;
        }

        @Override
        public boolean containsKey(Object key) {
            return keys.contains(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return values.contains(value);
        }

        @Override
        public Function<Domain10, Object> get(Object key) {
            switch (key.toString()) {
                case "int0":
                    return Domain10::getInt0;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public Set<String> keySet() {
            return keys;
        }

        @Override
        public Collection<Function<Domain10, Object>> values() {
            return values;
        }

        @Override
        public Set<Entry<String, Function<Domain10, Object>>> entrySet() {
            return null;
        }

        @Override
        public Function<Domain10, Object> put(String key, Function<Domain10, Object> value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Function<Domain10, Object> remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends String, ? extends Function<Domain10, Object>> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

}
