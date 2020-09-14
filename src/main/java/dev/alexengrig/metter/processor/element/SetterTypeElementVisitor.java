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

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SetterTypeElementVisitor extends BaseElementVisitor {
    private final Map<String, String> field2Method;
    private String className;

    public SetterTypeElementVisitor() {
        this.field2Method = new HashMap<>();
    }

    @Override
    public void visitType(TypeElement typeElement) {
        this.className = typeElement.getQualifiedName().toString();
        VariableNamesCollectorElementVisitor variableVisitor = new VariableNamesCollectorElementVisitor();
        SetterMethodCollectorElementVisitor methodVisitor = new SetterMethodCollectorElementVisitor();
        for (Element element : typeElement.getEnclosedElements()) {
            element.accept(variableVisitor, null);
            element.accept(methodVisitor, null);
        }
        Map<String, String> variable2Type = variableVisitor.getVariable2Type();
        Set<String> methodNames = methodVisitor.getMethodNames();
        variable2Type.forEach((variableName, variableType) -> {
            for (String methodName : methodNames) {
                String variablePart = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
                if (methodName.startsWith("set") && ("set" + variablePart).equals(methodName)) {
                    field2Method.put(variableName,
                            String.format("(instance, value) -> instance.%s((%s) value)",
                                    methodName, variableType));
                    break;
                }
            }
        });
    }

    public Map<String, String> getMap() {
        return field2Method;
    }

    public String getClassName() {
        return className;
    }
}
