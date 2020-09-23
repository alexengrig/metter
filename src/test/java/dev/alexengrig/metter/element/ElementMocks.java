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

package dev.alexengrig.metter.element;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ElementMocks {
    public static ExecutableElement executableElementMock() {
        ExecutableElement mock = mock(ExecutableElement.class);
        when(mock.accept(any(), any()))
                .then(invocationOnMock -> {
                    ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
                    Object parameter = invocationOnMock.getArgument(1);
                    return visitor.visitExecutable(mock, parameter);
                });
        return mock;
    }

    public static VariableElement variableElementMock() {
        VariableElement mock = mock(VariableElement.class);
        when(mock.accept(any(), any()))
                .then(invocationOnMock -> {
                    ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
                    Object parameter = invocationOnMock.getArgument(1);
                    return visitor.visitVariable(mock, parameter);
                });
        return mock;
    }

    public static TypeElement typeElement(List<Element> enclosedElements) {
        TypeElement mock = mock(TypeElement.class);
        when(mock.accept(any(), any()))
                .then(invocationOnMock -> {
                    ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
                    Object parameter = invocationOnMock.getArgument(1);
                    return visitor.visitType(mock, parameter);
                });
        //noinspection unchecked
        when(((List<Element>) mock.getEnclosedElements()))
                .thenReturn(enclosedElements);
        return mock;
    }
}
