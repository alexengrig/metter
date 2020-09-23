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

package dev.alexengrig.metter.element.collector;

import dev.alexengrig.metter.element.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class MethodCollectorTest {
    @Test
    void should_return_allMethods() {
        VariableElement field1 = ElementMocks.variableElementMock();
        VariableElement field2 = ElementMocks.variableElementMock();
        ExecutableElement method1 = ElementMocks.executableElementMock();
        ExecutableElement method2 = ElementMocks.executableElementMock();
        List<Element> enclosedElements = Arrays.asList(field1, method1, field2, method2);
        TypeElement type = ElementMocks.typeElement(enclosedElements);
        MethodCollector collector = new MethodCollector(type);
        assertEquals(new HashSet<>(Arrays.asList(method1, method2)), collector.getChildren(),
                "Invalid set of methods");
        collector.getChildren();
        verify(type).getEnclosedElements();
    }
}