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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Domain10GetterMap implements Map<String, Function<Domain10, Object>> {
    private final HashSet<String> keys;
    private final HashSet<Function<Domain10, Object>> values;
    private final Set<Map.Entry<String, Function<Domain10, Object>>> entries;

    public Domain10GetterMap() {
        keys = new HashSet<>(Arrays.asList(
                "int0",
                "int1",
                "int2",
                "int3",
                "int4",
                "string5",
                "string6",
                "string7",
                "string8",
                "string9"));
        values = new HashSet<>(Arrays.asList(
                Domain10::getInt0,
                Domain10::getInt1,
                Domain10::getInt2,
                Domain10::getInt3,
                Domain10::getInt4,
                Domain10::getString5,
                Domain10::getString6,
                Domain10::getString7,
                Domain10::getString8,
                Domain10::getString9));
        entries = new HashSet<>(Arrays.asList(
                Entry.of("int0", Domain10::getInt0),
                Entry.of("int1", Domain10::getInt1),
                Entry.of("int2", Domain10::getInt2),
                Entry.of("int3", Domain10::getInt3),
                Entry.of("int4", Domain10::getInt4),
                Entry.of("string5", Domain10::getString5),
                Entry.of("string6", Domain10::getString6),
                Entry.of("string7", Domain10::getString7),
                Entry.of("string8", Domain10::getString8),
                Entry.of("string9", Domain10::getString9)));
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
        FieldName fieldName = getFieldName(key);
        if (fieldName == null) return null;
        switch (fieldName) {
            case int0:
                return Domain10::getInt0;
            case int1:
                return Domain10::getInt1;
            case int2:
                return Domain10::getInt2;
            case int3:
                return Domain10::getInt3;
            case int4:
                return Domain10::getInt4;
            case string5:
                return Domain10::getString5;
            case string6:
                return Domain10::getString6;
            case string7:
                return Domain10::getString7;
            case string8:
                return Domain10::getString8;
            case string9:
                return Domain10::getString9;
            default:
                throw new IllegalStateException("Not possible error for key: " + key);
        }
    }

    private FieldName getFieldName(Object key) {
        try {
            return FieldName.valueOf(key.toString());
        } catch (IllegalArgumentException ignore) {
            return null;
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
    public Set<Map.Entry<String, Function<Domain10, Object>>> entrySet() {
        return entries;
    }
// unsupported

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

    enum FieldName {
        int0,
        int1,
        int2,
        int3,
        int4,
        string5,
        string6,
        string7,
        string8,
        string9
    }

    static abstract class Entry implements Map.Entry<String, Function<Domain10, Object>> {
        static Entry of(String key, Function<Domain10, Object> value) {
            return new Entry() {
                @Override
                public String getKey() {
                    return key;
                }

                @Override
                public Function<Domain10, Object> getValue() {
                    return value;
                }
            };
        }

        @Override
        public Function<Domain10, Object> setValue(Function<Domain10, Object> value) {
            throw new UnsupportedOperationException();
        }
    }
}
