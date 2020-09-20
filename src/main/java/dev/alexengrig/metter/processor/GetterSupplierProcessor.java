/*
 * Copyright 2020 Alexengrig Dev.
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
import dev.alexengrig.metter.processor.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.processor.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.processor.generator.GetterSupplierSourceGenerator;

import javax.annotation.processing.Processor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A supplier processor for getters.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @see dev.alexengrig.metter.annotation.GetterSupplier
 * @see dev.alexengrig.metter.processor.BaseMethodSupplierProcessor
 * @since 0.1.0
 */
@AutoService(Processor.class)
public class GetterSupplierProcessor extends BaseMethodSupplierProcessor<GetterSupplier> {
    public GetterSupplierProcessor() {
        super(GetterSupplier.class);
    }

    @Override
    protected String getCustomClassNameFromAnnotation(TypeDescriptor type) {
        GetterSupplier annotation = type.getAnnotation(annotationClass);
        return annotation.value();
    }

    @Override
    protected Set<String> getIncludedFields(TypeDescriptor type) {
        GetterSupplier annotation = type.getAnnotation(annotationClass);
        return new HashSet<>(Arrays.asList(annotation.includedFields()));
    }

    @Override
    protected Set<String> getExcludedFields(TypeDescriptor type) {
        GetterSupplier annotation = type.getAnnotation(annotationClass);
        return new HashSet<>(Arrays.asList(annotation.excludedFields()));
    }

    @Override
    protected boolean hasAllMethods(TypeDescriptor type) {
        return type.hasAnnotation("lombok.Data") || type.hasAnnotation("lombok.Getter");
    }

    @Override
    protected String getMethodName(FieldDescriptor field) {
        String methodNamePrefix = "boolean".equals(field.getClassName()) ? "is" : "get";
        String name = field.getName();
        return methodNamePrefix + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    protected boolean isTargetField(FieldDescriptor field) {
        return field.hasAnnotation("lombok.Getter");
    }

    @Override
    protected String getMethodView(TypeDescriptor type, FieldDescriptor field, String methodName) {
        return type.getQualifiedName().concat("::").concat(methodName);
    }

    @Override
    protected String createSource(TypeDescriptor type, Map<Object, Object> field2Method, String sourceClassName) {
        GetterSupplierSourceGenerator sourceGenerator = new GetterSupplierSourceGenerator();
        return sourceGenerator.generate(sourceClassName, type.getQualifiedName(), field2Method);
    }
}
