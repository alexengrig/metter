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

import javax.lang.model.element.VariableElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VariableNamesCollectorElementVisitor extends BaseElementVisitor {
    private final Map<String, String> variable2Type;

    public VariableNamesCollectorElementVisitor() {
        this.variable2Type = new HashMap<>();
    }

    @Override
    public void visitVariable(VariableElement variableElement) {
        String name = variableElement.getSimpleName().toString();
        this.variable2Type.put(name, variableElement.asType().toString());
    }

    public Map<String, String> getVariable2Type() {
        return variable2Type;
    }

    public Set<String> getVariableNames() {
        return variable2Type.keySet();
    }
}
