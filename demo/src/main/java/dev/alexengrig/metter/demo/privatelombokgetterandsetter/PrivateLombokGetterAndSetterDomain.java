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

package dev.alexengrig.metter.demo.privatelombokgetterandsetter;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@GetterSupplier
@SetterSupplier
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class PrivateLombokGetterAndSetterDomain {
    private int integer;
    private String string;
    private boolean bool;
}
