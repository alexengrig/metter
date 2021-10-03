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

package dev.alexengrig.metter.demo.protectedgettersandsetters;

import dev.alexengrig.metter.demo.BaseDomainTest;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ProtectedGettersAndSettersDomainTest extends BaseDomainTest<ProtectedGettersAndSettersDomain> {
    @Override
    protected Supplier<Map<String, Function<ProtectedGettersAndSettersDomain, Object>>> createGetterSupplier() {
        return new ProtectedGettersAndSettersDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<ProtectedGettersAndSettersDomain, Object>>> createSetterSupplier() {
        return new ProtectedGettersAndSettersDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("integer", "bool");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, true);
    }

    @Override
    protected Function<ProtectedGettersAndSettersDomain, Object>[] getFieldGetters() {
        return createGetters(
                ProtectedGettersAndSettersDomain::getInteger,
                ProtectedGettersAndSettersDomain::isBool);
    }

    @Override
    protected ProtectedGettersAndSettersDomain createDomain() {
        return new ProtectedGettersAndSettersDomain(1, true);
    }
}