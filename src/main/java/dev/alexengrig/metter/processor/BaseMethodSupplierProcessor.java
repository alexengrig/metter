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
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;
import dev.alexengrig.metter.util.Strings;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * Returns a custom class name from a descriptor.
     *
     * @param descriptor descriptor
     * @return custom class name from {@code descriptor}
     * @since 0.1.0
     */
    protected abstract String getCustomClassName(D descriptor);

    /**
     * Returns a default class name from a domain class name.
     *
     * @param domainClassName domain class name
     * @return default class name from {@code domainClassName}
     * @since 0.1.0
     */
    protected String getDefaultClassName(String domainClassName) {
        return domainClassName.concat(annotationClass.getSimpleName());
    }

    /**
     * Asserts a valid custom class name.
     *
     * @param className custom class name
     * @throws MetterException if for {@code className} {@link SourceVersion#isKeyword(java.lang.CharSequence)} returns {@code true}
     *                         or {@link SourceVersion#isIdentifier(java.lang.CharSequence)} returns {@code false}
     * @since 0.2.0
     */
    protected void assertValidCustomClassName(String className) {
        if (SourceVersion.isKeyword(className) || !SourceVersion.isIdentifier(className)) {
            throw new MetterException("Custom class name is invalid: '" + className + "'");
        }
    }

    /**
     * Creates a source file for an qualified class name.
     *
     * @param qualifiedClassName qualified class name
     * @return source file for {@code qualifiedClassName}
     * @since 0.1.0
     */
    protected JavaFileObject createSourceFile(String qualifiedClassName) {
        try {
            return processingEnv.getFiler().createSourceFile(qualifiedClassName);
        } catch (IOException e) {
            throw new MetterException("Exception of source file creation for: " + qualifiedClassName, e);
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
     * Creates a map of field to method from a descriptor.
     *
     * @param descriptor descriptor
     * @return map of field to method from {@code descriptor}
     * @since 0.1.0
     */
    protected Map<String, String> createField2MethodMap(D descriptor) {
        Map<String, String> field2Method = new HashMap<>();
        Set<FieldDescriptor> fields = getFields(descriptor);
        for (FieldDescriptor field : fields) {
            if (isTargetField(field)) {
                field2Method.put(field.getName(), getMethod(field));
            }
        }
        return field2Method;
    }

    /**
     * Returns fields from a descriptor with fields of super classes.
     *
     * @param descriptor descriptor
     * @return fields from {@code descriptor} with fields of super classes
     * @since 0.1.0
     */
    protected abstract Set<FieldDescriptor> getFields(D descriptor);

    /**
     * Returns included fields from a descriptor.
     *
     * @param descriptor descriptor
     * @return included fields from {@code descriptor}
     * @since 0.1.0
     */
    protected abstract Set<String> getIncludedFields(D descriptor);

    /**
     * Returns excluded fields from a descriptor.
     *
     * @param descriptor descriptor
     * @return excluded fields from {@code descriptor}
     * @since 0.1.0
     */
    protected abstract Set<String> getExcludedFields(D descriptor);

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
     * Checks if a type descriptor of a field descriptor has a getter method
     *
     * @param field descriptor
     * @return if a type descriptor of {@code descriptor} has a getter method
     * @since 0.1.1
     */
    protected boolean hasGetterMethod(FieldDescriptor field) {
        String getter = getGetterMethod(field);
        TypeDescriptor type = field.getParent();
        if (type.hasMethod(getter)) {
            Set<MethodDescriptor> methods = type.getMethods(getter);
            return methods.stream().anyMatch(method -> method.isNotPrivate() && method.hasNoParameters()
                    && field.getTypeName().equals(method.getTypeName()));
        }
        return false;
    }

    /**
     * Returns a getter-method for a field descriptor.
     *
     * @param field descriptor
     * @return getter-method for {@code field}
     * @since 0.1.1
     */
    protected String getGetterMethod(FieldDescriptor field) {
        String methodNamePrefix = "boolean".equals(field.getTypeName()) ? "is" : "get";
        String name = field.getName();
        return methodNamePrefix + Strings.capitalize(name);
    }


    /**
     * Checks if a type descriptor of a field descriptor has a setter method
     *
     * @param field descriptor
     * @return if a type descriptor of {@code field} has a setter method
     * @since 0.1.1
     */
    protected boolean hasSetterMethod(FieldDescriptor field) {
        String methodName = getSetterMethod(field);
        TypeDescriptor type = field.getParent();
        if (type.hasMethod(methodName)) {
            Set<MethodDescriptor> methods = type.getMethods(methodName);
            return methods.stream().anyMatch(method -> method.isNotPrivate() && "void".equals(method.getTypeName())
                    && method.hasOnlyOneParameter(field.getTypeName()));
        }
        return false;
    }

    /**
     * Returns a setter-method for a field descriptor.
     *
     * @param field descriptor
     * @return setter-method for {@code field}
     * @since 0.1.1
     */
    protected String getSetterMethod(FieldDescriptor field) {
        String name = field.getName();
        return "set" + Strings.capitalize(name);
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
