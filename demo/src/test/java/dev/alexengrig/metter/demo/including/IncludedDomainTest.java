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

package dev.alexengrig.metter.demo.including;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class IncludedDomainTest extends BaseDomainTest<IncludedDomain> {
    @Test
    public void should_contains_allIncludedGetters() {
        Map<String, Function<IncludedDomain, Object>> getterByField = getGetterMap(new IncludedDomainGetterSupplier());
        assertSize(getterByField, 1);
        assertGetterFields(getterByField, "included");
        IncludedDomain domain = new IncludedDomain(1, 2);
        assertGetterValue(getterByField, domain, "included", 1);
        assertEquals("Ignored field value is incorrect", 2, domain.getIgnored());
    }

    @Test
    public void should_contains_allIncludedSetters() {
        Map<String, BiConsumer<IncludedDomain, Object>> setterByField = getSetterMap(new IncludedDomainSetterSupplier());
        assertSize(setterByField, 1);
        assertSetterFields(setterByField, "included");
        IncludedDomain domain = new IncludedDomain(1, 2);
        assertSetterValue(setterByField, domain, "included", 10, IncludedDomain::getIncluded);
        domain.setIgnored(20);
        assertEquals("Ignored field value is incorrect", 20, domain.getIgnored());
    }
}