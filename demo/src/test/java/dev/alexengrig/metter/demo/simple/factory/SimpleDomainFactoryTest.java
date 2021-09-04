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

package dev.alexengrig.metter.demo.simple.factory;

import dev.alexengrig.metter.demo.BaseDomainFactoryTest;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.simple.SimpleDomain;

import java.util.function.Function;

public class SimpleDomainFactoryTest extends BaseDomainFactoryTest<SimpleDomain> {
    @Override
    protected Factory<SimpleDomain> createFactory() {
        return new SimpleDomainFactory();
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