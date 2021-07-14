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

package dev.alexengrig.metter.demo.includingandexcluding;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class InheritedIncludedAndExcludedDomainTest extends BaseDomainTest<InheritedIncludedAndExcludedDomain> {
    @Test
    public void should_contains_allIncludedGetters() {
        Map<String, Function<InheritedIncludedAndExcludedDomain, Object>> getterByField
                = getGetterMap(new InheritedIncludedAndExcludedDomainGetterSupplier());
        assertSize(getterByField, 2);
        assertGetterFields(getterByField, "included");
        assertGetterFields(getterByField, "alsoIncluded");
        InheritedIncludedAndExcludedDomain domain =
                new InheritedIncludedAndExcludedDomain(1, 2, 3, 4, 5, 6);
        assertGetterValue(getterByField, domain, "included", 1);
        assertGetterValue(getterByField, domain, "alsoIncluded", 4);
        assertEquals("Ignored field value is incorrect", 2, domain.getIgnored());
        assertEquals("Excluded field value is incorrect", 3, domain.getExcluded());
        assertEquals("AlsoIgnored field value is incorrect", 5, domain.getAlsoIgnored());
        assertEquals("AlsoExcluded field value is incorrect", 6, domain.getAlsoExcluded());
    }

    @Test
    public void should_contains_allIncludedSetters() {
        Map<String, BiConsumer<InheritedIncludedAndExcludedDomain, Object>> setterByField =
                getSetterMap(new InheritedIncludedAndExcludedDomainSetterSupplier());
        assertSize(setterByField, 2);
        assertSetterFields(setterByField, "included");
        assertSetterFields(setterByField, "alsoIncluded");
        InheritedIncludedAndExcludedDomain domain =
                new InheritedIncludedAndExcludedDomain(1, 2, 3, 4, 5, 6);
        assertSetterValue(setterByField, domain, "included", 10, InheritedIncludedAndExcludedDomain::getIncluded);
        assertSetterValue(setterByField, domain, "alsoIncluded", 40, InheritedIncludedAndExcludedDomain::getAlsoIncluded);
        domain.setIgnored(20);
        assertEquals("Ignored field value is incorrect", 20, domain.getIgnored());
        domain.setExcluded(30);
        assertEquals("Excluded field value is incorrect", 30, domain.getExcluded());
        domain.setAlsoIgnored(50);
        assertEquals("AlsoIgnored field value is incorrect", 50, domain.getAlsoIgnored());
        domain.setAlsoExcluded(60);
        assertEquals("AlsoExcluded field value is incorrect", 60, domain.getAlsoExcluded());
    }
}