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

import dev.alexengrig.metter.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FieldDescriptorTest {
    @Test
    void should_return_parent() {
        VariableElement field = ElementMocks.fieldMock();
        TypeElement type = ElementMocks.typeElementMock();
        when(field.getEnclosingElement()).thenReturn(type);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertSame(type, descriptor.getParent().typeElement, "Type element is invalid");
    }

    @Test
    void should_return_name() {
        String fieldName = "someField";
        VariableElement field = ElementMocks.fieldMock(fieldName);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertEquals(fieldName, descriptor.getName(), "Field name does not equal to 'someField'");
        descriptor.getName();
        verify(field).getSimpleName();
    }

    @Test
    void should_return_type() {
        VariableElement field = ElementMocks.fieldMock(String.class);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertEquals("java.lang.String", descriptor.getTypeName(), "Field type does not equal to 'java.lang.String'");
        descriptor.getTypeName();
        verify(field).asType();
    }

    @Test
    void should_return_annotations() {
        VariableElement field = ElementMocks.annotatedFieldMock(Deprecated.class, SuppressWarnings.class);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        List<String> expected = Arrays.asList("java.lang.Deprecated", "java.lang.SuppressWarnings");
        List<String> actual = descriptor.getAnnotations().stream()
                .map(AnnotationDescriptor::getQualifiedName)
                .collect(Collectors.toList());
        assertEquals(new HashSet<>(expected), new HashSet<>(actual),
                "Annotations of field are not 'java.lang.Deprecated' and 'java.lang.SuppressWarnings'");
        descriptor.getAnnotations();
        verify(field).getAnnotationMirrors();
    }

    @Test
    void should_check_hasAnnotation() {
        VariableElement field = ElementMocks.annotatedFieldMock(Deprecated.class, SuppressWarnings.class);
        FieldDescriptor descriptor = new FieldDescriptor(field);
        assertTrue(descriptor.hasAnnotation("java.lang.Deprecated"),
                "Field has no 'java.lang.Deprecated' annotation");
        assertTrue(descriptor.hasAnnotation("java.lang.Deprecated"),
                "Second time: Field has no 'java.lang.Deprecated' annotation");
        assertTrue(descriptor.hasAnnotation("java.lang.SuppressWarnings"),
                "Field has no 'java.lang.SuppressWarnings' annotation");
        verify(field).getAnnotationMirrors();
    }
}