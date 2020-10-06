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

package dev.alexengrig.metter.motivation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

class MapManChangeLogGenerator extends BaseChangeLogGenerator<Man> {
    protected Map<String, Function<Man, Object>> getterByField = createMap();

    protected Map<String, Function<Man, Object>> createMap() {
        return new HashMap<String, Function<Man, Object>>() {{
            put("name", Man::getName);
            put("age", Man::getAge);
        }};
    }

    @Override
    public String generate(Man man, Man newMan) {
        StringJoiner joiner = new StringJoiner("\n");
        getterByField.forEach((field, getter) -> {
            Object value = getter.apply(man);
            Object newValue = getter.apply(newMan);
            if (!Objects.equals(value, newValue)) {
                joiner.add(changeLog(field, value, newValue));
            }
        });
        return joiner.toString();
    }
}
