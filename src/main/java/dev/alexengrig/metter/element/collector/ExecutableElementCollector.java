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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

public class ExecutableElementCollector extends BaseElementVisitor {
    protected final TypeElement typeElement;
    protected transient Set<ExecutableElement> executableElements;

    public ExecutableElementCollector(TypeElement typeElement) {
        this.typeElement = typeElement;

    }

    public Set<ExecutableElement> getExecutableElements() {
        if (executableElements == null) {
            executableElements = new HashSet<>();
            for (Element enclosedElement : typeElement.getEnclosedElements()) {
                enclosedElement.accept(this, null);
            }
        }
        return executableElements;
    }

    @Override
    public void visitExecutable(ExecutableElement executableElement) {
        executableElements.add(executableElement);
    }
}
