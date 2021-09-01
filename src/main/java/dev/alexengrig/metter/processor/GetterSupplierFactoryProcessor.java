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
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.GetterSupplierSourceGenerator;
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;

import javax.annotation.processing.Processor;
import java.util.Collections;
import java.util.Optional;

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
    protected String createSource(String sourceClassName, MethodDescriptor method) {
        Class<?> domainClass = method.getAnnotation(GetterSupplierFactory.class)
                .map(GetterSupplierFactory::value)
                .orElseThrow(() -> /*TODO*/ new MetterException("TODO"));
        return sourceGenerator.generate(sourceClassName, domainClass.getName(), Collections.emptyMap());
    }

    @Override
    protected Optional<String> getClassName(MethodDescriptor method) {
        String customClassName = method.getAnnotation(GetterSupplierFactory.class)
                .map(GetterSupplierFactory::props)
                .map(GetterSupplier::value)
                .orElseThrow(() -> new MetterException("Method has no annotation: " + method + ", " + annotationClass));
        if (customClassName.isEmpty()) {
            return Optional.empty();
        }
        assertValidCustomClassName(customClassName);
        TypeDescriptor type = method.getParent();
        if (type.hasPackage()) {
            return Optional.of(type.getPackage() + "." + customClassName);
        } else {
            return Optional.of(customClassName);
        }
    }

    @Override
    protected String getDefaultClassName(MethodDescriptor method) {
        Optional<GetterSupplierFactory> annotation = method.getAnnotation(GetterSupplierFactory.class);
        note(annotation.get().value().toString());
        return "";
        /*Class<?> domainClass = annotation
                .map(GetterSupplierFactory::value)
                .orElseThrow(() -> new MetterException("Method has no annotation: " + method + ", " + annotationClass));
        String className = domainClass.getSimpleName() + annotationClass.getSimpleName();
        TypeDescriptor typeDescriptor = method.getParent();
        if (typeDescriptor.hasPackage()) {
            return className;
        }
        String packageName = typeDescriptor.getPackage();
        return packageName + "." + className;*/
    }
}
