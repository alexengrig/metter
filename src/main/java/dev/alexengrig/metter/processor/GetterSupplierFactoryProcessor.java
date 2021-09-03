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

import com.google.auto.service.AutoService;
import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.GetterSupplierFactory;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.GetterSupplierSourceGenerator;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.annotation.processing.Processor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class GetterSupplierFactoryProcessor extends OnMethodSupplierProcessor<GetterSupplierFactory> {
    public GetterSupplierFactoryProcessor() {
        super(GetterSupplierFactory.class);
    }

    @Override
    protected MethodSupplierSourceGenerator getSourceGenerator() {
        return new GetterSupplierSourceGenerator();
    }

    @Override
    protected String getCustomClassName(MethodDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(GetterSupplierFactory::props)
                .map(GetterSupplier::value)
                .orElseThrow(() -> new MetterException("Type has no annotation: " + descriptor + ", " + annotationClass));
    }

    @Override
    protected Set<String> getIncludedFields(MethodDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(GetterSupplierFactory::props)
                .map(GetterSupplier::includedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Method " + descriptor + " has no annotation: " + annotationClass));
    }

    @Override
    protected Set<String> getExcludedFields(MethodDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(GetterSupplierFactory::props)
                .map(GetterSupplier::excludedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Method " + descriptor + " has no annotation: " + annotationClass));
    }

    @Override
    protected boolean isTargetField(FieldDescriptor field) {
        if (field.hasAnnotation(Getter.class)) {
            return !field.getAnnotation(Getter.class)
                    .map(Getter::value)
                    .filter(AccessLevel.PRIVATE::equals)
                    .isPresent();
        }
        TypeDescriptor type = field.getParent();
        if (type.hasAnnotation(Getter.class)) {
            return !type.getAnnotation(Getter.class)
                    .map(Getter::value)
                    .filter(AccessLevel.PRIVATE::equals)
                    .isPresent();
        }
        return type.hasAnnotation(Data.class) || hasGetterMethod(field);
    }

    @Override
    protected String getMethod(FieldDescriptor field) {
        return field.getParent().getQualifiedName() + "::" + getGetterMethod(field);
    }
}
