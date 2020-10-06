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

import java.util.StringJoiner;

class ManualManChangeLogGenerator extends BaseChangeLogGenerator<Man> {
    @Override
    public String generate(Man man, Man newMan) {
        StringJoiner joiner = new StringJoiner("\n");
        if (!man.getName().equals(newMan.getName())) {
            joiner.add(changeLog("name", man.getName(), newMan.getName()));
        }
        if (man.getAge() != newMan.getAge()) {
            joiner.add(changeLog("age", man.getAge(), newMan.getAge()));
        }
        return joiner.toString();
    }
}
