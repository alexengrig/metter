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
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class DraftGetterSupplierProcessor extends DraftBaseProcessor<GetterSupplier, TypeElement> {
    public DraftGetterSupplierProcessor() {
        super(GetterSupplier.class);
    }

    @Override
    protected void process(TypeElement typeElement) {
        GetterSupplier annotation = typeElement.getAnnotation(annotationClass);
        Set<String> includedFields = new HashSet<>(Arrays.asList(annotation.includedFields()));
        Set<String> excludedFields = new HashSet<>(Arrays.asList(annotation.excludedFields()));
        TypeDescriptor type = new TypeDescriptor(typeElement);
        String className = type.getQualifiedName();
        Map<Object, Object> field2Getter = new HashMap<>();
        boolean hasAllGetters = type.hasAnnotation("lombok.Data") || type.hasAnnotation("lombok.Getter");
        for (FieldDescriptor field : type.getFields()) {
            String name = field.getName();
            if ((!includedFields.isEmpty() && includedFields.contains(name))
                    || (!excludedFields.isEmpty() && !excludedFields.contains(name))) {
                String getter = field.getGetterMethodName();
                if (hasAllGetters || field.hasAnnotation("lombok.Getter") || type.hasMethod(getter)) {
                    field2Getter.put(name, className.concat("::").concat(getter));
                }
            }
        }

        String sourceClassName = className + "GetterSupplier";
        JavaFileObject sourceFile = createSourceFile(sourceClassName);
        try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
            GetterSupplierSourceGenerator sourceGenerator = new GetterSupplierSourceGenerator();
            String source = sourceGenerator.generate(sourceClassName, className, field2Getter);
            sourcePrinter.println(source);
        } catch (IOException e) {
            error("Exception of source file writing", e);
        }
    }

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException("Exception of source file creation for:" + className);
        }
    }
}
