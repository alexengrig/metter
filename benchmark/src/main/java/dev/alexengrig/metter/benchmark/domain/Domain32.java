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

package dev.alexengrig.metter.benchmark.domain;

import dev.alexengrig.metter.annotation.GetterSupplier;
import lombok.Getter;

@Getter
@GetterSupplier
public class Domain32 extends Domain16 {
    private int int4;
    private int int5;
    private int int6;
    private int int7;
    private long long4;
    private long long5;
    private long long6;
    private long long7;
    private boolean bool4;
    private boolean bool5;
    private boolean bool6;
    private boolean bool7;
    private String string4;
    private String string5;
    private String string6;
    private String string7;
}
