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

package dev.alexengrig.metter.processor;

import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.element.ElementMocks;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
                "Custom class name is not equal to 'MyCustomClassName'");
    }

    @Test
    void should_return_includedFields() {
        TypeElement typeElement = mock(TypeElement.class);
        GetterSupplier annotation = new GetterSupplier() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String value() {
                return null;
            }

            @Override
            public String[] includedFields() {
                return new String[]{"includedField1", "includedField2"};
            }

            @Override
            public String[] excludedFields() {
                return new String[0];
            }
        };
        when(typeElement.getAnnotation(GetterSupplier.class)).thenReturn(annotation);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        HashSet<String> expected = new HashSet<>(Arrays.asList("includedField1", "includedField2"));
        assertEquals(expected, processor.getIncludedFields(typeDescriptor),
                "Included fields are not equal to 'includedField1' and 'includedField2'");
    }

    @Test
    void should_return_excludedFields() {
        TypeElement typeElement = mock(TypeElement.class);
        GetterSupplier annotation = new GetterSupplier() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String value() {
                return null;
            }

            @Override
            public String[] includedFields() {
                return new String[0];
            }

            @Override
            public String[] excludedFields() {
                return new String[]{"excludedField1", "excludedField2"};
            }
        };
        when(typeElement.getAnnotation(GetterSupplier.class)).thenReturn(annotation);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        HashSet<String> expected = new HashSet<>(Arrays.asList("excludedField1", "excludedField2"));
        assertEquals(expected, processor.getExcludedFields(typeDescriptor),
                "Excluded fields are not equal to 'excludedField1' and 'excludedField2'");
    }

    @Test
    void should_check_hasAllMethods_without_annotations() {
        TypeElement typeElement = mock(TypeElement.class);
        when(typeElement.getAnnotationMirrors()).thenReturn(Collections.emptyList());
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertFalse(processor.hasAllMethods(typeDescriptor), "Class has lombok annotations");
    }

    @Test
    void should_check_hasAllMethods_with_Data_annotation() {
        TypeElement typeElement = mock(TypeElement.class);
        AnnotationMirror data = ElementMocks.annotationMirrorMock(Data.class);
        Mockito.<List<? extends AnnotationMirror>>when(typeElement.getAnnotationMirrors())
                .thenReturn(Collections.singletonList(data));
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertTrue(processor.hasAllMethods(typeDescriptor), "Class has no Data annotation of lombok");
    }

    @Test
    void should_check_hasAllMethods_with_Getter_annotation() {
        TypeElement typeElement = mock(TypeElement.class);
        AnnotationMirror getter = ElementMocks.annotationMirrorMock(Getter.class);
        Mockito.<List<? extends AnnotationMirror>>when(typeElement.getAnnotationMirrors())
                .thenReturn(Collections.singletonList(getter));
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertTrue(processor.hasAllMethods(typeDescriptor), "Class has no Getter annotation of lombok");
    }

    @Test
    void should_check_hasAllMethods_with_Data_and_Getter_annotations() {
        TypeElement typeElement = mock(TypeElement.class);
        AnnotationMirror data = ElementMocks.annotationMirrorMock(Data.class);
        AnnotationMirror getter = ElementMocks.annotationMirrorMock(Getter.class);
        Mockito.<List<? extends AnnotationMirror>>when(typeElement.getAnnotationMirrors())
                .thenReturn(Arrays.asList(data, getter));
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertTrue(processor.hasAllMethods(typeDescriptor), "Class has no Data/Getter annotations of lombok");
    }

    @Test
    void should_return_methodName() {
        VariableElement booleanField = ElementMocks.variableElementMock("booleanField", boolean.class);
        FieldDescriptor booleanFieldDescriptor = new FieldDescriptor(booleanField);
        assertEquals("isBooleanField", processor.getMethodName(booleanFieldDescriptor),
                "Method name is not equal to 'isBooleanField'");
        VariableElement stringField = ElementMocks.variableElementMock("stringField", String.class);
        FieldDescriptor stringFieldDescriptor = new FieldDescriptor(stringField);
        assertEquals("getStringField", processor.getMethodName(stringFieldDescriptor),
                "Method name is not equal to 'getStringField'");
    }

    @Test
    void should_check_isTargetField_with_Getter_annotation() {
        VariableElement variableElement = mock(VariableElement.class);
        AnnotationMirror getter = ElementMocks.annotationMirrorMock(Getter.class);
        Mockito.<List<? extends AnnotationMirror>>when(variableElement.getAnnotationMirrors())
                .thenReturn(Collections.singletonList(getter));
        FieldDescriptor fieldDescriptor = new FieldDescriptor(variableElement);
        assertTrue(processor.isTargetField(fieldDescriptor),
                "Field with lombok Getter annotation must be target");
    }

    @Test
    void should_check_isTargetField_without_Getter_annotation() {
        VariableElement variableElement = mock(VariableElement.class);
        Mockito.<List<? extends AnnotationMirror>>when(variableElement.getAnnotationMirrors())
                .thenReturn(Collections.emptyList());
        FieldDescriptor fieldDescriptor = new FieldDescriptor(variableElement);
        assertFalse(processor.isTargetField(fieldDescriptor),
                "Field without lombok Getter annotation must not be target");
    }

    @Test
    void should_return_methodView() {
        TypeElement typeElement = mock(TypeElement.class);
        Name name = ElementMocks.nameMock("my.MyDomain");
        when(typeElement.getQualifiedName()).thenReturn(name);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertEquals("my.MyDomain::getName", processor.getMethodView(typeDescriptor, null, "getName"),
                "Method view is not equal 'my.MyDomain::getName'");
    }
}