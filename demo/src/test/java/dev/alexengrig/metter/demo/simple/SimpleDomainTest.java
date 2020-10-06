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
        assertTrue("Map not contain getter for 'simpleInt' field", getterByField.containsKey("simpleInt"));
        assertTrue("Map not contain getter for 'simpleBoolean' field", getterByField.containsKey("simpleBoolean"));
        assertTrue("Map not contain getter for 'simpleString' field", getterByField.containsKey("simpleString"));
        SimpleDomain domain = new SimpleDomain(1, true, "string");
        assertEquals("Getter for 'simpleInt' field returns wrong value",
                1, getterByField.get("simpleInt").apply(domain));
        assertEquals("Getter for 'simpleBoolean' field returns wrong value",
                true, getterByField.get("simpleBoolean").apply(domain));
        assertEquals("Getter for 'simpleString' field returns wrong value",
                "string", getterByField.get("simpleString").apply(domain));
    }

    @Test
    public void should_contains_allSetters() {
        Map<String, BiConsumer<SimpleDomain, Object>> setterByField = new SimpleDomainSetterSupplier().get();
        assertNotNull("Map is null", setterByField);
        assertEquals("Map size not equal to 3", 3, setterByField.size());
        assertTrue("Map not contain setter for 'simpleInt' field", setterByField.containsKey("simpleInt"));
        assertTrue("Map not contain setter for 'simpleBoolean' field", setterByField.containsKey("simpleBoolean"));
        assertTrue("Map not contain setter for 'simpleString' field", setterByField.containsKey("simpleString"));
        SimpleDomain domain = new SimpleDomain(0, false, "");
        setterByField.get("simpleInt").accept(domain, 1);
        assertEquals("Setter for 'simpleInt' field sets wrong value", 1, domain.getSimpleInt());
        setterByField.get("simpleBoolean").accept(domain, true);
        assertTrue("Setter for 'simpleBoolean' field sets wrong value", domain.isSimpleBoolean());
        setterByField.get("simpleString").accept(domain, "string");
        assertEquals("Setter for 'simpleString' field sets wrong value", "string", domain.getSimpleString());
    }
}