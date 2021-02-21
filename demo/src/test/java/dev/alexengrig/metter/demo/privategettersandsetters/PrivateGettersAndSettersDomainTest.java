/*
 * Copyright 2021 Alexengrig Dev.
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

package dev.alexengrig.metter.demo.privategettersandsetters;

import org.junit.Test;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.Assert.assertTrue;

public class PrivateGettersAndSettersDomainTest {
    @Test
    public void should_ignores_privateGetters() {
        Map<String, Function<PrivateGettersAndSettersDomain, Object>> getterByField
                = new PrivateGettersAndSettersDomainGetterSupplier().get();
        assertTrue("Map is not empty", getterByField.isEmpty());
    }

    @Test
    public void should_ignores_privateSetters() {
        Map<String, BiConsumer<PrivateGettersAndSettersDomain, Object>> setterByField
                = new PrivateGettersAndSettersDomainSetterSupplier().get();
        assertTrue("Map is not empty", setterByField.isEmpty());
    }
}
