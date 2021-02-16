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

import org.junit.jupiter.api.Test;

import javax.lang.model.element.TypeElement;
import java.util.Arrays;

import static dev.alexengrig.metter.ElementMocks.executableElementMock;
import static dev.alexengrig.metter.ElementMocks.fieldMock;
import static dev.alexengrig.metter.ElementMocks.typeElementMock;
import static dev.alexengrig.metter.ElementMocks.variableElementMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

class TypeDescriptorTest {
    @Test
    void should_return_qualifiedName() {
        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals("java.lang.String", descriptor.getQualifiedName(), "Qualified name is incorrect");
    }

    @Test
    void should_return_simpleName() {
        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals("String", descriptor.getSimpleName(), "Simple name is incorrect");
    }

    @Test
    void should_return_fields() {
        TypeElement typeElement = typeElementMock(Arrays.asList(
                fieldMock("field1"),
                fieldMock("field2"),
                executableElementMock("method")));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals(2, descriptor.getFields().size(), "Number of type fields is incorrect");
        descriptor.getFields();
        verify(typeElement).getEnclosedElements();
    }

    @Test
    void should_return_methods() {
        TypeElement typeElement = typeElementMock(Arrays.asList(
                executableElementMock("method1"),
                executableElementMock("method2"),
                variableElementMock("field")));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals(2, descriptor.getMethods().size(), "Number of type methods is incorrect");
        descriptor.getMethods();
        verify(typeElement).getEnclosedElements();
    }

    @Test
    void should_return_methods_by_name() {
        TypeElement typeElement = typeElementMock(Arrays.asList(
                executableElementMock("method1"),
                executableElementMock("method1"),
                executableElementMock("method2"),
                variableElementMock("field")));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals(2, descriptor.getMethods("method1").size(), "Number of type methods 'method1' is incorrect");
        descriptor.getMethods();
        verify(typeElement).getEnclosedElements();
    }

    @Test
    void should_check_hasMethod() {
        TypeElement typeElement = typeElementMock(Arrays.asList(
                executableElementMock("getOne"),
                executableElementMock("getTwo")));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertTrue(descriptor.hasMethod("getOne"), "Class has no 'getOne' method");
        assertTrue(descriptor.hasMethod("getOne"), "Second time: Class has no 'getOne' method");
        assertTrue(descriptor.hasMethod("getTwo"), "Class has no 'getTwo' method");
        assertFalse(descriptor.hasMethod("getThree"), "Class has 'getThree' method");
        verify(typeElement).getEnclosedElements();
    }
}