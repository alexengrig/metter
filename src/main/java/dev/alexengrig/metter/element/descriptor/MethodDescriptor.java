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

import dev.alexengrig.metter.element.collector.ExecutableElementCollector;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodDescriptor {
    protected final ExecutableElement executableElement;
    protected transient String name;

    public MethodDescriptor(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    public static Set<MethodDescriptor> of(TypeElement typeElement) {
        ExecutableElementCollector methodCollector = new ExecutableElementCollector(typeElement);
        return methodCollector.getExecutableElements().stream().map(MethodDescriptor::new).collect(Collectors.toSet());
    }

    public String getName() {
        if (name == null) {
            name = executableElement.getSimpleName().toString();
        }
        return name;
    }
}
