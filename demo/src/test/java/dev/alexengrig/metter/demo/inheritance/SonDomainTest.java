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

package dev.alexengrig.metter.demo.inheritance;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SonDomainTest extends BaseDomainTest<SonDomain> {
    @Test
    public void should_contains_allGettersWithSuper() {
        Map<String, Function<SonDomain, Object>> getterByField = getGetterMap(new SonDomainGetterSupplier());
        assertSize(getterByField, 2);
        assertGetterFields(getterByField, "fatherInt", "sonInt");
        SonDomain domain = new SonDomain(1, 2);
        assertGetterValue(getterByField, domain, "fatherInt", 1);
        assertGetterValue(getterByField, domain, "sonInt", 2);
    }

    @Test
    public void should_contains_allSettersWithSuper() {
        Map<String, BiConsumer<SonDomain, Object>> setterByField = getSetterMap(new SonDomainSetterSupplier());
        assertSize(setterByField, 2);
        assertSetterFields(setterByField, "fatherInt", "sonInt");
        SonDomain domain = new SonDomain(1, 2);
        assertSetterValue(setterByField, domain, "fatherInt", 10, SonDomain::getFatherInt);
        assertSetterValue(setterByField, domain, "sonInt", 20, SonDomain::getSonInt);
    }
}