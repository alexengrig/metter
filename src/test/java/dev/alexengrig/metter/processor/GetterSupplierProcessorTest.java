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

package dev.alexengrig.metter.processor;

import dev.alexengrig.metter.ElementMocks;
import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetterSupplierProcessorTest {
    static final GetterSupplierProcessor processor;
    static final ProcessingEnvironment environment;

    static {
        processor = new GetterSupplierProcessor();
        processor.init(environment = mock(ProcessingEnvironment.class));
    }

    @Test
    void should_return_customClassName() {
        TypeElement typeElement = mock(TypeElement.class);
        GetterSupplier annotation = new GetterSupplier() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String value() {
                return "MyCustomClassName";
            }

            @Override
            public String[] includedFields() {
                return new String[0];
            }

            @Override
            public String[] excludedFields() {
                return new String[0];
            }
        };
        when(typeElement.getAnnotation(GetterSupplier.class)).thenReturn(annotation);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertEquals("MyCustomClassName", processor.getCustomClassName(typeDescriptor),
                "Custom class name does not equal to 'MyCustomClassName'");
    }

    @Test
    void should_return_includedFields() {
        GetterSupplier annotation = mock(GetterSupplier.class);
        when(annotation.includedFields()).thenReturn(new String[]{"includedField1", "includedField2"});
        when(annotation.excludedFields()).thenReturn(new String[0]);
        TypeElement typeElement = mock(TypeElement.class);
        when(typeElement.getAnnotation(GetterSupplier.class)).thenReturn(annotation);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        HashSet<String> expected = new HashSet<>(Arrays.asList("includedField1", "includedField2"));
        assertEquals(expected, processor.getIncludedFields(typeDescriptor),
                "Included fields not equal to 'includedField1' and 'includedField2'");
    }

    @Test
    void should_return_excludedFields() {
        GetterSupplier annotation = mock(GetterSupplier.class);
        when(annotation.excludedFields()).thenReturn(new String[]{"excludedField1", "excludedField2"});
        when(annotation.includedFields()).thenReturn(new String[0]);
        TypeElement typeElement = mock(TypeElement.class);
        when(typeElement.getAnnotation(GetterSupplier.class)).thenReturn(annotation);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        HashSet<String> expected = new HashSet<>(Arrays.asList("excludedField1", "excludedField2"));
        assertEquals(expected, processor.getExcludedFields(typeDescriptor),
                "Excluded fields not equal to 'excludedField1' and 'excludedField2'");
    }

    @Test
    void should_return_methodName() {
        VariableElement booleanField = ElementMocks.fieldMock("booleanField", boolean.class);
        FieldDescriptor booleanFieldDescriptor = new FieldDescriptor(booleanField);
        assertEquals("isBooleanField", processor.getGetterMethod(booleanFieldDescriptor),
                "Method name does not equal to 'isBooleanField'");
        VariableElement stringField = ElementMocks.fieldMock("stringField", String.class);
        FieldDescriptor stringFieldDescriptor = new FieldDescriptor(stringField);
        assertEquals("getStringField", processor.getGetterMethod(stringFieldDescriptor),
                "Method name does not equal to 'getStringField'");
    }

    @Test
    void should_check_isTargetField_for_notPrivateLombokGetterOnField() {
        Getter getter = mock(Getter.class);
        when(getter.value()).thenReturn(AccessLevel.PUBLIC);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(true);
        when(fieldDescriptor.getAnnotation(Getter.class)).thenReturn(Optional.of(getter));

        boolean isTargetField = processor.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Field does not have not-private Lombok Getter annotation");
    }

    @Test
    void should_check_isTargetField_for_privateLombokGetterOnField() {
        Getter getter = mock(Getter.class);
        when(getter.value()).thenReturn(AccessLevel.PRIVATE);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(true);
        when(fieldDescriptor.getAnnotation(Getter.class)).thenReturn(Optional.of(getter));

        boolean isTargetField = processor.isTargetField(fieldDescriptor);

        assertFalse(isTargetField, "Field does not have private Lombok Getter annotation");
    }

    @Test
    void should_check_isTargetField_for_notPrivateLombokGetterOnClass() {
        Getter getter = mock(Getter.class);
        when(getter.value()).thenReturn(AccessLevel.PUBLIC);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Getter.class)).thenReturn(true);
        when(typeDescriptor.getAnnotation(Getter.class)).thenReturn(Optional.of(getter));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean isTargetField = processor.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have not-private Lombok Getter annotation");
    }

    @Test
    void should_check_isTargetField_for_privateLombokGetterOnClass() {
        Getter getter = mock(Getter.class);
        when(getter.value()).thenReturn(AccessLevel.PRIVATE);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Getter.class)).thenReturn(true);
        when(typeDescriptor.getAnnotation(Getter.class)).thenReturn(Optional.of(getter));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean isTargetField = processor.isTargetField(fieldDescriptor);

        assertFalse(isTargetField, "Class does not have private Lombok Getter annotation");
    }

    @Test
    void should_check_isTargetField_for_lombokDataOnClass() {
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Getter.class)).thenReturn(false);
        when(typeDescriptor.hasAnnotation(Data.class)).thenReturn(true);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean isTargetField = processor.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have Lombok Data annotation");
    }

    @Test
    void should_check_isTargetField_for_getterMethod() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("boolean");
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Getter.class)).thenReturn(false);
        when(typeDescriptor.hasAnnotation(Data.class)).thenReturn(false);
        when(typeDescriptor.hasMethod("isTest")).thenReturn(true);
        when(typeDescriptor.getMethods("isTest")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);
        when(fieldDescriptor.getName()).thenReturn("test");
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");

        boolean isTargetField = processor.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have getter-method");
    }
}