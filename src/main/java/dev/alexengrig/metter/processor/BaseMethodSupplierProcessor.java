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

import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base processor of method supplier.
 *
 * @param <A> type of annotation
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.0
 */
public abstract class BaseMethodSupplierProcessor<A extends Annotation> extends BaseProcessor<A, TypeElement> {
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
     * Process a type element.
     *
     * @param typeElement type element
     * @since 0.1.0
     */
    @Override
    protected void process(TypeElement typeElement) {
        TypeDescriptor type = new TypeDescriptor(typeElement);
        String sourceClassName = createSourceClassName(type);
        JavaFileObject sourceFile = createSourceFile(sourceClassName);
        Map<String, String> field2Method = createField2MethodMap(type);
        String source = createSource(type, field2Method, sourceClassName);
        writeSourceFile(sourceFile, source);
    }

    /**
     * Creates a source class name from a type descriptor.
     *
     * @param type descriptor
     * @return source class name from {@code type}
     * @since 0.1.0
     */
    protected String createSourceClassName(TypeDescriptor type) {
        return getClassName(type).orElseGet(() -> getDefaultClassName(type));
    }

    /**
     * Returns a class name from a type descriptor.
     *
     * @param type descriptor
     * @return class name from {@code type}
     * @since 0.1.0
     */
    protected Optional<String> getClassName(TypeDescriptor type) {
        String customClassName = getCustomClassName(type);
        if (customClassName.isEmpty()) {
            return Optional.empty();
        }
        assertValidCustomClassName(customClassName);
        String className = type.getQualifiedName();
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return Optional.of(className.substring(0, lastIndexOfDot).concat(".").concat(customClassName));
        }
        return Optional.of(customClassName);
    }

    /**
     * Returns a custom class name from a type descriptor.
     *
     * @param type descriptor
     * @return custom class name from {@code type}
     * @since 0.1.0
     */
    protected abstract String getCustomClassName(TypeDescriptor type);

    /**
     * Asserts a valid custom class name.
     *
     * @param className custom class name
     * @throws MetterException if for {@code className} {@link SourceVersion#isKeyword(java.lang.CharSequence)} returns {@code true}
     *                         or {@link SourceVersion#isIdentifier(java.lang.CharSequence)} returns {@code false}
     * @since 0.1.1
     */
    protected void assertValidCustomClassName(String className) {
        if (SourceVersion.isKeyword(className) || !SourceVersion.isIdentifier(className)) {
            throw new MetterException("Custom class name is invalid: '" + className + "'");
        }
    }

    /**
     * Returns a default class name from a type descriptor.
     *
     * @param type descriptor
     * @return default class name from {@code type}
     * @since 0.1.0
     */
    protected String getDefaultClassName(TypeDescriptor type) {
        String className = type.getQualifiedName();
        return className + annotationClass.getSimpleName();
    }

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
     * Creates a map of field to method from a type descriptor.
     *
     * @param type descriptor
     * @return map of field to method from {@code type}
     * @since 0.1.0
     */
    protected Map<String, String> createField2MethodMap(TypeDescriptor type) {
        Map<String, String> field2Method = new HashMap<>();
        Set<FieldDescriptor> fields = getFields(type);
        for (FieldDescriptor field : fields) {
            if (isTargetField(field)) {
                field2Method.put(field.getName(), getMethod(field));
            }
        }
        return field2Method;
    }

    /**
     * Returns fields from a type descriptor.
     *
     * @param type descriptor
     * @return fields from {@code type}
     * @since 0.1.0
     */
    protected Set<FieldDescriptor> getFields(TypeDescriptor type) {
        Set<FieldDescriptor> fields = type.getFields();
        Set<String> includedFields = getIncludedFields(type);
        Set<String> excludedFields = getExcludedFields(type);
        if (includedFields.isEmpty() && excludedFields.isEmpty()) {
            return fields;
        }
        if (!includedFields.isEmpty()) {
            return fields.stream().filter(f -> includedFields.contains(f.getName())).collect(Collectors.toSet());
        } else {
            return fields.stream().filter(f -> !excludedFields.contains(f.getName())).collect(Collectors.toSet());
        }
    }

    /**
     * Returns included fields from a type descriptor.
     *
     * @param type descriptor
     * @return included fields from {@code type}
     * @since 0.1.0
     */
    protected abstract Set<String> getIncludedFields(TypeDescriptor type);

    /**
     * Returns excluded fields from a type descriptor.
     *
     * @param type descriptor
     * @return excluded fields from {@code type}
     * @since 0.1.0
     */
    protected abstract Set<String> getExcludedFields(TypeDescriptor type);

    /**
     * Checks if a field descriptor is target field.
     *
     * @param field descriptor
     * @return {@code field} is target field.
     * @since 0.1.0
     */
    protected abstract boolean isTargetField(FieldDescriptor field);

    /**
     * Returns a method for a field descriptor.
     *
     * @param field descriptor
     * @return method for {@code field}
     * @since 0.1.1
     */
    protected abstract String getMethod(FieldDescriptor field);

    /**
     * Creates a source from a type descriptor, a map of field to method and a source class name.
     *
     * @param type            descriptor
     * @param field2Method    map of filed to method
     * @param sourceClassName source class name
     * @return source from {@code type}, {@code field2Method} and {@code sourceClassName}
     * @since 0.1.0
     */
    protected String createSource(TypeDescriptor type, Map<String, String> field2Method, String sourceClassName) {
        return sourceGenerator.generate(sourceClassName, type.getQualifiedName(), field2Method);
    }

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
