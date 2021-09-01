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

import dev.alexengrig.metter.element.descriptor.MethodDescriptor;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

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
        return getClassName(descriptor).orElseGet(() -> getDefaultClassName(descriptor));
    }

    protected abstract Optional<String> getClassName(MethodDescriptor descriptor);

    protected abstract String getDefaultClassName(MethodDescriptor descriptor);
}
