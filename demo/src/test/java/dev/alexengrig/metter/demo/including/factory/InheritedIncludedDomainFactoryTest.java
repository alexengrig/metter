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

package dev.alexengrig.metter.demo.including.factory;

import dev.alexengrig.metter.demo.BaseDomainFactoryTest;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.including.InheritedIncludedDomain;

import java.util.function.Function;

public class InheritedIncludedDomainFactoryTest extends BaseDomainFactoryTest<InheritedIncludedDomain> {
    @Override
    protected Factory<InheritedIncludedDomain> createFactory() {
        return new InheritedIncludedDomainFactory();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("included", "alsoIncluded");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, 3);
    }

    @Override
    protected Function<InheritedIncludedDomain, Object>[] getFieldGetters() {
        return createGetters(
                InheritedIncludedDomain::getIncluded,
                InheritedIncludedDomain::getAlsoIncluded);
    }

    @Override
    protected InheritedIncludedDomain createDomain() {
        return new InheritedIncludedDomain(1, 2, 3, 4);
    }
}