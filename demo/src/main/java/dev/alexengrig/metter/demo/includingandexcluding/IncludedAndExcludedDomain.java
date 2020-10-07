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

package dev.alexengrig.metter.demo.includingandexcluding;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier(includedFields = "included", excludedFields = "excluded")
@SetterSupplier(includedFields = "included", excludedFields = "excluded")
public class IncludedAndExcludedDomain {
    private int included;
    private int ignored;
    private int excluded;
    private byte noGetterAndSetter;

    public IncludedAndExcludedDomain(int included, int ignored, int excluded) {
        this.included = included;
        this.ignored = ignored;
        this.excluded = excluded;
    }

    public int getIncluded() {
        return included;
    }

    public void setIncluded(int included) {
        this.included = included;
    }

    public int getIgnored() {
        return ignored;
    }

    public void setIgnored(int ignored) {
        this.ignored = ignored;
    }

    public int getExcluded() {
        return excluded;
    }

    public void setExcluded(int excluded) {
        this.excluded = excluded;
    }
}
