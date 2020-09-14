/*
 * Copyright 2020 Alexengrig Dev.
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ManualPersonSetterSupplier implements Supplier<Map<String, BiConsumer<Person, Object>>> {
    protected final Map<String, BiConsumer<Person, Object>> setterByField;

    public ManualPersonSetterSupplier() {
        this.setterByField = new HashMap<>(4);
        this.setterByField.put("integer", (instance, value) -> instance.setInteger((Integer) value));
        this.setterByField.put("string", (instance, value) -> instance.setString((String) value));
        this.setterByField.put("enable", (instance, value) -> instance.setEnable((Boolean) value));
    }

    @Override
    public Map<String, BiConsumer<Person, Object>> get() {
        return setterByField;
    }
}
