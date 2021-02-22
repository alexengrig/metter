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

import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static dev.alexengrig.metter.ElementMocks.fieldMock;
import static dev.alexengrig.metter.ElementMocks.typeElementMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

class BaseMethodSupplierProcessorTest {
    static BaseMethodSupplierProcessor<Deprecated> getMock() {
        @SuppressWarnings("unchecked")
        BaseMethodSupplierProcessor<Deprecated> mock = mock(BaseMethodSupplierProcessor.class, withSettings()
                .useConstructor(Deprecated.class)
                .defaultAnswer(CALLS_REAL_METHODS));
        return mock;
    }

    static BaseMethodSupplierProcessor<Deprecated> getMock(MethodSupplierSourceGenerator generator) {
        class Child extends BaseMethodSupplierProcessor<Deprecated> {
            public Child() {
                super(Deprecated.class);
            }

            @Override
            protected MethodSupplierSourceGenerator getSourceGenerator() {
                return generator;
            }

            @Override
            protected String getCustomClassName(TypeDescriptor type) {
                return null;
            }

            @Override
            protected Set<String> getIncludedFields(TypeDescriptor type) {
                return null;
            }

            @Override
            protected Set<String> getExcludedFields(TypeDescriptor type) {
                return null;
            }

            @Override
            protected boolean isTargetField(FieldDescriptor field) {
                return false;
            }

            @Override
            protected String getMethod(FieldDescriptor field) {
                return null;
            }
        }
        return new Child();
    }

