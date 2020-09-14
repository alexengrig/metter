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

package dev.alexengrig.metter.processor.element;

import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.Set;

public class SetterMethodCollectorElementVisitor extends BaseElementVisitor {
    private final Set<String> methodNames;

    public SetterMethodCollectorElementVisitor() {
        methodNames = new HashSet<>();
    }

    @Override
    public void visitExecutable(ExecutableElement executableElement) {
        if (executableElement.getParameters().size() != 1) {
            return;
        }
        String name = executableElement.getSimpleName().toString();
        if (name.startsWith("set")) {
            methodNames.add(name);
        }
    }

    public Set<String> getMethodNames() {
        return methodNames;
    }
}
