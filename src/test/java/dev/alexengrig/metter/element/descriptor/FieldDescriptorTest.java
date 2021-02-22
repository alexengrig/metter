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

import dev.alexengrig.metter.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FieldDescriptorTest {
    @Test
    void should_throws_exception_by_nullableElement() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new FieldDescriptor(null));
        assertEquals("Variable element must not be null", exception.getMessage(),
                "Exception message is incorrect");
    }

    @Test
    void should_throws_exception_by_invalidKind() {
        VariableElement element = mock(VariableElement.class);
        when(element.getKind()).thenReturn(ElementKind.LOCAL_VARIABLE);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new FieldDescriptor(element));
        assertEquals("Variable element must be field kind", exception.getMessage(),
                "Exception message is incorrect");
    }

    @Test
    void should_create_set_by_type() {
        Set<FieldDescriptor> descriptors = FieldDescriptor.of(ElementMocks.typeElementMock(Arrays.asList(
                ElementMocks.fieldMock("field1"),
                ElementMocks.fieldMock("field2"),
                ElementMocks.fieldMock("field3"),
                ElementMocks.executableElementMock("method"))));
        assertEquals(3, descriptors.size(), "Number of descriptors is incorrect");
    }

    @Test
    void should_return_parent() {
        VariableElement field = ElementMocks.fieldMock();
        TypeElement type = ElementMocks.typeElementMock();
        when(field.getEnclosingElement()).thenReturn(type);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertSame(type, descriptor.getParent().element, "Type element is invalid");
        descriptor.getParent();
        verify(field).getEnclosingElement();
    }

    @Test
    void should_return_name() {
        String fieldName = "someField";
        VariableElement field = ElementMocks.fieldMock(fieldName);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertEquals(fieldName, descriptor.getName(), "Field name does not equal to 'someField'");
    }

    @Test
    void should_return_type() {
        VariableElement field = ElementMocks.fieldMock(String.class);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertEquals("java.lang.String", descriptor.getTypeName(), "Field type does not equal to 'java.lang.String'");
    }

    @Test
    void should_check_hasAnnotation() {
        VariableElement field = ElementMocks.annotatedFieldMock(Deprecated.class, SuppressWarnings.class);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertTrue(descriptor.hasAnnotation(Deprecated.class),
                "Field has no 'java.lang.Deprecated' annotation");
        assertTrue(descriptor.hasAnnotation(SuppressWarnings.class),
                "Field has no 'java.lang.SuppressWarnings' annotation");
    }
}