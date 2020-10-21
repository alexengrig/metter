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

package dev.alexengrig.metter.element.descriptor;

import dev.alexengrig.metter.element.collector.FieldCollector;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.HashMap;
import java.util.Map;
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
public class FieldDescriptor {
    /**
     * Variable element.
     *
     * @since 0.1.0
     */
    protected final VariableElement variableElement;
    /**
     * Parent - type descriptor.
     *
     * @since 0.1.1
     */
    protected transient TypeDescriptor parent;
    /**
     * Name.
     *
     * @since 0.1.0
     */
    protected transient String name;
    /**
     * Type name.
     *
     * @since 0.1.0
     */
    protected transient String typeName;
    /**
     * Set of annotation descriptors.
     *
     * @since 0.1.0
     */
    protected transient Set<AnnotationDescriptor> annotations;
    /**
     * Map of annotation qualified name to mark about presence.
     *
     * @since 0.1.0
     */
    protected transient Map<String, Boolean> hasAnnotationByQualifiedNameMap;

    /**
     * Constructs with a variable element.
     *
     * @param variableElement variable element
     * @since 0.1.0
     */
    public FieldDescriptor(VariableElement variableElement) {
        Objects.requireNonNull(variableElement, "Variable element must not be null");
        if (variableElement.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException("Variable element must be field kind");
        }
        this.variableElement = variableElement;
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
            parent = new TypeDescriptor((TypeElement) variableElement.getEnclosingElement());
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
        if (name == null) {
            name = variableElement.getSimpleName().toString();
        }
        return name;
    }

    /**
     * Returns a type name.
     *
     * @return type name
     * @since 0.1.0
     */
    public String getTypeName() {
        if (typeName == null) {
            typeName = variableElement.asType().toString();
        }
        return typeName;
    }

    /**
     * Returns a set of annotation descriptors.
     *
     * @return set of annotation descriptors
     * @since 0.1.0
     */
    public Set<AnnotationDescriptor> getAnnotations() {
        if (annotations == null) {
            annotations = AnnotationDescriptor.of(variableElement);
        }
        return annotations;
    }

    /**
     * Check if has a annotation by an qualified name.
     *
     * @param annotationQualifiedName annotation qualified name
     * @return if has a annotation by {@code annotationQualifiedName}
     * @since 0.1.0
     */
    public boolean hasAnnotation(String annotationQualifiedName) {
        if (hasAnnotationByQualifiedNameMap == null) {
            hasAnnotationByQualifiedNameMap = new HashMap<>();
        } else if (hasAnnotationByQualifiedNameMap.containsKey(annotationQualifiedName)) {
            return hasAnnotationByQualifiedNameMap.get(annotationQualifiedName);
        }
        boolean hasAnnotation = getAnnotations().stream()
                .map(AnnotationDescriptor::getQualifiedName)
                .anyMatch(annotationQualifiedName::equals);
        hasAnnotationByQualifiedNameMap.put(annotationQualifiedName, hasAnnotation);
        return hasAnnotation;
    }
}
