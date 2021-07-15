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

import dev.alexengrig.metter.element.descriptor.ElementDescriptor;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base processor of method supplier.
 *
 * @param <A> type of annotation
 * @param <E> type of element
 * @param <D> type of element descriptor
 * @author Grig Alex
 * @version 0.2.0
 * @since 0.1.0
 */
public abstract class BaseMethodSupplierProcessor<A extends Annotation, E extends Element, D extends ElementDescriptor<E>>
        extends BaseProcessor<A, E> {
    /**
     * Source generator.
     *
     * @since 0.1.0
     */
    protected final MethodSupplierSourceGenerator sourceGenerator;

    /**
     * Constructs with an annotation class
     *
     * @param annotationClass annotation class
     * @since 0.1.0
     */
    public BaseMethodSupplierProcessor(Class<? extends A> annotationClass) {
        super(annotationClass);
        this.sourceGenerator = getSourceGenerator();
    }

    /**
     * Returns a source generator.
     *
     * @return source generator
     * @since 0.1.0
     */
    protected abstract MethodSupplierSourceGenerator getSourceGenerator();

    /**
     * Process an element.
     *
     * @param element element
     * @since 0.2.0
     */
    @Override
    protected void process(E element) {
        D descriptor = createDescriptor(element);
        String sourceClassName = createSourceClassName(descriptor);
        JavaFileObject sourceFile = createSourceFile(sourceClassName);
        String source = createSource(sourceClassName, descriptor);
        writeSourceFile(sourceFile, source);
    }

    protected abstract D createDescriptor(E element);

    /**
     * Creates a source class name from a descriptor.
     *
     * @param descriptor descriptor
     * @return source class name from {@code descriptor}
     * @since 0.2.0
     */
    protected abstract String createSourceClassName(D descriptor);

    /**
     * Creates a source file for a class name.
     *
     * @param className class name
     * @return source file for {@code className}
     * @since 0.1.0
     */
    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new MetterException("Exception of source file creation for: " + className, e);
        }
    }

    /**
     * Creates a source from a source class name and a descriptor.
     *
     * @param sourceClassName source class name
     * @param descriptor      descriptor
     * @return source from {@code sourceClassName} and {@code descriptor}
     * @since 0.2.0
     */
    protected abstract String createSource(String sourceClassName, D descriptor);

    /**
     * Writes a source to a source file.
     *
     * @param sourceFile source file
     * @param source     source
     * @since 0.1.0
     */
    protected void writeSourceFile(JavaFileObject sourceFile, String source) {
        try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
            sourcePrinter.print(source);
        } catch (IOException e) {
            error("Exception of source file writing", e);
        }
    }
}
