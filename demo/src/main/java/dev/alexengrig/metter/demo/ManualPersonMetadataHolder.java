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
import java.util.function.Function;

public class ManualPersonMetadataHolder implements Function<String, Function<Person, Object>> {
    private final Map<String, Function<Person, Object>> getterByField;

    public ManualPersonMetadataHolder() {
        this.getterByField = new HashMap<>(4);
        // TODO: As IntelliJ Live template
//        ${FIELD_TO_GETTER}(this.getterByField.put(${FIELD}, ${GETTER}));
        this.getterByField.put("constant", Person::getConstant);
        this.getterByField.put("integer", Person::getInteger);
        this.getterByField.put("string", Person::getString);
        this.getterByField.put("enable", Person::isEnable);
        this.getterByField.put("lombok", Person::getLombok);
    }

    @Override
    public Function<Person, Object> apply(String field) {
        if (!getterByField.containsKey(field)) {
            throw new IllegalArgumentException("No getter for field: " + field);
        }
        return getterByField.get(field);
    }
}
