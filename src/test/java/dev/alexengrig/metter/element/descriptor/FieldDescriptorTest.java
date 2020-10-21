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

import dev.alexengrig.metter.element.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FieldDescriptorTest {
    @Test
    void should_return_name() {
        String fieldName = "someField";
        VariableElement variableElement = ElementMocks.fieldElementMock(fieldName);
        FieldDescriptor descriptor = new FieldDescriptor(variableElement);
        assertEquals(fieldName, descriptor.getName(), "Field name does not equal to 'someField'");
        descriptor.getName();
        verify(variableElement).getSimpleName();
    }

    @Test
    void should_return_type() {
        VariableElement variableElement = ElementMocks.fieldElementMock(String.class);
        FieldDescriptor descriptor = new FieldDescriptor(variableElement);
        assertEquals("java.lang.String", descriptor.getTypeName(), "Field type does not equal to 'java.lang.String'");
        descriptor.getTypeName();
        verify(variableElement).asType();
    }

    @Test
    void should_return_annotations() {
        VariableElement variableElement = ElementMocks.fieldElementMock(Deprecated.class, SuppressWarnings.class);
        FieldDescriptor descriptor = new FieldDescriptor(variableElement);
        List<String> expected = Arrays.asList("java.lang.Deprecated", "java.lang.SuppressWarnings");
        List<String> actual = descriptor.getAnnotations().stream()
                .map(AnnotationDescriptor::getQualifiedName)
                .collect(Collectors.toList());
        assertEquals(new HashSet<>(expected), new HashSet<>(actual),
                "Annotations of field are not 'java.lang.Deprecated' and 'java.lang.SuppressWarnings'");
        descriptor.getAnnotations();
        verify(variableElement).getAnnotationMirrors();
    }

    @Test
    void should_check_hasAnnotation() {
        VariableElement variableElement = ElementMocks.fieldElementMock(Deprecated.class, SuppressWarnings.class);
        FieldDescriptor descriptor = new FieldDescriptor(variableElement);
        assertTrue(descriptor.hasAnnotation(Deprecated.class),
                "Field has no 'java.lang.Deprecated' annotation");
        assertTrue(descriptor.hasAnnotation(Deprecated.class),
                "Second time: Field has no 'java.lang.Deprecated' annotation");
        assertTrue(descriptor.hasAnnotation(SuppressWarnings.class),
                "Field has no 'java.lang.SuppressWarnings' annotation");
        verify(variableElement, times(3)).getAnnotation(any());
    }

    @Test
    void should_return_annotation() {
        VariableElement variableElement = ElementMocks.fieldElementMock(Deprecated.class, SuppressWarnings.class);
        FieldDescriptor descriptor = new FieldDescriptor(variableElement);
        assertNotNull(descriptor.getAnnotation(Deprecated.class),
                "Field has no 'java.lang.Deprecated' annotation");
        assertNotNull(descriptor.getAnnotation(SuppressWarnings.class),
                "Field has no 'java.lang.SuppressWarnings' annotation");
        assertNull(descriptor.getAnnotation(Override.class),
                "Field has no 'java.lang.Override' annotation");
    }
}