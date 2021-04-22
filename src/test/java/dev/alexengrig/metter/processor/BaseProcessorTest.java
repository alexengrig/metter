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
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static dev.alexengrig.metter.ElementMocks.fieldMock;
import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseProcessorTest {
    private static BaseProcessor<Deprecated, Element> getMock() {
        return new BaseProcessor<Deprecated, Element>(Deprecated.class) {
            @Override
            protected void process(Element annotatedElement) {
            }
        };
    }

    @Test
    void should_not_process() {
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        when(roundEnvironment.processingOver()).thenReturn(true);
        BaseProcessor<Deprecated, Element> processor = getMock();
        assertFalse(processor.process(Collections.emptySet(), roundEnvironment), "Processor must not process");
    }

    @Test
    void should_process() {
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        when(roundEnvironment.processingOver()).thenReturn(false);
        Element typeElement = ElementMocks.typeElementMock();
        Mockito.<Set<? extends Element>>when(roundEnvironment.getElementsAnnotatedWith(Deprecated.class))
                .thenReturn(Collections.singleton(typeElement));
        BaseProcessor<Deprecated, Element> processor = getMock();
        assertTrue(processor.process(Collections.emptySet(), roundEnvironment), "Processor must process");
        verify(roundEnvironment).getElementsAnnotatedWith(Deprecated.class);
    }

    @Test
    void should_print_noteMessage() {
        Messager messager = mock(Messager.class);
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getMessager()).thenReturn(messager);
        BaseProcessor<Deprecated, Element> processor = getMock();
        processor.init(environment);
        processor.note("Note message");
        verify(messager).printMessage(Diagnostic.Kind.NOTE, "Note message");
    }

    @Test
    void should_print_errorMessage() {
        Messager messager = mock(Messager.class);
        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getMessager()).thenReturn(messager);
        ArrayList<String> messages = new ArrayList<>();
        doAnswer(invocation -> {
            messages.add(invocation.getArgument(1));
            return null;
        }).when(messager).printMessage(eq(Diagnostic.Kind.ERROR), any());
        RuntimeException exception = mock(RuntimeException.class);
        doAnswer(invocation -> {
            PrintWriter printWriter = invocation.getArgument(0, PrintWriter.class);
            printWriter.write("java.lang.RuntimeException without stack trace");
            return null;
        }).when(exception).printStackTrace(any(PrintWriter.class));
        BaseProcessor<Deprecated, Element> processor = getMock();
        processor.init(environment);
        processor.error("Error message", exception);
        verify(messager).printMessage(eq(Diagnostic.Kind.ERROR), any());
        assertEquals(1, messages.size(), "Number of messages is not 1");
        String message = messages.get(0);
        assertEquals("Error message" + lineSeparator() + "java.lang.RuntimeException without stack trace", message,
                "Error message is incorrect");
    }

    @Test
    void should_return_allSuperTypes() {
        VariableElement superField = fieldMock("superField");

        TypeElement superTypeElement = mock(TypeElement.class);
        Mockito.<List<? extends Element>>when(superTypeElement.getEnclosedElements())
                .thenReturn(Collections.singletonList(superField));
        when(superTypeElement.getKind()).thenReturn(ElementKind.CLASS);
        when(superTypeElement.toString()).thenReturn("NoObject");

        DeclaredType declaredType = mock(DeclaredType.class);
        when(declaredType.asElement()).thenReturn(superTypeElement);
        when(declaredType.getKind()).thenReturn(TypeKind.DECLARED);

        TypeMirror typeMirror = mock(TypeMirror.class);
        TypeElement typeElement = mock(TypeElement.class);
        when(typeElement.asType()).thenReturn(typeMirror);

        Types types = mock(Types.class);
        Mockito.<List<? extends TypeMirror>>when(types.directSupertypes(same(typeMirror)))
                .thenReturn(Collections.singletonList(declaredType));

        ProcessingEnvironment environment = mock(ProcessingEnvironment.class);
        when(environment.getTypeUtils()).thenReturn(types);

        TypeDescriptor typeDescriptor = new TypeDescriptor(typeElement);

        BaseProcessor<Deprecated, Element> processor = getMock();

        processor.init(environment);

        Set<TypeDescriptor> superTypes = processor.getAllSuperTypes(typeDescriptor);

        assertEquals(1, superTypes.size(), "Number of super types are incorrect");
        assertSame(superTypeElement, superTypes.iterator().next().getElement(), "Super type element is incorrect");
    }

    @Test
    void should_return_supportedOptions() {
        BaseProcessor<Deprecated, Element> processor = getMock();
        assertTrue(processor.getSupportedOptions().isEmpty(), "Processor has supported options");
    }

    @Test
    void should_return_supportedVersion() {
        BaseProcessor<Deprecated, Element> processor = getMock();
        assertEquals(SourceVersion.RELEASE_8, processor.getSupportedSourceVersion(),
                "Supported source version does not equal to 8");
    }

    @Test
    void should_return_supportedAnnotations() {
        BaseProcessor<Deprecated, Element> processor = getMock();
        assertEquals(Collections.singleton("java.lang.Deprecated"), processor.getSupportedAnnotationTypes(),
                "Supported annotation types are not equal to 'java.lang.Deprecated'");
    }
}