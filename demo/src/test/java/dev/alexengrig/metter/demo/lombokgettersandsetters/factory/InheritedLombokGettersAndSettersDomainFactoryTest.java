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

package dev.alexengrig.metter.demo.lombokgettersandsetters.factory;

import dev.alexengrig.metter.demo.BaseDomainFactoryTest;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.lombokgettersandsetters.InheritedLombokGettersAndSettersDomain;

import java.util.function.Function;

public class InheritedLombokGettersAndSettersDomainFactoryTest extends BaseDomainFactoryTest<InheritedLombokGettersAndSettersDomain> {
    @Override
    protected Factory<InheritedLombokGettersAndSettersDomain> createFactory() {
        return new InheritedLombokGettersAndSettersDomainFactory();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("integer", "bool", "longer");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, true, 4L);
    }

    @Override
    protected Function<InheritedLombokGettersAndSettersDomain, Object>[] getFieldGetters() {
        return createGetters(
                InheritedLombokGettersAndSettersDomain::getInteger,
                InheritedLombokGettersAndSettersDomain::isBool,
                InheritedLombokGettersAndSettersDomain::getLonger);
    }

    @Override
    protected InheritedLombokGettersAndSettersDomain createDomain() {
        return new InheritedLombokGettersAndSettersDomain(1, true, 3, 4);
    }
}