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

package dev.alexengrig.metter.demo.naming;

import dev.alexengrig.metter.demo.BaseDomainTest;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NamedDomainTest extends BaseDomainTest<NamedDomain> {
    @Override
    protected Supplier<Map<String, Function<NamedDomain, Object>>> createGetterSupplier() {
        return new MyDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<NamedDomain, Object>>> createSetterSupplier() {
        return new MyDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("integer");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1);
    }

    @Override
    protected Function<NamedDomain, Object>[] getFieldGetters() {
        return createGetters(NamedDomain::getInteger);
    }

    @Override
    protected NamedDomain createDomain() {
        return new NamedDomain(1);
    }
}