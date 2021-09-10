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

package dev.alexengrig.metter.demo.including;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier(includedFields = {"included", "alsoIncluded"})
@SetterSupplier(includedFields = {"included", "alsoIncluded"})
public class InheritedIncludedDomain extends IncludedDomain {
    private int alsoIncluded;
    private int alsoIgnored;

    public InheritedIncludedDomain(int included, int ignored, int alsoIncluded, int alsoIgnored) {
        super(included, ignored);
        this.alsoIncluded = alsoIncluded;
        this.alsoIgnored = alsoIgnored;
    }

    public int getAlsoIncluded() {
        return alsoIncluded;
    }

    public void setAlsoIncluded(int alsoIncluded) {
        this.alsoIncluded = alsoIncluded;
    }

    public int getAlsoIgnored() {
        return alsoIgnored;
    }

    public void setAlsoIgnored(int alsoIgnored) {
        this.alsoIgnored = alsoIgnored;
    }
}
