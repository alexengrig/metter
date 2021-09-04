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

package dev.alexengrig.metter.demo.privatelombokgetterandsetter;

import dev.alexengrig.metter.demo.BaseDomainTest;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PrivateLombokGetterAndSetterDomainTest extends BaseDomainTest<PrivateLombokGetterAndSetterDomain> {
    @Override
    protected Supplier<Map<String, Function<PrivateLombokGetterAndSetterDomain, Object>>> createGetterSupplier() {
        return new PrivateLombokGetterAndSetterDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<PrivateLombokGetterAndSetterDomain, Object>>> createSetterSupplier() {
        return new PrivateLombokGetterAndSetterDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames();
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues();
    }

    @Override
    protected Function<PrivateLombokGetterAndSetterDomain, Object>[] getFieldGetters() {
        return createGetters();
    }

    @Override
    protected PrivateLombokGetterAndSetterDomain createDomain() {
        return new PrivateLombokGetterAndSetterDomain();
    }
}
