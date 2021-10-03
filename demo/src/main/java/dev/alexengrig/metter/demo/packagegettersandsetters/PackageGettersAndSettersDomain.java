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

package dev.alexengrig.metter.demo.packagegettersandsetters;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier
@SetterSupplier
public class PackageGettersAndSettersDomain {
    private int integer;
    private boolean bool;

    public PackageGettersAndSettersDomain(int integer, boolean bool) {
        this.integer = integer;
        this.bool = bool;
    }

    /*package*/ int getInteger() {
        return integer;
    }

    /*package*/ void setInteger(int integer) {
        this.integer = integer;
    }

    /*package*/ boolean isBool() {
        return bool;
    }

    /*package*/ void setBool(boolean bool) {
        this.bool = bool;
    }
}
