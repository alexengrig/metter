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

package dev.alexengrig.metter.processor;

import dev.alexengrig.metter.ElementMocks;
import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetterSupplierProcessorTest {
    static final GetterSupplierProcessor PROCESSOR = new GetterSupplierProcessor();

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
        assertEquals("MyCustomClassName", PROCESSOR.getCustomClassName(typeDescriptor),
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
        assertEquals(expected, PROCESSOR.getIncludedFields(typeDescriptor),
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
        assertEquals(expected, PROCESSOR.getExcludedFields(typeDescriptor),
                "Excluded fields not equal to 'excludedField1' and 'excludedField2'");
    }

    @Test
    void should_return_getterMethod() {
        VariableElement booleanField = ElementMocks.fieldMock("booleanField", boolean.class);
        FieldDescriptor booleanFieldDescriptor = new FieldDescriptor(booleanField);
        assertEquals("isBooleanField", PROCESSOR.getGetterMethodName(booleanFieldDescriptor),
                "Method name does not equal to 'isBooleanField'");
        VariableElement stringField = ElementMocks.fieldMock("stringField", String.class);
        FieldDescriptor stringFieldDescriptor = new FieldDescriptor(stringField);
        assertEquals("getStringField", PROCESSOR.getGetterMethodName(stringFieldDescriptor),
                "Method name does not equal to 'getStringField'");
    }

    @Test
    void should_return_method() {
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.getQualifiedName()).thenReturn("java.lang.String");
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        String method = PROCESSOR.getMethod(fieldDescriptor);

        assertEquals("java.lang.String::isField", method, "Method is incorrect");
    }

    @Test
    void should_check_hasGetterMethod() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("boolean");
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("isField")).thenReturn(true);
        when(typeDescriptor.getMethods("isField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasGetterMethod = PROCESSOR.hasGetterMethod(fieldDescriptor);

        assertTrue(hasGetterMethod, "Class has no getter-method");
    }

    @Test
    void should_check_hasGetterMethod_without_getter() {
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("isField")).thenReturn(false);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasGetterMethod = PROCESSOR.hasGetterMethod(fieldDescriptor);

        assertFalse(hasGetterMethod, "Class has getter-method");
    }

    @Test
    void should_check_hasGetterMethod_with_privateGetter() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(false);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("isField")).thenReturn(true);
        when(typeDescriptor.getMethods("isField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasGetterMethod = PROCESSOR.hasGetterMethod(fieldDescriptor);

        assertFalse(hasGetterMethod, "Class has not-private getter-method");
    }

    @Test
    void should_check_hasGetterMethod_with_parametrizedGetter() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(false);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("isField")).thenReturn(true);
        when(typeDescriptor.getMethods("isField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasGetterMethod = PROCESSOR.hasGetterMethod(fieldDescriptor);

        assertFalse(hasGetterMethod, "Class has parametrized getter-method");
    }

    @Test
    void should_check_hasGetterMethod_for_getter_with_differentReturnType() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("int");
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("isField")).thenReturn(true);
        when(typeDescriptor.getMethods("isField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasGetterMethod = PROCESSOR.hasGetterMethod(fieldDescriptor);

        assertFalse(hasGetterMethod, "Class has getter-method with boolean return type");
    }

    /*@Test
    void should_check_isTargetField_for_notPrivateLombokGetterOnField() {
        Getter getter = mock(Getter.class);
        when(getter.value()).thenReturn(AccessLevel.PUBLIC);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(true);
        when(fieldDescriptor.getAnnotation(Getter.class)).thenReturn(Optional.of(getter));

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Field does not have not-private Lombok Getter annotation");
    }

    @Test
    void should_check_isTargetField_for_privateLombokGetterOnField() {
        Getter getter = mock(Getter.class);
        when(getter.value()).thenReturn(AccessLevel.PRIVATE);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Getter.class)).thenReturn(true);
        when(fieldDescriptor.getAnnotation(Getter.class)).thenReturn(Optional.of(getter));

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

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

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

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

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

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

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

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

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have getter-method");
    }

    @Test
    void should_check_isTargetField_with_differentTypeOfGetter() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("int");
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

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertFalse(isTargetField, "Class have getter-method with int return type");
    }*/
}