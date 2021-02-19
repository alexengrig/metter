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

import dev.alexengrig.metter.annotation.SetterSupplier;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        String setterMethod = PROCESSOR.getSetterMethod(fieldDescriptor);

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

        assertTrue(hasSetterMethod, "Has no setter method");
    }
}