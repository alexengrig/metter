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

import dev.alexengrig.metter.annotation.SetterSupplier;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SetterSupplierProcessorTest {
    static final SetterSupplierProcessor PROCESSOR = new SetterSupplierProcessor();

    @Test
    void should_return_customClassName() {
        SetterSupplier annotation = mock(SetterSupplier.class);
        when(annotation.value()).thenReturn("MyCustomClassName");
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.getAnnotation(SetterSupplier.class)).thenReturn(Optional.of(annotation));

        String customClassName = PROCESSOR.getCustomClassName(typeDescriptor);

        assertEquals("MyCustomClassName", customClassName,
                "Custom class name is incorrect");
    }

    @Test
    void should_return_includedFields() {
        SetterSupplier annotation = mock(SetterSupplier.class);
        when(annotation.includedFields()).thenReturn(new String[]{"includedField1", "includedField2"});
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.getAnnotation(SetterSupplier.class)).thenReturn(Optional.of(annotation));

        Set<String> includedFields = PROCESSOR.getIncludedFields(typeDescriptor);

        assertEquals(new HashSet<>(Arrays.asList("includedField1", "includedField2")), includedFields,
                "Included fields are not equal to 'includedField1' and 'includedField2'");
    }

    @Test
    void should_return_excludedFields() {
        SetterSupplier annotation = mock(SetterSupplier.class);
        when(annotation.excludedFields()).thenReturn(new String[]{"excludedField1", "excludedField2"});
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.getAnnotation(SetterSupplier.class)).thenReturn(Optional.of(annotation));

        Set<String> excludedFields = PROCESSOR.getExcludedFields(typeDescriptor);

        assertEquals(new HashSet<>(Arrays.asList("excludedField1", "excludedField2")), excludedFields,
                "Excluded fields are not equal to 'excludedField1' and 'excludedField2'");
    }

    @Test
    void should_return_setterMethod() {
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");

        String setterMethod = PROCESSOR.getSetterMethodName(fieldDescriptor);

        assertEquals("setField", setterMethod, "Method name is incorrect");
    }

    @Test
    void should_return_method() {
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getTypeName()).thenReturn("java.lang.String");

        String method = PROCESSOR.getMethod(fieldDescriptor);

        assertEquals("(instance, value) -> instance.setField((java.lang.String) value)", method, "Method is incorrect");
    }

    @Test
    void should_check_hasSetterMethod() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("void");
        when(methodDescriptor.hasOnlyOneParameter("java.lang.String")).thenReturn(true);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("setField")).thenReturn(true);
        when(typeDescriptor.getMethods("setField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getTypeName()).thenReturn("java.lang.String");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasSetterMethod = PROCESSOR.hasSetterMethod(fieldDescriptor);

        assertTrue(hasSetterMethod, "Class has no setter-method");
    }

    @Test
    void should_check_hasSetterMethod_without_setterMethod() {
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("setField")).thenReturn(false);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasSetterMethod = PROCESSOR.hasSetterMethod(fieldDescriptor);

        assertFalse(hasSetterMethod, "Class has setter-method");
    }

    @Test
    void should_check_hasSetterMethod_with_privateSetter() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(false);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("setField")).thenReturn(true);
        when(typeDescriptor.getMethods("setField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getTypeName()).thenReturn("java.lang.String");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasSetterMethod = PROCESSOR.hasSetterMethod(fieldDescriptor);

        assertFalse(hasSetterMethod, "Class has not-private setter-method");
    }

    @Test
    void should_check_hasSetterMethod_for_setter_with_returnType() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("int");
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("setField")).thenReturn(true);
        when(typeDescriptor.getMethods("setField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getTypeName()).thenReturn("java.lang.String");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasSetterMethod = PROCESSOR.hasSetterMethod(fieldDescriptor);

        assertFalse(hasSetterMethod, "Class has setter-method with void return type");
    }

    @Test
    void should_check_hasSetterMethod_for_setterMethod_without_stringParameter() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("void");
        when(methodDescriptor.hasOnlyOneParameter("java.lang.String")).thenReturn(false);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasMethod("setField")).thenReturn(true);
        when(typeDescriptor.getMethods("setField")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("field");
        when(fieldDescriptor.getTypeName()).thenReturn("java.lang.String");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean hasSetterMethod = PROCESSOR.hasSetterMethod(fieldDescriptor);

        assertFalse(hasSetterMethod, "Class has setter-method with String parameter");
    }

    /*@Test
    void should_check_isTargetField_for_notPrivateLombokSetterOnField() {
        Setter setter = mock(Setter.class);
        when(setter.value()).thenReturn(AccessLevel.PUBLIC);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(true);
        when(fieldDescriptor.getAnnotation(Setter.class)).thenReturn(Optional.of(setter));

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Field does not have not-private Lombok Setter annotation");
    }

    @Test
    void should_check_isTargetField_for_privateLombokSetterOnField() {
        Setter setter = mock(Setter.class);
        when(setter.value()).thenReturn(AccessLevel.PRIVATE);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(true);
        when(fieldDescriptor.getAnnotation(Setter.class)).thenReturn(Optional.of(setter));

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertFalse(isTargetField, "Field does not have private Lombok Setter annotation");
    }

    @Test
    void should_check_isTargetField_for_notPrivateLombokSetterOnClass() {
        Setter setter = mock(Setter.class);
        when(setter.value()).thenReturn(AccessLevel.PUBLIC);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Setter.class)).thenReturn(true);
        when(typeDescriptor.getAnnotation(Setter.class)).thenReturn(Optional.of(setter));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have not-private Lombok Setter annotation");
    }

    @Test
    void should_check_isTargetField_for_privateLombokSetterOnClass() {
        Setter setter = mock(Setter.class);
        when(setter.value()).thenReturn(AccessLevel.PRIVATE);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Setter.class)).thenReturn(true);
        when(typeDescriptor.getAnnotation(Setter.class)).thenReturn(Optional.of(setter));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertFalse(isTargetField, "Class does not have private Lombok Setter annotation");
    }

    @Test
    void should_check_isTargetField_for_lombokDataOnClass() {
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(typeDescriptor.hasAnnotation(Data.class)).thenReturn(true);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have Lombok Data annotation");
    }

    @Test
    void should_check_isTargetField_for_setterMethod() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("void");
        when(methodDescriptor.hasOnlyOneParameter("boolean")).thenReturn(true);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(typeDescriptor.hasAnnotation(Data.class)).thenReturn(false);
        when(typeDescriptor.hasMethod("setTest")).thenReturn(true);
        when(typeDescriptor.getMethods("setTest")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("test");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertTrue(isTargetField, "Class does not have setter-method");
    }

    @Test
    void should_check_isTargetField_for_setterMethod_with_intParameter() {
        MethodDescriptor methodDescriptor = mock(MethodDescriptor.class);
        when(methodDescriptor.isNotPrivate()).thenReturn(true);
        when(methodDescriptor.hasNoParameters()).thenReturn(true);
        when(methodDescriptor.getTypeName()).thenReturn("void");
        when(methodDescriptor.hasOnlyOneParameter("int")).thenReturn(true);
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        when(typeDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(typeDescriptor.hasAnnotation(Data.class)).thenReturn(false);
        when(typeDescriptor.hasMethod("setTest")).thenReturn(true);
        when(typeDescriptor.getMethods("setTest")).thenReturn(Collections.singleton(methodDescriptor));
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("test");
        when(fieldDescriptor.getParent()).thenReturn(typeDescriptor);
        when(fieldDescriptor.hasAnnotation(Setter.class)).thenReturn(false);
        when(fieldDescriptor.getTypeName()).thenReturn("boolean");

        boolean isTargetField = PROCESSOR.isTargetField(fieldDescriptor);

        assertFalse(isTargetField, "Class have setter-method with boolean parameter");
    }*/
}