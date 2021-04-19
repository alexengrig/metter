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

package dev.alexengrig.metter.demo;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class BaseDomainTest<T> {
    protected Map<String, Function<T, Object>> getGetterMap(Supplier<Map<String, Function<T, Object>>> domainGetterSupplier) {
        Map<String, Function<T, Object>> getterByField = domainGetterSupplier.get();
        assertNotNull("Map is null", getterByField);
        return getterByField;
    }

    protected Map<String, BiConsumer<T, Object>> getSetterMap(Supplier<Map<String, BiConsumer<T, Object>>> domainSetterSupplier) {
        Map<String, BiConsumer<T, Object>> setterByField = domainSetterSupplier.get();
        assertNotNull("Map is null", setterByField);
        return setterByField;
    }

    protected void assertSize(Map<String, ?> map, int size) {
        assertEquals("Map size is incorrect", size, map.size());
    }

    protected void assertGetterFields(Map<String, Function<T, Object>> getterByField, String... fields) {
        for (String field : fields) {
            assertTrue("Map not contain getter for field '" + field + "'", getterByField.containsKey(field));
        }
    }

    protected void assertSetterFields(Map<String, BiConsumer<T, Object>> setterByField, String... fields) {
        for (String field : fields) {
            assertTrue("Map not contain setter for field '" + field + "'", setterByField.containsKey(field));
        }
    }

    protected void assertGetterValue(Map<String, Function<T, Object>> getterByField, T domain, String field, Object value) {
        assertEquals("Getter for field '" + field + "' returns wrong value", value, getterByField.get(field).apply(domain));
    }

    protected void assertSetterValue(Map<String, BiConsumer<T, Object>> setterByField, T domain, String field, Object value, Function<T, ?> getter) {
        setterByField.get(field).accept(domain, value);
        assertEquals("Setter for field '" + field + "' sets wrong value", value, getter.apply(domain));
    }
}
