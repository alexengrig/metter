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

package dev.alexengrig.metter.demo.privatemethod;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier
@SetterSupplier
public class PrivateMethodDomain {
    private int integer;
    private int ignored;

    public PrivateMethodDomain(int integer, int ignored) {
        this.integer = integer;
        this.ignored = ignored;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    private int getIgnored() {
        return ignored;
    }

    private void setIgnored(int ignored) {
        this.ignored = ignored;
    }
}
