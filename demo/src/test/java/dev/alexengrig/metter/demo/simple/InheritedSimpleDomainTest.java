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

public class InheritedSimpleDomainTest extends BaseDomainTest<InheritedSimpleDomain> {
    @Override
    protected Supplier<Map<String, Function<InheritedSimpleDomain, Object>>> createGetterSupplier() {
        return new InheritedSimpleDomainGetterSupplier();
    }

    @Override
    protected Supplier<Map<String, BiConsumer<InheritedSimpleDomain, Object>>> createSetterSupplier() {
        return new InheritedSimpleDomainSetterSupplier();
    }

    @Override
    protected String[] getFieldNames() {
        return createNames("integer", "bool", "string", "longer");
    }

    @Override
    protected Object[] getFieldValues() {
        return createValues(1, true, "text", 4L);
    }

    @Override
    protected Function<InheritedSimpleDomain, Object>[] getFieldGetters() {
        return createGetters(
                InheritedSimpleDomain::getInteger,
                InheritedSimpleDomain::isBool,
                InheritedSimpleDomain::getString,
                InheritedSimpleDomain::getLonger);
    }

    @Override
    protected InheritedSimpleDomain createDomain() {
        return new InheritedSimpleDomain(1, true, "text", 4);
    }
}