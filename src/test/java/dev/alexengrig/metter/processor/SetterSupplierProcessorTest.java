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

package dev.alexengrig.metter.processor;

import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class SetterSupplierProcessorTest {
    @Test
    public void should_create_supplierClass() {
        assertNotNull("Create supplier class: DomainSetterSupplier", DomainSetterSupplier.class);
    }

    @Test
    public void should_create_supplierClass_with_customName() {
        assertNotNull("Create supplier class: SetterSupplierForCustomDomain", SetterSupplierForCustomDomain.class);
    }

    @Test
    public void should_create_settersMap_with_allFields() {
        Map<String, BiConsumer<Domain, Object>> map = new DomainSetterSupplier().get();
        assertNotNull("Has setter for 'integer' field", map.get("integer"));
        assertNotNull("Has setter for 'bool' field", map.get("bool"));
        assertNotNull("Has setter for 'string' field", map.get("string"));
    }

    @Test
    public void should_create_settersMap_with_allSetters() {
        Domain domain = new Domain();
        domain.setInteger(1);
        domain.setBool(true);
        domain.setString("string");
        Domain other = new Domain();
        other.setInteger(-1);
        other.setBool(false);
        other.setString("other-string");
        Map<String, BiConsumer<Domain, Object>> map = new DomainSetterSupplier().get();
        assertNotEquals("Value of 'integer' field", domain.getInteger(), other.getInteger());
        map.get("integer").accept(domain, other.getInteger());
        assertEquals("Value of 'integer' field", domain.getInteger(), other.getInteger());
        assertNotEquals("Value of 'bool' field", domain.isBool(), other.isBool());
        map.get("bool").accept(domain, other.isBool());
        assertEquals("Value of 'bool' field", domain.isBool(), other.isBool());
        assertNotEquals("Value of 'string' field", domain.getString(), other.getString());
        map.get("string").accept(domain, other.getString());
        assertEquals("Value of 'string' field", domain.getString(), other.getString());
    }
}