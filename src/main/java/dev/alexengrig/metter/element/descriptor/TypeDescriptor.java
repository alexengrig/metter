/*
 * Copyright 2021 Alexengrig Dev.
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A type element descriptor.
 *
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.0
 */
public class TypeDescriptor extends ElementDescriptor<TypeElement> {
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
     * Map of method name to mark about presence
     *
     * @since 0.1.0
     */
    protected transient Map<String, Boolean> hasMethodByNameMap;

    /**
     * Constructs with a type element.
     *
     * @param typeElement type element
     * @since 0.1.0
     */
    public TypeDescriptor(TypeElement typeElement) {
        super(typeElement);
    }

    /**
     * Returns an qualified name.
     *
     * @return qualified name
     * @since 0.1.0
     */
    public String getQualifiedName() {
        return element.getQualifiedName().toString();
    }

    /**
     * Returns a simple name.
     *
     * @return simple name
     * @since 0.1.0
     */
    public String getSimpleName() {
        return element.getSimpleName().toString();
    }

    /**
     * Returns a set of field descriptors.
     *
     * @return set of field descriptors
     * @since 0.1.0
     */
    public Set<FieldDescriptor> getFields() {
        if (fields == null) {
            fields = FieldDescriptor.of(element);
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
            methods = MethodDescriptor.of(element);
        }
        return methods;
    }

    /**
     * Returns a set of method descriptors by a method name.
     *
     * @param methodName method name
     * @return set of method descriptors by {@code methodName}
     * @since 0.1.1
     */
    public Set<MethodDescriptor> getMethods(String methodName) {
        return getMethods().stream().filter(m -> methodName.equals(m.getName())).collect(Collectors.toSet());
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
}
