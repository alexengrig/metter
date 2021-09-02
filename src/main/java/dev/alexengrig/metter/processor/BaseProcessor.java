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

import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.util.Exceptions;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base processor.
 *
 * @param <A> type of annotation
 * @param <E> type of element
 * @author Grig Alex
 * @version 0.2.0
 * @see javax.annotation.processing.AbstractProcessor
 * @since 0.1.0
 */
public abstract class BaseProcessor<A extends Annotation, E extends Element> extends AbstractProcessor {
    /**
     * {@link Class#getName()} for {@link Object}.
     *
     * @since 0.2.0
     */
    protected static final String JAVA_LANG_OBJECT_CLASS_NAME = Object.class.getName();

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
     * Prints a note message from an object.
     *
     * @param object object for message
     * @since 0.2.0
     */
    protected void note(Object object) {
        note(object.toString());
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
     * Returns all super type descriptors for a type descriptor.
     *
     * @param typeDescriptor type descriptor
     * @return all super type descriptors for {@code typeDescriptor}
     * @since 0.2.0
     */
    protected Set<TypeDescriptor> getAllSuperTypes(TypeDescriptor typeDescriptor) {
        Set<TypeDescriptor> target = new HashSet<>();
        Queue<TypeElement> queue = new LinkedList<>();
        queue.add(typeDescriptor.getElement());
        while (!queue.isEmpty()) {
            TypeElement type = queue.remove();
            Set<TypeElement> superTypes = processingEnv.getTypeUtils().directSupertypes(type.asType())
                    .stream()
                    .filter(typeMirror -> typeMirror.getKind() == TypeKind.DECLARED)
                    .map(DeclaredType.class::cast)
                    .map(DeclaredType::asElement)
                    .filter(element -> element.getKind() == ElementKind.CLASS)
                    .filter(element -> !JAVA_LANG_OBJECT_CLASS_NAME.equals(element.toString()))
                    .map(TypeElement.class::cast)
                    .collect(Collectors.toSet());
            if (!superTypes.isEmpty()) {
                target.addAll(superTypes.stream().map(TypeDescriptor::new).collect(Collectors.toSet()));
                queue.addAll(superTypes);
            }
        }
        return target;
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
