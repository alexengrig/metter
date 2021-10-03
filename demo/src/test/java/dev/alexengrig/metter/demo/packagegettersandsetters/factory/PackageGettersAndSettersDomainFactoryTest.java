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

package dev.alexengrig.metter.demo.packagegettersandsetters.factory;

import dev.alexengrig.metter.demo.BaseDomainFactoryTest;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.packagegettersandsetters.PackageGettersAndSettersDomain;

import java.util.function.Function;

public class PackageGettersAndSettersDomainFactoryTest extends BaseDomainFactoryTest<PackageGettersAndSettersDomain> {
    @Override
    protected Factory<PackageGettersAndSettersDomain> createFactory() {
        return new PackageGettersAndSettersDomainFactory();
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
    protected Function<PackageGettersAndSettersDomain, Object>[] getFieldGetters() {
        return createGetters();
    }

    @Override
    protected PackageGettersAndSettersDomain createDomain() {
        return new PackageGettersAndSettersDomain(1, true);
    }
}