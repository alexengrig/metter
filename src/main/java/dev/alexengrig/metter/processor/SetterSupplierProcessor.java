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
import dev.alexengrig.metter.annotation.SetterSupplier;
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.generator.SetterSupplierSourceGenerator;
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
public class SetterSupplierProcessor extends BaseMethodSupplierProcessor<SetterSupplier> {
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
     * @param type descriptor
     * @return custom class name from {@link dev.alexengrig.metter.annotation.SetterSupplier#value()}
     * @since 0.1.0
     */
    @Override
    protected String getCustomClassName(TypeDescriptor type) {
        SetterSupplier annotation = type.getAnnotation(annotationClass);
        return annotation.value();
    }

    /**
     * Returns included fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#includedFields()}.
     *
     * @param type descriptor
     * @return included fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#includedFields()}
     * @since 0.1.0
     */
    @Override
    protected Set<String> getIncludedFields(TypeDescriptor type) {
        SetterSupplier annotation = type.getAnnotation(annotationClass);
        return new HashSet<>(Arrays.asList(annotation.includedFields()));
    }

    /**
     * Returns excluded fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#excludedFields()}.
     *
     * @param type descriptor
     * @return excluded fields from {@link dev.alexengrig.metter.annotation.SetterSupplier#excludedFields()}
     * @since 0.1.0
     */
    @Override
    protected Set<String> getExcludedFields(TypeDescriptor type) {
        SetterSupplier annotation = type.getAnnotation(annotationClass);
        return new HashSet<>(Arrays.asList(annotation.excludedFields()));
    }

    /**
     * Checks if a type descriptor has {@code lombok.Data} or {@code lombok.Setter} annotations.
     *
     * @param type descriptor
     * @return if a type descriptor has {@code lombok.Data} or {@code lombok.Setter} annotations
     * @since 0.1.0
     */
    protected boolean hasAllMethods(TypeDescriptor type) {
        return type.hasAnnotation("lombok.Data") || type.hasAnnotation("lombok.Setter");
    }

    /**
     * Returns a method name from a field descriptor: {@code set} prefix name.
     *
     * @param field descriptor
     * @return method name from {@code field}
     * @since 0.1.0
     */
    @Override
    protected String getMethodName(FieldDescriptor field) {
        String name = field.getName();
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
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
            Setter setter = field.getAnnotation(Setter.class);
            return setter.value() != AccessLevel.PRIVATE;
        }
        TypeDescriptor type = field.getParent();
        if (type.hasAnnotation(Setter.class)) {
            Setter setter = type.getAnnotation(Setter.class);
            return setter.value() != AccessLevel.PRIVATE;
        }
        return type.hasAnnotation(Data.class) || hasSetterMethod(field);
    }

    /**
     * Checks if a type descriptor of a field descriptor has a setter method
     *
     * @param field descriptor
     * @return if a type descriptor of a field descriptor has a setter method
     */
    protected boolean hasSetterMethod(FieldDescriptor field) {
        String methodName = getMethodName(field);
        TypeDescriptor type = field.getParent();
        if (type.hasMethod(methodName)) {
            Set<MethodDescriptor> methods = type.getMethods(methodName);
            return methods.stream().anyMatch(method -> !method.isPrivate() && "void".equals(method.getTypeName())
                    && method.hasOnlyOneParameter(field.getTypeName()));
        }
        return false;
    }

    /**
     * Returns a method view from a type name of a field descriptor and a method name:
     * <pre>{@code
     * (instance, value) -> instance.methodName((FieldType) value)
     * }</pre>
     *
     * @param type       descriptor
     * @param field      descriptor
     * @param methodName method name
     * @return method view from a type name of {@code field} and {@code methodName}
     * @since 0.1.0
     */
    @Override
    protected String getMethodView(TypeDescriptor type, FieldDescriptor field, String methodName) {
        return String.format("(instance, value) -> instance.%s((%s) value)",
                methodName, field.getTypeName());
    }
}
