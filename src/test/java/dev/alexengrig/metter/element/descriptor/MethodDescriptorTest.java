/*
 * Copyright 2020-2021 Alexengrig Dev.
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

import dev.alexengrig.metter.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.ExecutableElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static dev.alexengrig.metter.ElementMocks.executableElementMock;
import static dev.alexengrig.metter.ElementMocks.noParametersMethodMock;
import static dev.alexengrig.metter.ElementMocks.notPrivateMethodMock;
import static dev.alexengrig.metter.ElementMocks.parameterizedMethodMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MethodDescriptorTest {
    @Test
    void should_create_set_by_type() {
        Set<MethodDescriptor> descriptors = MethodDescriptor.of(ElementMocks.typeElementMock(Arrays.asList(
                executableElementMock("method1"),
                executableElementMock("method2"),
                executableElementMock("method3"),
                ElementMocks.fieldMock("field"))));
        assertEquals(3, descriptors.size(), "Number of descriptors is incorrect");
    }

    @Test
    void should_return_name() {
        ExecutableElement executableElement = executableElementMock("getText");
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        assertEquals("getText", descriptor.getName(), "Method name is incorrect");
    }

    @Test
    void should_return_typeName() {
        ExecutableElement executableElement = executableElementMock(String.class);
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        assertEquals(String.class.getName(), descriptor.getTypeName(), "Method type name is incorrect");
    }

    @Test
    void should_check_isNotPrivate() {
        ExecutableElement executableElement = notPrivateMethodMock();
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        assertTrue(descriptor.isNotPrivate(), "Method is private");
        descriptor.isNotPrivate();
        verify(executableElement).getModifiers();
    }

    @Test
    void should_check_hasNoParameters() {
        ExecutableElement executableElement = noParametersMethodMock();
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        assertTrue(descriptor.hasNoParameters(), "Method has parameters");
    }

    @Test
    void should_check_hasOnlyOneParameter() {
        ExecutableElement executableElement = parameterizedMethodMock(String.class);
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        boolean hasOnlyOneParameter = descriptor.hasOnlyOneParameter(String.class.getName());
        assertTrue(hasOnlyOneParameter, () -> "Method has no only one parameter: " + String.class.getName());
    }

    @Test
    void should_check_hasOnlyOneParameter_without_parameters() {
        ExecutableElement executableElement = mock(ExecutableElement.class);
        when(executableElement.getParameters()).thenReturn(Collections.emptyList());
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        boolean hasOnlyOneParameter = descriptor.hasOnlyOneParameter(String.class.getName());
        assertFalse(hasOnlyOneParameter, () -> "Method has only one parameter: " + String.class.getName());
    }

    @Test
    void should_check_hasOnlyOneParameter_with_differentParameter() {
        ExecutableElement executableElement = parameterizedMethodMock(Integer.class);
        MethodDescriptor descriptor = new MethodDescriptor(executableElement);
        boolean hasOnlyOneParameter = descriptor.hasOnlyOneParameter(String.class.getName());
        assertFalse(hasOnlyOneParameter, () -> "Method has only one parameter: " + Integer.class.getName());
    }
}