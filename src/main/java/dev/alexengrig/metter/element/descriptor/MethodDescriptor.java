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
 * @since 0.1.1
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
     * Mark about not private modifier.
     *
     * @since 0.1.1
     */
    protected transient Boolean isNotPrivate;
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
        return executableElement.getSimpleName().toString();
    }

    /**
     * Returns a type name.
     *
     * @return type name
     * @since 0.1.1
     */
    public String getTypeName() {
        return executableElement.getReturnType().toString();
    }

    /**
     * Checks if method is private.
     *
     * @return if method is private
     * @since 0.1.1
     */
    public boolean isNotPrivate() {
        if (isNotPrivate == null) {
            isNotPrivate = executableElement.getModifiers().stream().noneMatch(Modifier.PRIVATE::equals);
        }
        return isNotPrivate;
    }

    /**
     * Checks if method has no parameters.
     *
     * @return if method has no parameters
     * @since 0.1.1
     */
    public boolean hasNoParameters() {
        return executableElement.getParameters().isEmpty();
    }

    /**
     * Checks if method has only one parameter.
     *
     * @param parameterTypeName parameter type name
     * @return if method has only one parameter
     * @since 0.1.1
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
