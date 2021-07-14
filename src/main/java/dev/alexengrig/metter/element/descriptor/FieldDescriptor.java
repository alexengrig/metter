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

package dev.alexengrig.metter.element.descriptor;

import dev.alexengrig.metter.element.collector.FieldCollector;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A descriptor of field.
 *
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.0
 */
public class FieldDescriptor extends ElementDescriptor<VariableElement> {
    /**
     * Parent - type descriptor.
     *
     * @since 0.1.1
     */
    protected transient TypeDescriptor parent;

    /**
     * Constructs with a variable element.
     *
     * @param variableElement variable element
     * @since 0.1.0
     */
    public FieldDescriptor(VariableElement variableElement) {
        super(requireValid(variableElement));
    }

    /**
     * Checks that a variable element is valid.
     *
     * @param variableElement variable element
     * @return {@code variableElement} is valid
     * @throws NullPointerException     if {@code variableElement} is {@code null}
     * @throws IllegalArgumentException if {@code variableElement} kind is not
     *                                  {@link javax.lang.model.element.ElementKind#FIELD}
     * @since 0.1.1
     */
    protected static VariableElement requireValid(VariableElement variableElement) {
        Objects.requireNonNull(variableElement, "Variable element must not be null");
        if (variableElement.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException("Variable element must be field kind");
        }
        return variableElement;
    }

    /**
     * Creates a set from a type element.
     *
     * @param typeElement type element
     * @return set from {@code typeElement}
     * @since 0.1.0
     */
    public static Set<FieldDescriptor> of(TypeElement typeElement) {
        FieldCollector fieldCollector = new FieldCollector(typeElement);
        return fieldCollector.getChildren().stream()
                .map(FieldDescriptor::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a parent - class.
     *
     * @return parent - class
     * @since 0.1.1
     */
    public TypeDescriptor getParent() {
        if (parent == null) {
            parent = new TypeDescriptor((TypeElement) element.getEnclosingElement());
        }
        return parent;
    }

    /**
     * Returns a name.
     *
     * @return name
     * @since 0.1.0
     */
    public String getName() {
        return element.getSimpleName().toString();
    }

    /**
     * Returns a type name.
     *
     * @return type name
     * @since 0.1.0
     */
    public String getTypeName() {
        return element.asType().toString();
    }
}
