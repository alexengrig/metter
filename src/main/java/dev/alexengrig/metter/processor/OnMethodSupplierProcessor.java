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
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OnMethodSupplierProcessor<A extends Annotation>
        extends BaseMethodSupplierProcessor<A, ExecutableElement, MethodDescriptor> {
    public OnMethodSupplierProcessor(Class<? extends A> annotationClass) {
        super(annotationClass);
    }

    @Override
    protected MethodDescriptor createDescriptor(ExecutableElement element) {
        return new MethodDescriptor(element);
    }

    @Override
    protected String createSourceClassName(MethodDescriptor descriptor) {
        TypeDescriptor methodTypeDescriptor = descriptor.getParent();
        TypeDescriptor domainTypeDescriptor = getDomainTypeDescriptor(descriptor);
        String customClassName = getCustomClassName(descriptor);
        if (customClassName.isEmpty()) {
            String defaultClassName = getDefaultClassName(domainTypeDescriptor.getSimpleName());
            if (methodTypeDescriptor.hasPackage()) {
                return methodTypeDescriptor.getPackage().concat(".").concat(defaultClassName);
            }
            return defaultClassName;
        }
        assertValidCustomClassName(customClassName);
        if (methodTypeDescriptor.hasPackage()) {
            return methodTypeDescriptor.getPackage().concat(".").concat(customClassName);
        }
        return customClassName;
    }

    @Override
    protected String createSource(String sourceClassName, MethodDescriptor descriptor) {
        TypeDescriptor typeDescriptor = getDomainTypeDescriptor(descriptor);
        Map<String, String> field2Method = createField2MethodMap(descriptor);
        return sourceGenerator.generate(sourceClassName, typeDescriptor.getQualifiedName(), field2Method);
    }

    @Override
    protected Set<FieldDescriptor> getFields(MethodDescriptor methodDescriptor) {
        TypeDescriptor typeDescriptor = getDomainTypeDescriptor(methodDescriptor);
        Set<TypeDescriptor> superTypes = getAllSuperTypes(typeDescriptor);
        Set<FieldDescriptor> fields = Stream.concat(Stream.of(typeDescriptor), superTypes.stream())
                .flatMap(descriptor -> descriptor.getFields().stream())
                .collect(Collectors.toSet());
        Set<String> includedFields = getIncludedFields(methodDescriptor);
        Set<String> excludedFields = getExcludedFields(methodDescriptor);
        if (includedFields.isEmpty() && excludedFields.isEmpty()) {
            return fields;
        }
        if (!includedFields.isEmpty()) {
            return fields.stream().filter(f -> includedFields.contains(f.getName())).collect(Collectors.toSet());
        } else {
            return fields.stream().filter(f -> !excludedFields.contains(f.getName())).collect(Collectors.toSet());
        }
    }

    protected TypeDescriptor getDomainTypeDescriptor(MethodDescriptor descriptor) {
        List<? extends AnnotationMirror> annotations = descriptor.getElement().getAnnotationMirrors();
        for (AnnotationMirror annotation : annotations) {
            if (annotationClass.getName().equals(annotation.getAnnotationType().toString())) {
                Map<? extends ExecutableElement, ? extends AnnotationValue> valueByMethod = annotation.getElementValues();
                for (ExecutableElement method : valueByMethod.keySet()) {
                    if (method.getSimpleName().contentEquals("value")) {
                        AnnotationValue value = valueByMethod.get(method);
                        TypeMirror typeMirror = (TypeMirror) value.getValue();
                        Element element = processingEnv.getTypeUtils().asElement(typeMirror);
                        return new TypeDescriptor((TypeElement) element);
                    }
                }
            }
        }
        throw new MetterException("GetterSupplierFactory annotation with a domain class");
    }
}
