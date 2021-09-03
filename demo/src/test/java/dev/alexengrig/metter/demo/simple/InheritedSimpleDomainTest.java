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

package dev.alexengrig.metter.demo.simple;

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class InheritedSimpleDomainTest extends BaseDomainTest<InheritedSimpleDomain> {
    @Test
    public void should_contains_allGetters() {
        Map<String, Function<InheritedSimpleDomain, Object>> getterByField
                = getGetterMap(new InheritedSimpleDomainGetterSupplier());
        assertSize(getterByField, 4);
        assertGetterFields(getterByField, "integer", "bool", "string", "longer");
        InheritedSimpleDomain domain = new InheritedSimpleDomain(1, true, "text", 4);
        assertGetterValue(getterByField, domain, "integer", 1);
        assertGetterValue(getterByField, domain, "bool", true);
        assertGetterValue(getterByField, domain, "string", "text");
        assertGetterValue(getterByField, domain, "longer", 4L);
    }

    @Test
    public void should_contains_allSetters() {
        Map<String, BiConsumer<InheritedSimpleDomain, Object>> setterByField
                = getSetterMap(new InheritedSimpleDomainSetterSupplier());
        assertSize(setterByField, 4);
        assertSetterFields(setterByField, "integer", "bool", "string", "longer");
        InheritedSimpleDomain domain = new InheritedSimpleDomain(1, true, "text", 4);
        assertSetterValue(setterByField, domain, "integer", 10, InheritedSimpleDomain::getInteger);
        assertSetterValue(setterByField, domain, "bool", false, InheritedSimpleDomain::isBool);
        assertSetterValue(setterByField, domain, "string", "new", InheritedSimpleDomain::getString);
        assertSetterValue(setterByField, domain, "longer", 40L, InheritedSimpleDomain::getLonger);
    }

}