    @Test
    void should_create_customClassName() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        when(processor.getCustomClassName(any())).thenReturn("CustomClassName");

        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        Optional<String> className = processor.getClassName(typeDescriptor);
        assertTrue(className.isPresent(), "Class name created");
        assertEquals("java.lang.CustomClassName", className.get(), "Class name is incorrect");
    }

    @Test
    void should_create_defaultClassName() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertEquals("java.lang.StringDeprecated", processor.getDefaultClassName(typeDescriptor),
                "Default class name is incorrect");
    }

    @Test
    void should_create_sourceClassName() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        when(processor.getCustomClassName(any())).thenReturn("");

        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        String className = processor.createSourceClassName(typeDescriptor);
        assertEquals("java.lang.StringDeprecated", className, "Source class name is incorrect");
    }

    @Test
    void should_create_sourceFile() throws IOException {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        JavaFileObject expectedSourceFile = mock(JavaFileObject.class);
        Filer filer = mock(Filer.class);
        when(filer.createSourceFile(any())).thenReturn(expectedSourceFile);
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getFiler()).thenReturn(filer);

        processor.init(environment);
        JavaFileObject sourceFile = processor.createSourceFile("MySource");

        assertSame(expectedSourceFile, sourceFile);
        verify(filer).createSourceFile("MySource");
    }

    @Test
    void should_throws_exception_while_create_sourceFile() throws IOException {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        Filer filer = mock(Filer.class);
        when(filer.createSourceFile(any())).thenThrow(IOException.class);
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getFiler()).thenReturn(filer);

        processor.init(environment);
        MetterException exception = assertThrows(MetterException.class, () -> processor.createSourceFile("MySource"));

        assertEquals("Exception of source file creation for: MySource", exception.getMessage(),
                "Exception message is incorrect");
        assertEquals(IOException.class, exception.getCause().getClass(), "Exception cause class is incorrect");
    }

    @Test
    void should_write_sourceFile() throws IOException {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        StringWriter writer = new StringWriter();
        JavaFileObject file = mock(JavaFileObject.class);
        when(file.openWriter()).thenReturn(writer);

        processor.writeSourceFile(file, "My test source");
        assertEquals("My test source", writer.getBuffer().toString(), "Written text is incorrect");
    }

    @Test
    void should_print_error_while_write_sourceFile() throws IOException {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        JavaFileObject file = mock(JavaFileObject.class);
        when(file.openWriter()).thenThrow(IOException.class);
        Messager messager = mock(Messager.class);
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getMessager()).thenReturn(messager);

        processor.init(environment);
        processor.writeSourceFile(file, "ignore");

        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Exception of source file writing");
    }

    @Test
    void should_create_source() {
        MethodSupplierSourceGenerator generator = mock(MethodSupplierSourceGenerator.class);
        BaseMethodSupplierProcessor<Deprecated> processor = getMock(generator);
        when(generator.generate(any(), any(), anyMap())).thenReturn("generated source");

        TypeElement typeElement = typeElementMock(String.class);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);

        String source = processor.createSource(typeDescriptor, Collections.emptyMap(), "ignore");

        assertEquals("generated source", source, "Source is incorrect");
    }

    @Test
    void should_process() throws IOException {
        MethodSupplierSourceGenerator generator = mock(MethodSupplierSourceGenerator.class);
        class Child extends BaseMethodSupplierProcessor<Deprecated> {
            public Child() {
                super(Deprecated.class);
            }

            @Override
            protected MethodSupplierSourceGenerator getSourceGenerator() {
                return generator;
            }

            @Override
            protected String getCustomClassName(TypeDescriptor type) {
                return "";
            }

            @Override
            protected Set<String> getIncludedFields(TypeDescriptor type) {
                return Collections.emptySet();
            }

            @Override
            protected Set<String> getExcludedFields(TypeDescriptor type) {
                return Collections.emptySet();
            }

            @Override
            protected boolean isTargetField(FieldDescriptor field) {
                return false;
            }

            @Override
            protected String getMethod(FieldDescriptor field) {
                return null;
            }
        }
        BaseMethodSupplierProcessor<Deprecated> processor = new Child();
        when(generator.generate(any(), any(), anyMap())).thenReturn("generated source");
        StringWriter writer = new StringWriter();
        JavaFileObject file = mock(JavaFileObject.class);
        when(file.openWriter()).thenReturn(writer);
        Filer filer = mock(Filer.class);
        when(filer.createSourceFile(any())).thenReturn(file);
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getFiler()).thenReturn(filer);
        VariableElement variableElement = fieldMock("field", String.class);
        TypeElement typeElement = typeElementMock(String.class);
        Mockito.<List<? extends Element>>when(typeElement.getEnclosedElements())
                .thenReturn(Collections.singletonList(variableElement));

        processor.init(environment);
        processor.process(typeElement);

        assertEquals("generated source", writer.getBuffer().toString(),
                "Generated source does not equal to 'generated source'");
    }

    @Test
    void should_create_field2MethodMap() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        when(processor.isTargetField(any())).thenReturn(true);
        when(processor.getMethod(any())).thenReturn("method");

        TypeElement typeElement = typeElementMock(Arrays.asList(
                fieldMock("field1"),
                fieldMock("field2")));
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        Map<String, String> field2MethodMap = processor.createField2MethodMap(typeDescriptor);
        assertEquals(2, field2MethodMap.size(), "Size of field-method map is incorrect");
        assertTrue(field2MethodMap.containsKey("field1"), "Field-method map has no 'field1'");
        assertTrue(field2MethodMap.containsKey("field2"), "Field-method map has no 'field2'");
    }

    @Test
    void should_return_fields() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        FieldDescriptor field = new FieldDescriptor(fieldMock("field"));
        when(typeDescriptor.getFields()).thenReturn(Collections.singleton(field));
        Set<FieldDescriptor> fields = processor.getFields(typeDescriptor);
        assertEquals(1, fields.size(), "Number of fields does not equal to 1");
        assertEquals("field", fields.iterator().next().getName(), "Field name does not equal to 'field'");
    }

    @Test
    void should_return_includedFields() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        FieldDescriptor field = new FieldDescriptor(fieldMock("field"));
        FieldDescriptor included = new FieldDescriptor(fieldMock("included"));
        when(typeDescriptor.getFields()).thenReturn(new HashSet<>(Arrays.asList(field, included)));
        when(processor.getIncludedFields(any())).thenReturn(Collections.singleton("included"));
        Set<FieldDescriptor> fields = processor.getFields(typeDescriptor);
        assertEquals(1, fields.size(), "Number of fields does not equal to 1");
        assertEquals("included", fields.iterator().next().getName(), "Field name does not equal to 'included'");
    }

    @Test
    void should_return_notExcludedFields() {
        BaseMethodSupplierProcessor<Deprecated> processor = getMock();
        TypeDescriptor typeDescriptor = mock(TypeDescriptor.class);
        FieldDescriptor field = new FieldDescriptor(fieldMock("field"));
        FieldDescriptor excluded = new FieldDescriptor(fieldMock("excluded"));
        when(typeDescriptor.getFields()).thenReturn(new HashSet<>(Arrays.asList(field, excluded)));
        when(processor.getExcludedFields(any())).thenReturn(Collections.singleton("excluded"));
        Set<FieldDescriptor> fields = processor.getFields(typeDescriptor);
        assertEquals(1, fields.size(), "Number of fields does not equal to 1");
        assertEquals("field", fields.iterator().next().getName(), "Field name does not equal to 'field'");
    }
}