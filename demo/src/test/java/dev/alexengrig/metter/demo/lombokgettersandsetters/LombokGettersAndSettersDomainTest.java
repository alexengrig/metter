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

package dev.alexengrig.metter.demo.lombokgettersandsetters;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LombokGettersAndSettersDomainTest extends BaseDomainTest<LombokGettersAndSettersDomain> {
    @Test
    public void should_contains_allGetters() {
        Map<String, Function<LombokGettersAndSettersDomain, Object>> getterByField
                = getGetterMap(new LombokGettersAndSettersDomainGetterSupplier());
        assertSize(getterByField, 2);
        assertGetterFields(getterByField, "integer", "bool");
        LombokGettersAndSettersDomain domain = new LombokGettersAndSettersDomain(1, true, 2);
        assertGetterValue(getterByField, domain, "integer", 1);
        assertGetterValue(getterByField, domain, "bool", true);
    }

    @Test
    public void should_contains_allSetters() {
        Map<String, BiConsumer<LombokGettersAndSettersDomain, Object>> setterByField
                = getSetterMap(new LombokGettersAndSettersDomainSetterSupplier());
        assertSize(setterByField, 2);
        assertSetterFields(setterByField, "integer", "bool");
        LombokGettersAndSettersDomain domain = new LombokGettersAndSettersDomain(1, true, 2);
        assertSetterValue(setterByField, domain, "integer", 10, LombokGettersAndSettersDomain::getInteger);
        assertSetterValue(setterByField, domain, "bool", false, LombokGettersAndSettersDomain::isBool);
    }
}