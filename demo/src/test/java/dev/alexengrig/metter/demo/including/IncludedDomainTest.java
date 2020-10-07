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

package dev.alexengrig.metter.demo.including;

import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IncludedDomainTest {
    @Test
    public void should_contains_allIncludedGetters() {
        Map<String, Function<IncludedDomain, Object>> getterByField = new IncludedDomainGetterSupplier().get();
        assertNotNull("Map is null", getterByField);
        assertEquals("Map size not equal to 1", 1, getterByField.size());
        assertTrue("Map not contain getter for 'included' field", getterByField.containsKey("included"));
        assertFalse("Map contains getter for 'ignored' field", getterByField.containsKey("ignored"));
        IncludedDomain domain = new IncludedDomain(1, 2);
        assertEquals("Getter for 'included' field returns wrong value",
                1, getterByField.get("included").apply(domain));
    }

    @Test
    public void should_contains_allIncludedSetters() {
        Map<String, BiConsumer<IncludedDomain, Object>> setterByField = new IncludedDomainSetterSupplier().get();
        assertNotNull("Map is null", setterByField);
        assertEquals("Map size not equal to 1", 1, setterByField.size());
        assertTrue("Map not contain setter for 'included' field", setterByField.containsKey("included"));
        assertFalse("Map contains setter for 'ignored' field", setterByField.containsKey("ignored"));
        IncludedDomain domain = new IncludedDomain(0, 2);
        setterByField.get("included").accept(domain, 1);
        assertEquals("Setter for 'included' field sets wrong value", 1, domain.getIncluded());
    }
}