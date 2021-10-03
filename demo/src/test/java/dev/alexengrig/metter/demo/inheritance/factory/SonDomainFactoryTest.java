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

package dev.alexengrig.metter.demo.inheritance.factory;

import dev.alexengrig.metter.demo.BaseDomainFactoryTest;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.inheritance.SonDomain;

import java.util.function.Function;

public class SonDomainFactoryTest extends BaseDomainFactoryTest<SonDomain> {
    @Override
    protected Factory<SonDomain> createFactory() {
        return new SonDomainFactory();
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