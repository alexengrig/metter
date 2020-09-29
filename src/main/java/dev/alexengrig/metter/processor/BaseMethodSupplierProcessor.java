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

import dev.alexengrig.metter.element.descriptor.FieldDescriptor;
import dev.alexengrig.metter.element.descriptor.TypeDescriptor;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class BaseMethodSupplierProcessor<A extends Annotation> extends BaseProcessor<A, TypeElement> {
    public BaseMethodSupplierProcessor(Class<? extends A> annotationClass) {
        super(annotationClass);
    }

    @Override
    protected void process(TypeElement typeElement) {
        TypeDescriptor type = new TypeDescriptor(typeElement);
        String sourceClassName = createSourceClassName(type);
        JavaFileObject sourceFile = createSourceFile(sourceClassName);
        Map<String, String> field2Method = createField2MethodMap(type);
        String source = createSource(type, field2Method, sourceClassName);
        writeSourceFile(sourceFile, source);
    }

    protected String createSourceClassName(TypeDescriptor type) {
        return getCustomClassName(type).orElseGet(() -> getDefaultClassName(type));
    }

    protected Optional<String> getCustomClassName(TypeDescriptor type) {
        String customClassName = getCustomClassNameFromAnnotation(type);
        if (customClassName.isEmpty()) {
            return Optional.empty();
        }
        String packageName = "";
        String className = type.getQualifiedName();
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            packageName = className.substring(0, lastIndexOfDot);
        }
        return Optional.of(packageName.concat(".").concat(customClassName));
    }

    protected abstract String getCustomClassNameFromAnnotation(TypeDescriptor type);

    protected String getDefaultClassName(TypeDescriptor type) {
        String className = type.getQualifiedName();
        return className + annotationClass.getSimpleName();
    }

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception of source file creation for:" + className);
        }
    }

    protected Map<String, String> createField2MethodMap(TypeDescriptor type) {
        Map<String, String> field2Method = new HashMap<>();
        Set<String> includedFields = getIncludedFields(type);
        Set<String> excludedFields = getExcludedFields(type);
        boolean hasAllMethods = hasAllMethods(type);
        for (FieldDescriptor field : type.getFields()) {
            String fieldName = field.getName();
            if ((!includedFields.isEmpty() && includedFields.contains(fieldName))
                    || (!excludedFields.isEmpty() && !excludedFields.contains(fieldName))) {
                String methodName = getMethodName(field);
                if (isTargetField(field) || hasAllMethods || type.hasMethod(methodName)) {
                    field2Method.put(fieldName, getMethodView(type, field, methodName));
                }
            }
        }
        return field2Method;
    }

    protected abstract Set<String> getIncludedFields(TypeDescriptor type);

    protected abstract Set<String> getExcludedFields(TypeDescriptor type);

    protected abstract boolean hasAllMethods(TypeDescriptor type);

    protected abstract String getMethodName(FieldDescriptor field);

    protected abstract boolean isTargetField(FieldDescriptor field);

    protected abstract String getMethodView(TypeDescriptor type, FieldDescriptor field, String methodName);

    protected abstract String createSource(TypeDescriptor type,
                                           Map<String, String> field2Method,
                                           String sourceClassName);

    protected void writeSourceFile(JavaFileObject sourceFile, String source) {
        try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
            sourcePrinter.print(source);
        } catch (IOException e) {
            error("Exception of source file writing", e);
        }
    }
}
