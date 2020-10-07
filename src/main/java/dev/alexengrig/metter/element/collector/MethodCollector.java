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

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Method collector.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class MethodCollector extends BaseEnclosedElementCollector<TypeElement, ExecutableElement> {
    /**
     * Constructs for a type element.
     *
     * @param parent type element
     * @since 0.1.0
     */
    public MethodCollector(TypeElement parent) {
        super(parent);
    }

    /**
     * Collects an executable element
     *
     * @param executableElement executable element
     * @since 0.1.0
     */
    @Override
    public void visitExecutable(ExecutableElement executableElement) {
        children.add(executableElement);
    }
}
