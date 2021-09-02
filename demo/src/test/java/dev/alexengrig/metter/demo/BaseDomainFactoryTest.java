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

package dev.alexengrig.metter.demo;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertNotNull;

public class BaseDomainFactoryTest<T> extends BaseDomainTest<T> {
    protected Map<String, Function<T, Object>> getGetterMap(Factory<T> factory) {
        Map<String, Function<T, Object>> getterByField = factory.getters();
        assertNotNull("Map is null", getterByField);
        return getterByField;
    }

    protected Map<String, BiConsumer<T, Object>> getSetterMap(Factory<T> factory) {
        Map<String, BiConsumer<T, Object>> setterByField = factory.setters();
        assertNotNull("Map is null", setterByField);
        return setterByField;
    }
}
