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

public class SonDomainTest extends BaseDomainTest<SonDomain> {
    @Override
    protected Supplier<Map<String, Function<SonDomain, Object>>> createGetterSupplier() {
        return new SonDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<SonDomain, Object>>> createSetterSupplier() {
        return new SonDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("fatherInt", "sonInt");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, 2);
    }

    @Override
    protected Function<SonDomain, Object>[] getFieldGetters() {
        return createGetters(
                SonDomain::getFatherInt,
                SonDomain::getSonInt);
    }

    @Override
    protected SonDomain createDomain() {
        return new SonDomain(1, 2);
    }
}