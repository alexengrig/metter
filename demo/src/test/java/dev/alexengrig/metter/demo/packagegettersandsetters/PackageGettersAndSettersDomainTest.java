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

package dev.alexengrig.metter.demo.packagegettersandsetters;

import dev.alexengrig.metter.demo.BaseDomainTest;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PackageGettersAndSettersDomainTest extends BaseDomainTest<PackageGettersAndSettersDomain> {
    @Override
    protected Supplier<Map<String, Function<PackageGettersAndSettersDomain, Object>>> createGetterSupplier() {
        return new PackageGettersAndSettersDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<PackageGettersAndSettersDomain, Object>>> createSetterSupplier() {
        return new PackageGettersAndSettersDomainSetterSupplier();
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
    protected Function<PackageGettersAndSettersDomain, Object>[] getFieldGetters() {
        return createGetters(
                PackageGettersAndSettersDomain::getInteger,
                PackageGettersAndSettersDomain::isBool);
    }

    @Override
    protected PackageGettersAndSettersDomain createDomain() {
        return new PackageGettersAndSettersDomain(1, true);
    }
}