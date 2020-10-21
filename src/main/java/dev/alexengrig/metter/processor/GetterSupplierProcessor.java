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
import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.MethodDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;
import dev.alexengrig.metter.generator.GetterSupplierSourceGenerator;
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
        GetterSupplier annotation = type.getAnnotation(annotationClass);
        return annotation.value();
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
        GetterSupplier annotation = type.getAnnotation(annotationClass);
        return new HashSet<>(Arrays.asList(annotation.includedFields()));
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
        GetterSupplier annotation = type.getAnnotation(annotationClass);
        return new HashSet<>(Arrays.asList(annotation.excludedFields()));
    }

    /**
     * Checks if a type descriptor has {@link lombok.Data} or {@link lombok.Getter} (not private) annotations.
     *
     * @param type descriptor
     * @return if {@code descriptor} has {@link lombok.Data} or {@link lombok.Getter} (not private) annotations
     * @since 0.1.0
     */
    protected boolean hasAllMethods(TypeDescriptor type) {
        if (type.hasAnnotation(Data.class)) {
            return true;
        } else if (type.hasAnnotation(Getter.class)) {
            return type.getAnnotation(Getter.class).value() != AccessLevel.PRIVATE;
        }
        return false;
    }

    /**
     * Returns a method name from a field descriptor:
     * for {@code boolean} type - {@code is} prefix name,
     * for other types - {@code get} prefix name.
     *
     * @param field descriptor
     * @return method name from {@code field}
     * @since 0.1.0
     */
    @Override
    protected String getMethodName(FieldDescriptor field) {
        String methodNamePrefix = "boolean".equals(field.getTypeName()) ? "is" : "get";
        String name = field.getName();
        return methodNamePrefix + name.substring(0, 1).toUpperCase() + name.substring(1);
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
     * @since 0.1.0
     */
    @Override
    protected boolean isTargetField(FieldDescriptor field) {
        if (field.hasAnnotation(Getter.class)) {
            Getter getter = field.getAnnotation(Getter.class);
            return getter.value() != AccessLevel.PRIVATE;
        }
        TypeDescriptor type = field.getParent();
        if (type.hasAnnotation(Getter.class)) {
            Getter getter = type.getAnnotation(Getter.class);
            return getter.value() != AccessLevel.PRIVATE;
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
        String methodName = getMethodName(field);
        TypeDescriptor type = field.getParent();
        if (type.hasMethod(methodName)) {
            Set<MethodDescriptor> methods = type.getMethods(methodName);
            return methods.stream().anyMatch(method -> !method.isPrivate() && method.hasNoParameters()
                    && field.getTypeName().equals(method.getTypeName()));
        }
        return false;
    }

    /**
     * Returns a method view from an qualified name of a type descriptor and a method name:
     * <pre>{@code
     * QualifiedName::methodName
     * }</pre>
     *
     * @param type       descriptor
     * @param field      descriptor
     * @param methodName method name
     * @return method view from an qualified name of {@code type} and {@code methodName}
     * @since 0.1.0
     */
    @Override
    protected String getMethodView(TypeDescriptor type, FieldDescriptor field, String methodName) {
        return type.getQualifiedName().concat("::").concat(methodName);
    }
}
