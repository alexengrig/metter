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
import dev.alexengrig.metter.generator.MethodSupplierSourceGenerator;

import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseMethodSupplierProcessor<A extends Annotation> extends BaseProcessor<A, TypeElement> {
    protected final MethodSupplierSourceGenerator sourceGenerator;

    public BaseMethodSupplierProcessor(Class<? extends A> annotationClass) {
        super(annotationClass);
        this.sourceGenerator = getSourceGenerator();
    }

    protected abstract MethodSupplierSourceGenerator getSourceGenerator();

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
        return getClassName(type).orElseGet(() -> getDefaultClassName(type));
    }

    protected Optional<String> getClassName(TypeDescriptor type) {
        String customClassName = getCustomClassName(type);
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

    protected abstract String getCustomClassName(TypeDescriptor type);

    protected String getDefaultClassName(TypeDescriptor type) {
        String className = type.getQualifiedName();
        return className + annotationClass.getSimpleName();
    }

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception of source file creation for:" + className, e);
        }
    }

    protected Map<String, String> createField2MethodMap(TypeDescriptor type) {
        Map<String, String> field2Method = new HashMap<>();
        Set<FieldDescriptor> fields = getFields(type);
        boolean hasAllMethods = hasAllMethods(type);
        for (FieldDescriptor field : fields) {
            String fieldName = field.getName();
            String methodName = getMethodName(field);
            if (isTargetField(field) || hasAllMethods || type.hasMethod(methodName)) {
                field2Method.put(fieldName, getMethodView(type, field, methodName));
            }
        }
        return field2Method;
    }

    protected Set<FieldDescriptor> getFields(TypeDescriptor type) {
        Set<FieldDescriptor> fields = type.getFields();
        Set<String> includedFields = getIncludedFields(type);
        Set<String> excludedFields = getExcludedFields(type);
        if (includedFields.isEmpty() && excludedFields.isEmpty()) {
            return fields;
        }
        if (!includedFields.isEmpty()) {
            return fields.stream().filter(f -> includedFields.contains(f.getName())).collect(Collectors.toSet());
        } else {
            return fields.stream().filter(f -> !excludedFields.contains(f.getName())).collect(Collectors.toSet());
        }
    }

    protected abstract Set<String> getIncludedFields(TypeDescriptor type);

    protected abstract Set<String> getExcludedFields(TypeDescriptor type);

    protected abstract boolean hasAllMethods(TypeDescriptor type);

    protected abstract String getMethodName(FieldDescriptor field);

    protected abstract boolean isTargetField(FieldDescriptor field);

    protected abstract String getMethodView(TypeDescriptor type, FieldDescriptor field, String methodName);

    protected String createSource(TypeDescriptor type, Map<String, String> field2Method, String sourceClassName) {
        return sourceGenerator.generate(sourceClassName, type.getQualifiedName(), field2Method);
    }

    protected void writeSourceFile(JavaFileObject sourceFile, String source) {
        try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
            sourcePrinter.print(source);
        } catch (IOException e) {
            error("Exception of source file writing", e);
        }
    }
}
