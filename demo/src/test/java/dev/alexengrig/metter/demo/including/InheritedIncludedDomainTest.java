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

public class InheritedIncludedDomainTest extends BaseDomainTest<InheritedIncludedDomain> {
    @Test
    public void should_contains_allIncludedGetters() {
        Map<String, Function<InheritedIncludedDomain, Object>> getterByField = getGetterMap(new InheritedIncludedDomainGetterSupplier());
        assertSize(getterByField, 2);
        assertGetterFields(getterByField, "included", "alsoIncluded");
        InheritedIncludedDomain domain = new InheritedIncludedDomain(1, 2, 3, 4);
        assertGetterValue(getterByField, domain, "included", 1);
        assertGetterValue(getterByField, domain, "alsoIncluded", 3);
        assertEquals("Ignored field value is incorrect", 2, domain.getIgnored());
        assertEquals("Also ignored field value is incorrect", 4, domain.getAlsoIgnored());
    }

    @Test
    public void should_contains_allIncludedSetters() {
        Map<String, BiConsumer<InheritedIncludedDomain, Object>> setterByField = getSetterMap(new InheritedIncludedDomainSetterSupplier());
        assertSize(setterByField, 2);
        assertSetterFields(setterByField, "included", "alsoIncluded");
        InheritedIncludedDomain domain = new InheritedIncludedDomain(1, 2, 3, 4);
        assertSetterValue(setterByField, domain, "included", 10, InheritedIncludedDomain::getIncluded);
        assertSetterValue(setterByField, domain, "alsoIncluded", 30, InheritedIncludedDomain::getAlsoIncluded);
        domain.setIgnored(20);
        assertEquals("Ignored field value is incorrect", 20, domain.getIgnored());
        domain.setAlsoIgnored(40);
        assertEquals("Also ignored field value is incorrect", 40, domain.getAlsoIgnored());
    }
}