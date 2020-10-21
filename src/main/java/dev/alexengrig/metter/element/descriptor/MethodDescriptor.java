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

import dev.alexengrig.metter.element.collector.MethodCollector;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A descriptor of method.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class MethodDescriptor {
    /**
     * Executable element.
     *
     * @since 0.1.0
     */
    protected final ExecutableElement executableElement;
    /**
     * Name.
     *
     * @since 0.1.0
     */
    protected transient String name;
    /**
     * Type name.
     *
     * @since 0.1.1
     */
    protected transient String typeName;
    /**
     * Mark about private modifier.
     *
     * @since 0.1.1
     */
    protected transient Boolean isPrivate;
    /**
     * Mark about empty parameters.
     *
     * @since 0.1.1
     */
    protected transient Boolean hasNoParameters;
    /**
     * Mark about single parameter.
     *
     * @since 0.1.1
     */
    protected transient Boolean hasOnlyOneParameter;

    /**
     * Constructs with an executable element.
     *
     * @param executableElement executable element
     * @since 0.1.0
     */
    public MethodDescriptor(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    /**
     * Creates a set from a type element.
     *
     * @param typeElement type element
     * @return set from {@code typeElement}
     * @since 0.1.0
     */
    public static Set<MethodDescriptor> of(TypeElement typeElement) {
        MethodCollector methodCollector = new MethodCollector(typeElement);
        return methodCollector.getChildren().stream()
                .map(MethodDescriptor::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a name.
     *
     * @return name
     * @since 0.1.0
     */
    public String getName() {
        if (name == null) {
            name = executableElement.getSimpleName().toString();
        }
        return name;
    }

    /**
     * Returns a type name.
     *
     * @return type name
     * @since 0.1.1
     */
    public String getTypeName() {
        if (typeName == null) {
            typeName = executableElement.getReturnType().toString();
        }
        return typeName;
    }

    /**
     * Checks if method is private.
     *
     * @return if method is private
     * @since 0.1.1
     */
    public boolean isPrivate() {
        if (isPrivate == null) {
            isPrivate = executableElement.getModifiers().stream().anyMatch(Modifier.PRIVATE::equals);
        }
        return isPrivate;
    }

    /**
     * Checks if method has no parameters.
     *
     * @return if method has no parameters
     * @since 0.1.1
     */
    public boolean hasNoParameters() {
        if (hasNoParameters == null) {
            hasNoParameters = executableElement.getParameters().isEmpty();
        }
        return hasNoParameters;
    }

    /**
     * Checks if method has only one parameter.
     *
     * @param parameterTypeName parameter type name
     * @return if method has only one parameter
     */
    public boolean hasOnlyOneParameter(String parameterTypeName) {
        if (hasOnlyOneParameter == null) {
            List<? extends VariableElement> parameters = executableElement.getParameters();
            hasOnlyOneParameter = parameters.size() == 1
                    && parameterTypeName.equals(parameters.get(0).asType().toString());
        }
        return hasOnlyOneParameter;
    }
}
