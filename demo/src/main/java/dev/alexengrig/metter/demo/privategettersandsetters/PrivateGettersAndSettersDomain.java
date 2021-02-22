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

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier
@SetterSupplier
public class PrivateGettersAndSettersDomain {
    private int integer;
    private String string;
    private boolean bool;

    private int getInteger() {
        return integer;
    }

    private void setInteger(int integer) {
        this.integer = integer;
    }

    private String getString() {
        return string;
    }

    private void setString(String string) {
        this.string = string;
    }

    private boolean isBool() {
        return bool;
    }

    private void setBool(boolean bool) {
        this.bool = bool;
    }
}
