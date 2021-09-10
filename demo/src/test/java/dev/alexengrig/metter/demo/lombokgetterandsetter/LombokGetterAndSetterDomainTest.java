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

package dev.alexengrig.metter.demo.lombokgetterandsetter;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LombokGetterAndSetterDomainTest extends BaseDomainTest<LombokGetterAndSetterDomain> {
    @Test
    public void should_contains_allGetters() {
        Map<String, Function<LombokGetterAndSetterDomain, Object>> getterByField = getGetterMap(new LombokGetterAndSetterDomainGetterSupplier());
        assertSize(getterByField, 2);
        assertGetterFields(getterByField, "integer", "bool");
        LombokGetterAndSetterDomain domain = new LombokGetterAndSetterDomain(1, true);
        assertGetterValue(getterByField, domain, "integer", 1);
        assertGetterValue(getterByField, domain, "bool", true);
    }

    @Test
    public void should_contains_allSetters() {
        Map<String, BiConsumer<LombokGetterAndSetterDomain, Object>> setterByField = getSetterMap(new LombokGetterAndSetterDomainSetterSupplier());
        assertSize(setterByField, 2);
        assertSetterFields(setterByField, "integer", "bool");
        LombokGetterAndSetterDomain domain = new LombokGetterAndSetterDomain(1, true);
        assertSetterValue(setterByField, domain, "integer", 10, LombokGetterAndSetterDomain::getInteger);
        assertSetterValue(setterByField, domain, "bool", false, LombokGetterAndSetterDomain::isBool);
    }
}