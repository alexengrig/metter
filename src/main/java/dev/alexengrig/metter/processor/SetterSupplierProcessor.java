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
import dev.alexengrig.metter.annotation.SetterSupplier;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.exception.MetterException;
import dev.alexengrig.metter.generator.SetterSupplierSourceGenerator;
import dev.alexengrig.metter.util.Strings;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.annotation.processing.Processor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Processor of setter supplier.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @see dev.alexengrig.metter.annotation.SetterSupplier
 * @since 0.1.0
 */
@AutoService(Processor.class)
public class SetterSupplierProcessor extends OnClassSupplierProcessor<SetterSupplier> {
    /**
     * Constructs.
     *
     * @since 0.1.0
     */
    public SetterSupplierProcessor() {
        super(SetterSupplier.class);
    }

    /**
     * Returns a generator source of setter supplier.
     *
     * @return generator source of setter supplier
     * @since 0.1.0
     */
    @Override
    protected SetterSupplierSourceGenerator getSourceGenerator() {
        return new SetterSupplierSourceGenerator();
    }

    /**
     * Returns a custom class name from {@link dev.alexengrig.metter.annotation.SetterSupplier#value()}.
     *
     * @param descriptor descriptor
     * @return custom class name from {@link dev.alexengrig.metter.annotation.SetterSupplier#value()}
     * @since 0.1.0
     */
    @Override
    protected String getCustomClassName(TypeDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(SetterSupplier::value)
                .orElseThrow(() -> new MetterException("Type " + descriptor + " has no annotation: " + annotationClass));
    }

    /**
     * Returns included fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#includedFields()}.
     *
     * @param descriptor descriptor
     * @return included fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#includedFields()}
     * @since 0.1.0
     */
    @Override
    protected Set<String> getIncludedFields(TypeDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(SetterSupplier::includedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Type " + descriptor + " has no annotation: " + annotationClass));
    }

    /**
     * Returns excluded fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#excludedFields()}.
     *
     * @param descriptor descriptor
     * @return excluded fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#excludedFields()}
     * @since 0.1.0
     */
    @Override
    protected Set<String> getExcludedFields(TypeDescriptor descriptor) {
        return descriptor.getAnnotation(annotationClass)
                .map(SetterSupplier::excludedFields)
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElseThrow(() -> new MetterException("Type " + descriptor + " has no annotation: " + annotationClass));
    }

    /**
     * Checks if a field descriptor has {@link lombok.Setter} annotation (not private)
     * or a type descriptor of field descriptor has {@link lombok.Setter} annotation (not private)
     * or type descriptor of field descriptor has {@link lombok.Data}
     * or type descriptor of field descriptor has a setter method.
     *
     * @param field descriptor
     * @return if {@code descriptor} has {@link lombok.Setter} annotation (not private)
     * or a type descriptor of {@code descriptor} has {@link lombok.Setter} annotation (not private)
     * or type descriptor of {@code descriptor} has {@link lombok.Data}
     * or type descriptor of {@code descriptor} has a setter method
     * @since 0.1.0
     */
    @Override
    protected boolean isTargetField(FieldDescriptor field) {
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
        return type.hasAnnotation(Data.class) || hasSetterMethod(field);
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
     * Returns a setter for a field descriptor.
     *
     * @param field descriptor
     * @return setter for {@code field}
     * @since 0.1.1
     */
    @Override
    protected String getMethod(FieldDescriptor field) {
        return "(instance, value) -> instance." + getSetterMethod(field) + "((" + field.getTypeName() + ") value)";
    }
}
