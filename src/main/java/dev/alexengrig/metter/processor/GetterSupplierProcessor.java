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
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.GetterSupplierSourceGenerator;
import dev.alexengrig.metter.util.Strings;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.annotation.processing.Processor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Processor of getter supplier.
 *
 * @author Grig Alex
 * @version 0.1.1
 * @see dev.alexengrig.metter.annotation.GetterSupplier
 * @since 0.1.0
 */
@AutoService(Processor.class)
public class GetterSupplierProcessor extends BaseMethodSupplierProcessor<GetterSupplier> {
    /**
     * Constructs.
     *
     * @since 0.1.0
     */
    public GetterSupplierProcessor() {
        super(GetterSupplier.class);
    }

    /**
     * Returns a generator source of getter supplier.
     *
     * @return generator source of getter supplier
     * @since 0.1.0
     */
    @Override
    protected GetterSupplierSourceGenerator getSourceGenerator() {
        return new GetterSupplierSourceGenerator();
    }

    /**
     * Returns a custom class name from {@link dev.alexengrig.metter.annotation.GetterSupplier#value()}.
     *
     * @param type descriptor
     * @return custom class name from {@link dev.alexengrig.metter.annotation.GetterSupplier#value()}
     * @since 0.1.0
     */
    @Override
    protected String getCustomClassName(TypeDescriptor type) {
        return type.getAnnotation(annotationClass)
                .map(GetterSupplier::value)
                .orElseThrow(() -> new MetterException("Type has no annotation: " + type + ", " + annotationClass));
    }

    /**
     * Returns included fields from {@link dev.alexengrig.metter.annotation.GetterSupplier#includedFields()}.
     *
     * @param type descriptor
     * @return included fields from {@link dev.alexengrig.metter.annotation.GetterSupplier#includedFields()}
     * @since 0.1.0
     */
    @Override
    protected Set<String> getIncludedFields(TypeDescriptor type) {
        return type.getAnnotation(annotationClass)
                .map(GetterSupplier::includedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Type " + type + " has no annotation: " + annotationClass));
    }

    /**
     * Returns excluded fields from {@link dev.alexengrig.metter.annotation.GetterSupplier#excludedFields()}.
     *
     * @param type descriptor
     * @return excluded fields from {@link dev.alexengrig.metter.annotation.GetterSupplier#excludedFields()}
     * @since 0.1.0
     */
    @Override
    protected Set<String> getExcludedFields(TypeDescriptor type) {
        return type.getAnnotation(annotationClass)
                .map(GetterSupplier::excludedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Type " + type + " has no annotation: " + annotationClass));
    }

    /**
     * Checks if a field descriptor has {@link lombok.Getter} (not private) annotation
     * or a type descriptor of field descriptor has {@link lombok.Getter} (not private) annotation
     * or type descriptor of field descriptor has {@link lombok.Data}
     * or type descriptor of field descriptor has a getter method.
     *
     * @param field descriptor
     * @return if {@code descriptor} has {@link lombok.Getter} (not private) annotation
     * or a type descriptor of {@code descriptor} has {@link lombok.Getter} (not private) annotation
     * or type descriptor of {@code descriptor} has {@link lombok.Data}
     * or type descriptor of {@code descriptor} has a getter method
     * @since 0.1.1
     */
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
     * Returns a getter for a field descriptor.
     *
     * @param field descriptor
     * @return getter for {@code field}
     * @since 0.1.1
     */
    @Override
    protected String getMethod(FieldDescriptor field) {
        return field.getParent().getQualifiedName() + "::" + getGetterMethod(field);
    }
}
