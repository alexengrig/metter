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

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@GetterSupplier(excludedFields = "excluded")
@SetterSupplier(excludedFields = "excluded")
public class Person {
    @SuppressWarnings("FieldCanBeLocal")
    private final int constant = 100;
    private int integer;
    private String string;
    private boolean enable;
    @SuppressWarnings({"unused", "RedundantSuppression"})
    private boolean withoutGetterAndSetter;
    private String excluded;
    @Getter(value = AccessLevel.PACKAGE)
    @Setter
    private String lombok;
    @Getter
    @Setter
    private boolean booleanLombok;
    @Getter
    @Setter
    private Boolean boxedBooleanLombok;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getConstant() {
        return constant;
    }

    public String getExcluded() {
        return excluded;
    }

    public void setExcluded(String excluded) {
        this.excluded = excluded;
    }
}
