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

import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OnClassMethodSupplierProcessor<A extends Annotation>
        extends BaseMethodSupplierProcessor<A, TypeElement, TypeDescriptor> {
    public OnClassMethodSupplierProcessor(Class<? extends A> annotationClass) {
        super(annotationClass);
    }

    @Override
    protected TypeDescriptor createDescriptor(TypeElement element) {
        return new TypeDescriptor(element);
    }

    /**
     * Creates a source class name from a type descriptor.
     *
     * @param descriptor type descriptor
     * @return source class name from {@code descriptor}
     * @since 0.2.0
     */
    @Override
    protected String createSourceClassName(TypeDescriptor descriptor) {
        return getClassName(descriptor).orElseGet(() -> getDefaultClassName(descriptor));
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

    @Override
    protected String createSource(String sourceClassName, TypeDescriptor descriptor) {
        Map<String, String> field2Method = createField2MethodMap(descriptor);
        return sourceGenerator.generate(sourceClassName, descriptor.getQualifiedName(), field2Method);
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
     * Returns fields from a type descriptor with fields of super classes.
     *
     * @param type descriptor
     * @return fields from {@code type} with fields of super classes
     * @since 0.1.0
     */
    protected Set<FieldDescriptor> getFields(TypeDescriptor type) {
        Set<TypeDescriptor> superTypes = getAllSuperTypes(type);
        Set<FieldDescriptor> fields = Stream.concat(Stream.of(type), superTypes.stream())
                .flatMap(descriptor -> descriptor.getFields().stream())
                .collect(Collectors.toSet());
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
}
