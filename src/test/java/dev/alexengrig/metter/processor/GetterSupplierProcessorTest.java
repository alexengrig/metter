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

import dev.alexengrig.metter.processor.domain.CustomExclusionDomain;
import dev.alexengrig.metter.processor.domain.CustomExclusionDomainGetterSupplier;
import dev.alexengrig.metter.processor.domain.CustomInExclusionDomain;
import dev.alexengrig.metter.processor.domain.CustomInExclusionDomainGetterSupplier;
import dev.alexengrig.metter.processor.domain.CustomInclusionDomain;
import dev.alexengrig.metter.processor.domain.CustomInclusionDomainGetterSupplier;
import dev.alexengrig.metter.processor.domain.Domain;
import org.junit.Test;

import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GetterSupplierProcessorTest {
    @Test
    public void should_create_supplierClass() {
        assertNotNull("Create supplier class: DomainGetterSupplier", DomainGetterSupplier.class);
    }

    @Test
    public void should_create_supplierClass_with_customName() {
        assertNotNull("Create supplier class: GetterSupplierForCustomDomain", GetterSupplierForCustomDomain.class);
    }

    @Test
    public void should_create_gettersMap_with_allFields() {
        Map<String, Function<Domain, Object>> map = new DomainGetterSupplier().get();
        assertNotNull("Has getter for 'integer' field", map.get("integer"));
        assertNotNull("Has getter for 'bool' field", map.get("bool"));
        assertNotNull("Has getter for 'string' field", map.get("string"));
    }

    @Test
    public void should_create_gettersMap_with_allGetters() {
        Domain domain = new Domain();
        domain.setInteger(1);
        domain.setBool(true);
        domain.setString("string");
        Map<String, Function<Domain, Object>> map = new DomainGetterSupplier().get();
        assertEquals("Get value from 'integer' field", domain.getInteger(), map.get("integer").apply(domain));
        assertEquals("Get value from 'bool' field", domain.isBool(), map.get("bool").apply(domain));
        assertEquals("Get value from 'string' field", domain.getString(), map.get("string").apply(domain));
    }

    @Test
    public void should_create_gettersMap_with_includedFields() {
        Map<String, Function<CustomInclusionDomain, Object>> map = new CustomInclusionDomainGetterSupplier().get();
        assertFalse("No has getter for 'integer' field", map.containsKey("integer"));
        assertFalse("No has getter for 'excludedString' field", map.containsKey("excludedString"));
        assertTrue("Has getter for 'includedString' field", map.containsKey("includedString"));
    }

    @Test
    public void should_create_gettersMap_without_excludedFields() {
        Map<String, Function<CustomExclusionDomain, Object>> map = new CustomExclusionDomainGetterSupplier().get();
        assertFalse("No has getter for 'excludedString' field", map.containsKey("excludedString"));
        assertTrue("Has getter for 'integer' field", map.containsKey("integer"));
        assertTrue("Has getter for 'includedString' field", map.containsKey("includedString"));
    }

    @Test
    public void should_create_gettersMap_with_includedFields_and_ignore_excludedFields() {
        Map<String, Function<CustomInExclusionDomain, Object>> map = new CustomInExclusionDomainGetterSupplier().get();
        assertFalse("No has getter for 'integer' field", map.containsKey("integer"));
        assertFalse("No has getter for 'excludedString' field", map.containsKey("excludedString"));
        assertTrue("Has getter for 'includedString' field", map.containsKey("includedString"));
    }
}