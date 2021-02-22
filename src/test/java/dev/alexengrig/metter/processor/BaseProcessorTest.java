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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseProcessorTest {
    static final BaseProcessor<Deprecated, Element> processor;
    static final ProcessingEnvironment environment;

    static {
        environment = mock(ProcessingEnvironment.class);
        processor = new BaseProcessor<Deprecated, Element>(Deprecated.class) {
            {
                init(environment);
            }

            @Override
            protected void process(Element annotatedElement) {
            }
        };
    }

    @Test
    void should_not_process() {
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        when(roundEnvironment.processingOver()).thenReturn(true);
        assertFalse(processor.process(Collections.emptySet(), roundEnvironment), "Processor must not process");
    }

    @Test
    void should_process() {
        RoundEnvironment roundEnvironment = mock(RoundEnvironment.class);
        when(roundEnvironment.processingOver()).thenReturn(false);
        Element typeElement = ElementMocks.typeElementMock();
        Mockito.<Set<? extends Element>>when(roundEnvironment.getElementsAnnotatedWith(Deprecated.class))
                .thenReturn(Collections.singleton(typeElement));
        assertTrue(processor.process(Collections.emptySet(), roundEnvironment), "Processor must process");
        verify(roundEnvironment).getElementsAnnotatedWith(Deprecated.class);
    }

    @Test
    void should_print_noteMessage() {
        Messager messager = mock(Messager.class);
        when(environment.getMessager()).thenReturn(messager);
        processor.note("Note message");
        verify(messager).printMessage(Diagnostic.Kind.NOTE, "Note message");
    }

    @Test
    void should_print_errorMessage() {
        Messager messager = mock(Messager.class);
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
        processor.error("Error message", exception);
        verify(messager).printMessage(eq(Diagnostic.Kind.ERROR), any());
        assertEquals(1, messages.size(), "Number of messages is not 1");
        String message = messages.get(0);
        assertEquals("Error message" + lineSeparator() + "java.lang.RuntimeException without stack trace", message,
                "Error message is incorrect");
    }

    @Test
    void should_return_supportedOptions() {
        assertTrue(processor.getSupportedOptions().isEmpty(), "Processor has supported options");
    }

    @Test
    void should_return_supportedVersion() {
        assertEquals(SourceVersion.RELEASE_8, processor.getSupportedSourceVersion(),
                "Supported source version does not equal to 8");
    }

    @Test
    void should_return_supportedAnnotations() {
        assertEquals(Collections.singleton("java.lang.Deprecated"), processor.getSupportedAnnotationTypes(),
                "Supported annotation types are not equal to 'java.lang.Deprecated'");
    }
}