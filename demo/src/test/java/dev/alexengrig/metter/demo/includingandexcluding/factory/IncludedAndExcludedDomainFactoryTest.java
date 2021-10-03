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

package dev.alexengrig.metter.demo.includingandexcluding.factory;

import dev.alexengrig.metter.demo.BaseDomainFactoryTest;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.includingandexcluding.IncludedAndExcludedDomain;

import java.util.function.Function;

public class IncludedAndExcludedDomainFactoryTest extends BaseDomainFactoryTest<IncludedAndExcludedDomain> {
    @Override
    protected Factory<IncludedAndExcludedDomain> createFactory() {
        return new IncludedAndExcludedDomainFactory();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("included");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1);
    }

    @Override
    protected Function<IncludedAndExcludedDomain, Object>[] getFieldGetters() {
        return createGetters(IncludedAndExcludedDomain::getIncluded);
    }

    @Override
    protected IncludedAndExcludedDomain createDomain() {
        return new IncludedAndExcludedDomain(1, 2, 3);
    }
}