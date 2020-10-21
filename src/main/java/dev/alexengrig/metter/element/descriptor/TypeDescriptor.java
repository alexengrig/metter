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


import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A descriptor of type.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class TypeDescriptor {
    /**
     * Type element.
     *
     * @since 0.1.0
     */
    protected final TypeElement typeElement;
    /**
     * Qualified name.
     *
     * @since 0.1.0
     */
    protected transient String qualifiedName;
    /**
     * Simple name.
     *
     * @since 0.1.0
     */
    protected transient String simpleName;
    /**
     * Set of field descriptors.
     *
     * @since 0.1.0
     */
    protected transient Set<FieldDescriptor> fields;
    /**
     * Set of method descriptors.
     *
     * @since 0.1.0
     */
    protected transient Set<MethodDescriptor> methods;
    /**
     * Set of annotation descriptors.
     *
     * @since 0.1.0
     */
    protected transient Set<AnnotationDescriptor> annotations;
    /**
     * Map of method name to mark about presence
     *
     * @since 0.1.0
     */
    protected transient Map<String, Boolean> hasMethodByNameMap;
    /**
     * Map of annotation qualified name to mark about presence
     *
     * @since 0.1.0
     */
    protected transient Map<String, Boolean> hasAnnotationByQualifiedNameMap;

    /**
     * Constructs with a type element.
     *
     * @param typeElement type element
     * @since 0.1.0
     */
    public TypeDescriptor(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    /**
     * Returns an qualified name.
     *
     * @return qualified name
     * @since 0.1.0
     */
    public String getQualifiedName() {
        if (qualifiedName == null) {
            qualifiedName = typeElement.getQualifiedName().toString();
        }
        return qualifiedName;
    }

    /**
     * Returns a simple name.
     *
     * @return simple name
     * @since 0.1.0
     */
    public String getSimpleName() {
        if (simpleName == null) {
            simpleName = typeElement.getSimpleName().toString();
        }
        return simpleName;
    }

    /**
     * Returns a set of field descriptors.
     *
     * @return set of field descriptors
     * @since 0.1.0
     */
    public Set<FieldDescriptor> getFields() {
        if (fields == null) {
            fields = FieldDescriptor.of(typeElement);
        }
        return fields;
    }

    /**
     * Returns a set of method descriptors.
     *
     * @return set of method descriptors
     * @since 0.1.0
     */
    public Set<MethodDescriptor> getMethods() {
        if (methods == null) {
            methods = MethodDescriptor.of(typeElement);
        }
        return methods;
    }

    /**
     * Returns a set of method descriptors by a name.
     *
     * @return set of method descriptors by a name
     * @since 0.1.1
     */
    public Set<MethodDescriptor> getMethods(String methodName) {
        return getMethods().stream()
                .filter(method -> methodName.equals(method.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Check if has a method by a name.
     *
     * @param methodName annotation qualified name
     * @return if has a method by {@code methodName}
     * @since 0.1.0
     */
    public boolean hasMethod(String methodName) {
        if (hasMethodByNameMap == null) {
            hasMethodByNameMap = new HashMap<>();
        } else if (hasMethodByNameMap.containsKey(methodName)) {
            return hasMethodByNameMap.get(methodName);
        }
        boolean hasMethod = getMethods().stream()
                .map(MethodDescriptor::getName)
                .anyMatch(methodName::equals);
        hasMethodByNameMap.put(methodName, hasMethod);
        return hasMethod;
    }

    /**
     * Returns a set of annotation descriptors.
     *
     * @return set of annotation descriptors
     * @since 0.1.0
     */
    public Set<AnnotationDescriptor> getAnnotations() {
        if (annotations == null) {
            annotations = AnnotationDescriptor.of(typeElement);
        }
        return annotations;
    }

    /**
     * Checks if has an annotation by an qualified name.
     *
     * @param annotationQualifiedName annotation qualified name
     * @return if has an annotation by an qualified name
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

    public <T extends Annotation> boolean hasAnnotation(Class<? extends T> annotationType) {
        return typeElement.getAnnotation(annotationType) != null;
    }

    /**
     * Returns an annotation by a type if present, else {@code null}.
     *
     * @param annotationType annotation type
     * @param <T>            type of annotation
     * @return annotation by {@code annotationType} if present, else {@code null}
     * @since 0.1.0
     */
    public <T extends Annotation> T getAnnotation(Class<? extends T> annotationType) {
        return typeElement.getAnnotation(annotationType);
    }
}
