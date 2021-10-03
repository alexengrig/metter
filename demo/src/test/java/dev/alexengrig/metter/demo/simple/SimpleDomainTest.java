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

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SimpleDomainTest extends BaseDomainTest<SimpleDomain> {
    @Override
    protected Supplier<Map<String, Function<SimpleDomain, Object>>> createGetterSupplier() {
        return new SimpleDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<SimpleDomain, Object>>> createSetterSupplier() {
        return new SimpleDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("integer", "bool", "string");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, true, "text");
    }

    @Override
    protected Function<SimpleDomain, Object>[] getFieldGetters() {
        return createGetters(
                SimpleDomain::getInteger,
                SimpleDomain::isBool,
                SimpleDomain::getString);
    }

    @Override
    protected SimpleDomain createDomain() {
        return new SimpleDomain(1, true, "text");
    }
}