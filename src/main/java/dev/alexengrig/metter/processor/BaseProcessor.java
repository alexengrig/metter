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

import dev.alexengrig.metter.util.Exceptions;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * Base processor.
 *
 * @param <A> type of annotation
 * @param <E> type of element
 * @author Grig Alex
 * @version 0.1.1
 * @see javax.annotation.processing.AbstractProcessor
 * @since 0.1.0
 */
public abstract class BaseProcessor<A extends Annotation, E extends Element> extends AbstractProcessor {
    /**
     * Annotation class.
     *
     * @since 0.1.0
     */
    protected final Class<? extends A> annotationClass;

    /**
     * Constructs with an annotation class.
     *
     * @param annotationClass annotation class
     * @since 0.1.0
     */
    public BaseProcessor(Class<? extends A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * {@inheritDoc}
     *
     * @see #process(Element)
     * @since 0.1.0
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            @SuppressWarnings("unchecked")
            E element = (E) annotatedElement;
            process(element);
        }
        return true;
    }

    /**
     * Processes a annotated element.
     *
     * @param annotatedElement annotated element
     * @since 0.1.0
     */
    protected abstract void process(E annotatedElement);

    /**
     * Prints a note message.
     *
     * @param message message text
     * @since 0.1.0
     */
    protected void note(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    /**
     * Prints an error message.
     *
     * @param message   message text
     * @param throwable exception
     * @since 0.1.0
     */
    protected void error(String message, Throwable throwable) {
        String stackTrace = Exceptions.getStackTrace(throwable);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message + System.lineSeparator() + stackTrace);
    }

    /**
     * Returns an empty set.
     *
     * @return empty set
     * @since 0.1.0
     */
    @Override
    public Set<String> getSupportedOptions() {
        return Collections.emptySet();
    }

    /**
     * Returns 1.8 version.
     *
     * @return 1.8 version
     * @since 0.1.0
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    /**
     * Returns a set with an annotation class name.
     *
     * @return set with an annotation class name.
     * @since 0.1.0
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(annotationClass.getName());
    }
}
