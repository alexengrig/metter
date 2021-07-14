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

public class IncludedAndExcludedDomainTest extends BaseDomainTest<IncludedAndExcludedDomain> {
    @Test
    public void should_contains_allIncludedGetters() {
        Map<String, Function<IncludedAndExcludedDomain, Object>> getterByField = getGetterMap(new IncludedAndExcludedDomainGetterSupplier());
        assertSize(getterByField, 1);
        assertGetterFields(getterByField, "included");
        IncludedAndExcludedDomain domain = new IncludedAndExcludedDomain(1, 2, 3);
        assertGetterValue(getterByField, domain, "included", 1);
        assertEquals("Ignored field value is incorrect", 2, domain.getIgnored());
        assertEquals("Excluded field value is incorrect", 3, domain.getExcluded());
    }

    @Test
    public void should_contains_allIncludedSetters() {
        Map<String, BiConsumer<IncludedAndExcludedDomain, Object>> setterByField = getSetterMap(new IncludedAndExcludedDomainSetterSupplier());
        assertSize(setterByField, 1);
        assertSetterFields(setterByField, "included");
        IncludedAndExcludedDomain domain = new IncludedAndExcludedDomain(1, 2, 3);
        assertSetterValue(setterByField, domain, "included", 10, IncludedAndExcludedDomain::getIncluded);
        domain.setIgnored(20);
        assertEquals("Ignored field value is incorrect", 20, domain.getIgnored());
        domain.setExcluded(30);
        assertEquals("Excluded field value is incorrect", 30, domain.getExcluded());
    }
}