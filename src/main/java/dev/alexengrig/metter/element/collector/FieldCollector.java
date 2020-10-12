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

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Field collector.
 *
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.0
 */
public class FieldCollector extends BaseEnclosedElementCollector<TypeElement, VariableElement> {
    /**
     * Constructs for a type element.
     *
     * @param parent type element
     * @since 0.1.0
     */
    public FieldCollector(TypeElement parent) {
        super(parent);
    }

    /**
     * Collects a variable element.
     *
     * @param variableElement variable element to visit
     * @since 0.1.0
     */
    @Override
    public void visitVariable(VariableElement variableElement) {
        if (variableElement.getKind() == ElementKind.FIELD) {
            children.add(variableElement);
        }
    }
}
