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

package dev.alexengrig.metter.demo.excluding.factory;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.GetterSupplierFactory;
import dev.alexengrig.metter.annotation.SetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplierFactory;
import dev.alexengrig.metter.demo.Factory;
import dev.alexengrig.metter.demo.excluding.InheritedExcludedDomain;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class InheritedExcludedDomainFactory implements Factory<InheritedExcludedDomain> {
    @Override
    @GetterSupplierFactory(value = InheritedExcludedDomain.class, props =
    @GetterSupplier(excludedFields = {"excluded", "alsoExcluded"}))
    public Map<String, Function<InheritedExcludedDomain, Object>> getters() {
        return new InheritedExcludedDomainGetterSupplierFactory().get();
    }

    @Override
    @SetterSupplierFactory(value = InheritedExcludedDomain.class, props =
    @SetterSupplier(excludedFields = {"excluded", "alsoExcluded"}))
    public Map<String, BiConsumer<InheritedExcludedDomain, Object>> setters() {
        return new InheritedExcludedDomainSetterSupplierFactory().get();
    }
}
