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

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GrandsonDomainTest extends BaseDomainTest<GrandsonDomain> {
    @Override
    protected Supplier<Map<String, Function<GrandsonDomain, Object>>> createGetterSupplier() {
        return new GrandsonDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<GrandsonDomain, Object>>> createSetterSupplier() {
        return new GrandsonDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("fatherInt", "sonInt", "grandsonInt");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, 2, 3);
    }

    @Override
    protected Function<GrandsonDomain, Object>[] getFieldGetters() {
        return createGetters(
                GrandsonDomain::getFatherInt,
                GrandsonDomain::getSonInt,
                GrandsonDomain::getGrandsonInt);
    }

    @Override
    protected GrandsonDomain createDomain() {
        return new GrandsonDomain(1, 2, 3);
    }
}