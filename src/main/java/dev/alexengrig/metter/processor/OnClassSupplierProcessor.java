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

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OnClassSupplierProcessor<A extends Annotation>
        extends BaseMethodSupplierProcessor<A, TypeElement, TypeDescriptor> {
    public OnClassSupplierProcessor(Class<? extends A> annotationClass) {
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
        String customClassName = getCustomClassName(descriptor);
        if (customClassName.isEmpty()) {
            String defaultClassName = getDefaultClassName(descriptor.getSimpleName());
            if (descriptor.hasPackage()) {
                return descriptor.getPackage().concat(".").concat(defaultClassName);
            }
            return defaultClassName;
        }
        assertValidCustomClassName(customClassName);
        if (descriptor.hasPackage()) {
            return descriptor.getPackage().concat(".").concat(customClassName);
        }
        return customClassName;
    }

    @Override
    protected String createSource(String sourceClassName, TypeDescriptor descriptor) {
        Map<String, String> field2Method = createField2MethodMap(descriptor);
        return sourceGenerator.generate(sourceClassName, descriptor.getQualifiedName(), field2Method);
    }

    @Override
    protected Set<FieldDescriptor> getFields(TypeDescriptor typeDescriptor) {
        Set<TypeDescriptor> superTypes = getAllSuperTypes(typeDescriptor);
        Set<FieldDescriptor> fields = Stream.concat(Stream.of(typeDescriptor), superTypes.stream())
                .flatMap(descriptor -> descriptor.getFields().stream())
                .collect(Collectors.toSet());
        Set<String> includedFields = getIncludedFields(typeDescriptor);
        Set<String> excludedFields = getExcludedFields(typeDescriptor);
        if (includedFields.isEmpty() && excludedFields.isEmpty()) {
            return fields;
        }
        if (!includedFields.isEmpty()) {
            return fields.stream().filter(f -> includedFields.contains(f.getName())).collect(Collectors.toSet());
        } else {
            return fields.stream().filter(f -> !excludedFields.contains(f.getName())).collect(Collectors.toSet());
        }
    }
}
