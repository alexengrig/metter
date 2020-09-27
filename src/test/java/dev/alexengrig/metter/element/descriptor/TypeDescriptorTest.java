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

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.alexengrig.metter.element.ElementMocks.executableElementMock;
import static dev.alexengrig.metter.element.ElementMocks.typeElementMock;
import static dev.alexengrig.metter.element.ElementMocks.variableElementMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

class TypeDescriptorTest {
    @Test
    void should_return_qualifiedName() {
        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals("java.lang.String", descriptor.getQualifiedName(),
                "Qualified name is not equal to 'java.lang.String'");
        descriptor.getQualifiedName();
        verify(typeElement).getQualifiedName();
    }

    @Test
    void should_return_simpleName() {
        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertEquals("String", descriptor.getSimpleName(), "Simple name is not equal to 'String'");
        descriptor.getSimpleName();
        verify(typeElement).getSimpleName();
    }

    @Test
    void should_return_fields() {
        VariableElement field1 = variableElementMock("field1");
        VariableElement field2 = variableElementMock("field2");
        ExecutableElement method = executableElementMock();
        TypeElement typeElement = typeElementMock(Arrays.asList(field1, field2, method));
        Set<String> expected = new HashSet<>(Arrays.asList("field1", "field2"));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        Set<String> actual = descriptor.getFields().stream()
                .map(FieldDescriptor::getName)
                .collect(Collectors.toSet());
        assertEquals(expected, actual, "Fields of class are not 'field1' and 'field2'");
        descriptor.getFields();
        verify(typeElement).getEnclosedElements();
    }

    @Test
    void should_return_methods() {
        ExecutableElement method1 = executableElementMock("method1");
        ExecutableElement method2 = executableElementMock("method2");
        VariableElement field = variableElementMock();
        TypeElement typeElement = typeElementMock(Arrays.asList(method1, method2, field));
        Set<String> expected = new HashSet<>(Arrays.asList("method1", "method2"));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        Set<String> actual = descriptor.getMethods().stream()
                .map(MethodDescriptor::getName)
                .collect(Collectors.toSet());
        assertEquals(expected, actual, "Methods of class are not 'method1' and 'method2'");
        descriptor.getMethods();
        verify(typeElement).getEnclosedElements();
    }

    @Test
    void should_check_hasMethod() {
        ExecutableElement method1 = executableElementMock("getOne");
        ExecutableElement method2 = executableElementMock("getTwo");
        TypeElement typeElement = typeElementMock(Arrays.asList(method1, method2));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertTrue(descriptor.hasMethod("getOne"), "Class has no 'getOne' method");
        assertTrue(descriptor.hasMethod("getTwo"), "Class has no 'getTwo' method");
        assertFalse(descriptor.hasMethod("getThree"), "Class has 'getThree' method");
        verify(typeElement).getEnclosedElements();
    }

    @Test
    void should_return_annotations() {
        TypeElement typeElement = ElementMocks.typeElementMock(Deprecated.class, SuppressWarnings.class);
        HashSet<String> expected = new HashSet<>(Arrays.asList("java.lang.Deprecated", "java.lang.SuppressWarnings"));
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        Set<String> actual = descriptor.getAnnotations().stream()
                .map(AnnotationDescriptor::getQualifiedName)
                .collect(Collectors.toSet());
        assertEquals(expected, actual,
                "Annotations of class are not 'java.lang.Deprecated' and 'java.lang.SuppressWarnings'");
        descriptor.getAnnotations();
        verify(typeElement).getAnnotationMirrors();
    }

    @Test
    void should_check_hasAnnotation() {
        TypeElement typeElement = ElementMocks.typeElementMock(Deprecated.class, SuppressWarnings.class);
        TypeDescriptor descriptor = new TypeDescriptor(typeElement);
        assertTrue(descriptor.hasAnnotation("java.lang.Deprecated"),
                "Class has no 'java.lang.Deprecated' annotation");
        assertTrue(descriptor.hasAnnotation("java.lang.SuppressWarnings"),
                "Class has no 'java.lang.SuppressWarnings' annotation");
        assertFalse(descriptor.hasAnnotation("java.lang.Override"),
                "Class has 'java.lang.Override' annotation");
        verify(typeElement).getAnnotationMirrors();
    }
}