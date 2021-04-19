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

import dev.alexengrig.metter.demo.BaseDomainTest;
import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NoPackageDomainTest extends BaseDomainTest<NoPackageDomain> {
    @Test
    public void should_contains_allGetters() {
        Map<String, Function<NoPackageDomain, Object>> getterByField = getGetterMap(new NoPackageDomainGetterSupplier());
        assertSize(getterByField, 1);
        assertGetterFields(getterByField, "integer");
        NoPackageDomain domain = new NoPackageDomain(1);
        assertGetterValue(getterByField, domain, "integer", 1);
    }

    @Test
    public void should_contains_allSetters() {
        Map<String, BiConsumer<NoPackageDomain, Object>> setterByField = getSetterMap(new NoPackageDomainSetterSupplier());
        assertSize(setterByField, 1);
        assertSetterFields(setterByField, "integer");
        NoPackageDomain domain = new NoPackageDomain(1);
        assertSetterValue(setterByField, domain, "integer", 10, NoPackageDomain::getInteger);
    }
}