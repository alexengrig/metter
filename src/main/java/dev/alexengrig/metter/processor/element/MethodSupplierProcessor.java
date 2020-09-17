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

package dev.alexengrig.metter.processor.element;

import dev.alexengrig.metter.processor.BaseProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A supplier processor for methods.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @see dev.alexengrig.metter.processor.BaseProcessor
 * @since 0.1.0
 */
public abstract class MethodSupplierProcessor extends BaseProcessor {
    public MethodSupplierProcessor(Class<? extends Annotation> annotation) {
        super(annotation);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                MethodSupplierClassVisitor methodSupplierClassVisitor = getMethodSupplierClassVisitor();
                element.accept(methodSupplierClassVisitor, null);
                String className = methodSupplierClassVisitor.getClassName();
                String sourceClassName = methodSupplierClassVisitor.getSourceClassName();
                Map<String, String> field2Method = methodSupplierClassVisitor.getField2Method();
                JavaFileObject sourceFile = createSourceFile(sourceClassName);
                try (PrintWriter sourcePrinter = new PrintWriter(sourceFile.openWriter())) {
                    String source = generateSource(sourceClassName, className, field2Method);
                    sourcePrinter.println(source);
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }
        return true;
    }

    protected abstract MethodSupplierClassVisitor getMethodSupplierClassVisitor();

    protected JavaFileObject createSourceFile(String className) {
        try {
            return processingEnv.getFiler().createSourceFile(className);
        } catch (IOException e) {
            throw new IllegalArgumentException(className);
        }
    }

    protected abstract String generateSource(String className, String domainClassName,
                                             Map<String, String> field2Method);

    protected String getPackageName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return className.substring(0, lastIndexOfDot);
        }
        return null;
    }

    protected String getSimpleName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        return className.substring(lastIndexOfDot + 1);
    }

    protected abstract class MethodSupplierClassVisitor extends BaseElementVisitor {
        protected String className;
        protected String sourceClassName;
        protected Map<String, String> field2Method;

        public String getClassName() {
            return className;
        }

        public String getSourceClassName() {
            return sourceClassName;
        }

        public Map<String, String> getField2Method() {
            return field2Method;
        }

        @Override
        public void visitType(TypeElement typeElement) {
            this.className = typeElement.getQualifiedName().toString();
            this.sourceClassName = getSourceClassName(typeElement);
            Field2MethodVisitor field2MethodVisitor = getField2MethodVisitor(typeElement);
            for (Element element : typeElement.getEnclosedElements()) {
                element.accept(field2MethodVisitor, null);
            }
            this.field2Method = field2MethodVisitor.getField2Method();
        }

        protected String getSourceClassName(TypeElement typeElement) {
            String customName = customSourceClassName(typeElement);
            if (customName.isEmpty()) {
                return defaultSourceClassName(className);
            }
            return customName;
        }

        protected String customSourceClassName(TypeElement typeElement) {
            String name = customClassName(typeElement);
            if (name.isEmpty()) {
                return name;
            }
            String packageName = getPackageName(className);
            if (packageName != null) {
                return packageName.concat(".").concat(name);
            }
            return name;
        }

        protected abstract String customClassName(TypeElement typeElement);

        protected abstract String defaultSourceClassName(String className);

        protected abstract Field2MethodVisitor getField2MethodVisitor(TypeElement typeElement);

        protected abstract class Field2MethodVisitor extends BaseElementVisitor {
            protected final Map<String, String> field2Type = new HashMap<>();
            protected final Set<String> methods = new HashSet<>();
            protected final Set<String> includedFieldNames;
            protected final Set<String> excludedFieldNames;

            protected Field2MethodVisitor(Set<String> includedFieldNames, Set<String> excludedFieldNames) {
                this.includedFieldNames = includedFieldNames;
                this.excludedFieldNames = excludedFieldNames;
            }

            public Map<String, String> getField2Method() {
                Map<String, String> map = new HashMap<>();
                for (String method : methods) {
                    String field = getFieldFromMethod(method);
                    if (field2Type.containsKey(field)) {
                        map.put(field, getMethodForField(field, method));
                    }
                }
                return map;
            }

            protected abstract String getFieldFromMethod(String method);

            protected abstract String getMethodForField(String field, String method);

            @Override
            public void visitVariable(VariableElement variableElement) {
                if (isTargetField(variableElement)) {
                    String name = variableElement.getSimpleName().toString();
                    String type = variableElement.asType().toString();
                    field2Type.put(name, type);
                }
            }

            protected boolean isTargetField(VariableElement variableElement) {
                String name = variableElement.getSimpleName().toString();
                if (!includedFieldNames.isEmpty()) {
                    return includedFieldNames.contains(name);
                } else if (!excludedFieldNames.isEmpty()) {
                    return !excludedFieldNames.contains(name);
                }
                return true;
            }

            @Override
            public void visitExecutable(ExecutableElement executableElement) {
                if (isTargetMethod(executableElement)) {
                    methods.add(executableElement.getSimpleName().toString());
                }
            }

            protected abstract boolean isTargetMethod(ExecutableElement executableElement);
        }
    }
}
