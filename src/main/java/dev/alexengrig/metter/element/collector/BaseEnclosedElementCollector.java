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

package dev.alexengrig.metter.element.collector;

import dev.alexengrig.metter.element.BaseElementVisitor;

import javax.lang.model.element.Element;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseEnclosedElementCollector<E extends Element, T extends Element> extends BaseElementVisitor {
    protected final E parent;
    protected transient Set<T> children;

    public BaseEnclosedElementCollector(E parent) {
        this.parent = parent;
    }

    public Set<T> getChildren() {
        if (children == null) {
            children = new HashSet<>();
            for (Element enclosedElement : parent.getEnclosedElements()) {
                enclosedElement.accept(this, null);
            }
        }
        return children;
    }
}
