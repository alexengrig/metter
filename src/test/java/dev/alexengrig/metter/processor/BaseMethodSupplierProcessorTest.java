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

import dev.alexengrig.metter.element.ElementMocks;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseMethodSupplierProcessorTest {
    static final BaseMethodSupplierProcessor<Deprecated> processor;
    static final ProcessingEnvironment environment;
    static final JavaFileObject sourceFile;

    static {
        environment = mock(ProcessingEnvironment.class);
        processor = new BaseMethodSupplierProcessor<Deprecated>(Deprecated.class) {
            {
                init(environment);
            }

            @Override
            protected MethodSupplierSourceGenerator getSourceGenerator() {
                return null;
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
            protected boolean hasAllMethods(TypeDescriptor type) {
                return false;
            }

            @Override
            protected String getMethodName(FieldDescriptor field) {
                return null;
            }

            @Override
            protected boolean isTargetField(FieldDescriptor field) {
                return false;
            }

            @Override
            protected String getMethodView(TypeDescriptor type, FieldDescriptor field, String methodName) {
                return null;
            }

            @Override
            protected String createSource(TypeDescriptor type, Map<String, String> field2Method, String sourceClassName) {
                return null;
            }
        };
        sourceFile = mock(JavaFileObject.class);
    }

    @Test
    void should_create_defaultClassName() {
        TypeElement typeElement = ElementMocks.typeElementMock(String.class);
        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);
        assertEquals("java.lang.StringDeprecated", processor.getDefaultClassName(typeDescriptor));
    }

    @Test
    void should_create_sourceFile() throws IOException {
        Filer filer = mock(Filer.class);
        when(filer.createSourceFile(any())).thenReturn(sourceFile);
        when(environment.getFiler()).thenReturn(filer);
        assertSame(sourceFile, processor.createSourceFile("MySource"));
        verify(filer).createSourceFile("MySource");
    }

    @Test
    void should_throw_exception_while_create_sourceFile() throws IOException {
        Filer filer = mock(Filer.class);
        when(filer.createSourceFile(any())).thenThrow(IOException.class);
        when(environment.getFiler()).thenReturn(filer);
        assertThrows(IllegalArgumentException.class, () -> processor.createSourceFile("MySource"));
    }

    @Test
    void should_write_sourceFile() throws IOException {
        JavaFileObject file = mock(JavaFileObject.class);
        StringWriter writer = new StringWriter();
        when(file.openWriter()).thenReturn(writer);
        processor.writeSourceFile(file, "My test source");
        assertEquals("My test source", writer.getBuffer().toString(), "Written text is not equal to 'My test source'");
    }

    @Test
    void should_print_error_while_write_sourceFile() throws IOException {
        JavaFileObject file = mock(JavaFileObject.class);
        when(file.openWriter()).thenThrow(IOException.class);
        Messager messager = mock(Messager.class);
        when(environment.getMessager()).thenReturn(messager);
        processor.writeSourceFile(file, "ignore");
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Exception of source file writing");
    }
}