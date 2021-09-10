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

package dev.alexengrig.metter.demo.excluding;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ExcludedDomainTest extends BaseDomainTest<ExcludedDomain> {
    @Test
    public void should_contains_allNotExcludedGetters() {
        Map<String, Function<ExcludedDomain, Object>> getterByField = getGetterMap(new ExcludedDomainGetterSupplier());
        assertSize(getterByField, 1);
        assertGetterFields(getterByField, "integer");
        ExcludedDomain domain = new ExcludedDomain(1, 2);
        assertGetterValue(getterByField, domain, "integer", 1);
        assertEquals("Excluded field value is incorrect", 2, domain.getExcluded());
    }

    @Test
    public void should_contains_allNotExcludedSetters() {
        Map<String, BiConsumer<ExcludedDomain, Object>> setterByField = getSetterMap(new ExcludedDomainSetterSupplier());
        assertSize(setterByField, 1);
        assertSetterFields(setterByField, "integer");
        ExcludedDomain domain = new ExcludedDomain(1, 2);
        assertSetterValue(setterByField, domain, "integer", 10, ExcludedDomain::getInteger);
        domain.setExcluded(20);
        assertEquals("Excluded field value is incorrect", 20, domain.getExcluded());
    }
}