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

package dev.alexengrig.metter.demo.simple;

import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimpleDomainTest {
    @Test
    public void should_contains_allGetters() {
        Map<String, Function<SimpleDomain, Object>> getterByField = new SimpleDomainGetterSupplier().get();
        assertNotNull("Map is null", getterByField);
        assertEquals("Map size not equal to 3", 3, getterByField.size());
        assertTrue("Map not contain getter for 'integer' field", getterByField.containsKey("integer"));
        assertTrue("Map not contain getter for 'bool' field", getterByField.containsKey("bool"));
        assertTrue("Map not contain getter for 'string' field", getterByField.containsKey("string"));
        SimpleDomain domain = new SimpleDomain(1, true, "text");
        assertEquals("Getter for 'integer' field returns wrong value",
                1, getterByField.get("integer").apply(domain));
        assertEquals("Getter for 'bool' field returns wrong value",
                true, getterByField.get("bool").apply(domain));
        assertEquals("Getter for 'string' field returns wrong value",
                "text", getterByField.get("string").apply(domain));
    }

    @Test
    public void should_contains_allSetters() {
        Map<String, BiConsumer<SimpleDomain, Object>> setterByField = new SimpleDomainSetterSupplier().get();
        assertNotNull("Map is null", setterByField);
        assertEquals("Map size not equal to 3", 3, setterByField.size());
        assertTrue("Map not contain setter for 'integer' field", setterByField.containsKey("integer"));
        assertTrue("Map not contain setter for 'bool' field", setterByField.containsKey("bool"));
        assertTrue("Map not contain setter for 'string' field", setterByField.containsKey("string"));
        SimpleDomain domain = new SimpleDomain(0, false, "");
        setterByField.get("integer").accept(domain, 1);
        assertEquals("Setter for 'integer' field sets wrong value", 1, domain.getInteger());
        setterByField.get("bool").accept(domain, true);
        assertTrue("Setter for 'bool' field sets wrong value", domain.isBool());
        setterByField.get("string").accept(domain, "text");
        assertEquals("Setter for 'string' field sets wrong value", "text", domain.getString());
    }
}