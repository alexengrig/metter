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
import javax.lang.model.element.TypeElement;
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
}
