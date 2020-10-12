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

package dev.alexengrig.metter.demo.failmethod;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier
@SetterSupplier
public class FailMethodDomain {
    private int integer;
    private int failCountParams;
    private int failTypeParams;

    public FailMethodDomain(int integer, int failCountParams, int failTypeParams) {
        this.integer = integer;
        this.failCountParams = failCountParams;
        this.failTypeParams = failTypeParams;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public int getFakeParams(int unused) {
        return failCountParams;
    }

    public void setFakeParams(int fakeParams, int unused) {
        this.failCountParams = fakeParams;
    }

    public double getFailTypeParams() {
        return failTypeParams;
    }

    public void setFailTypeParams(double failTypeParams) {
        this.failTypeParams = (int) failTypeParams;
    }
}
