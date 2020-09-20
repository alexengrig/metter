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

package dev.alexengrig.metter.processor.element.descriptor;

import dev.alexengrig.metter.processor.element.collector.VariableElementCollector;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldDescriptor {
    protected final VariableElement variableElement;
    protected transient String name;
    protected transient String className;
    protected transient Set<AnnotationDescriptor> annotations;

    public FieldDescriptor(VariableElement variableElement) {
        this.variableElement = variableElement;
    }

    public static Set<FieldDescriptor> of(TypeElement typeElement) {
        VariableElementCollector fieldCollector = new VariableElementCollector(typeElement);
        return fieldCollector.getVariableElements().stream()
                .map(FieldDescriptor::new)
                .collect(Collectors.toSet());
    }

    public String getName() {
        if (name == null) {
            name = variableElement.getSimpleName().toString();
        }
        return name;
    }

    public String getClassName() {
        if (className == null) {
            className = variableElement.asType().toString();
        }
        return className;
    }

    public Set<AnnotationDescriptor> getAnnotations() {
        if (annotations == null) {
            annotations = AnnotationDescriptor.of(variableElement);
        }
        return annotations;
    }

    public boolean hasAnnotation(String annotationQualifiedName) {
        //FIXME: TypeDescriptor#hasAnnotation
        return getAnnotations().stream()
                .map(AnnotationDescriptor::getQualifiedName)
                .anyMatch(annotationQualifiedName::equals);
    }
}
