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
import dev.alexengrig.metter.FieldChecker;
import dev.alexengrig.metter.annotation.SetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplierFactory;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;
import dev.alexengrig.metter.generator.SetterSupplierSourceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.annotation.processing.Processor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AutoService(Processor.class)
public class SetterSupplierFactoryProcessor extends OnMethodSupplierProcessor<SetterSupplierFactory> {
    public SetterSupplierFactoryProcessor() {
        super(SetterSupplierFactory.class);
    }

    @Override
    protected MethodSupplierSourceGenerator getSourceGenerator() {
        return new SetterSupplierSourceGenerator();
    }

    @Override
    protected String getCustomClassName(MethodDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(SetterSupplierFactory::props)
                .map(SetterSupplier::value)
                .orElseThrow(() -> new MetterException("Method has no annotation: " + descriptor + ", " + annotationClass));
    }

    @Override
    protected Set<String> getIncludedFields(MethodDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(SetterSupplierFactory::props)
                .map(SetterSupplier::includedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Method " + descriptor + " has no annotation: " + annotationClass));
    }

    @Override
    protected Set<String> getExcludedFields(MethodDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(SetterSupplierFactory::props)
                .map(SetterSupplier::excludedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Method " + descriptor + " has no annotation: " + annotationClass));
    }

    @Override
    protected FieldChecker getFieldChecker(MethodDescriptor descriptor) {
        String methodPackage;
        if (descriptor.getParent().hasPackage()) {
            methodPackage = descriptor.getParent().getPackage();
        } else {
            methodPackage = "";
        }
        return field -> {
            if (field.hasAnnotation(Setter.class)) {
                return !field.getAnnotation(Setter.class)
                        .map(Setter::value)
                        .filter(AccessLevel.PRIVATE::equals)
                        .isPresent();
            }
            TypeDescriptor type = field.getParent();
            if (type.hasAnnotation(Setter.class)) {
                return !type.getAnnotation(Setter.class)
                        .map(Setter::value)
                        .filter(AccessLevel.PRIVATE::equals)
                        .isPresent();
            }
            if (type.hasAnnotation(Data.class)) {
                return true;
            }
            Optional<MethodDescriptor> setterOptional = getSetter(field);
            if (!setterOptional.isPresent()) {
                return false;
            }
            MethodDescriptor method = setterOptional.get();
            if (method.isPublic()) {
                return true;
            }
            return methodPackage.equals(field.getParent().getPackage());
        };
    }

    @Override
    protected String getMethod(FieldDescriptor field) {
        return "(instance, value) -> instance." + getSetterMethodName(field) + "((" + field.getTypeName() + ") value)";
    }
}
