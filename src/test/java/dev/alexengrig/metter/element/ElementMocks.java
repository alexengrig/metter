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

package dev.alexengrig.metter.element;

import org.mockito.Mockito;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ElementMocks {
    public static ExecutableElement executableElementMock() {
        ExecutableElement mock = mock(ExecutableElement.class);
        when(mock.accept(any(), any())).then(invocationOnMock -> {
            ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
            Object parameter = invocationOnMock.getArgument(1);
            return visitor.visitExecutable(mock, parameter);
        });
        return mock;
    }

    public static ExecutableElement executableElementMock(String methodName) {
        ExecutableElement mock = executableElementMock();
        Name name = nameMock(methodName);
        when(mock.getSimpleName()).thenReturn(name);
        return mock;
    }

    public static VariableElement variableElementMock() {
        VariableElement mock = mock(VariableElement.class);
        when(mock.accept(any(), any())).then(invocationOnMock -> {
            ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
            Object parameter = invocationOnMock.getArgument(1);
            return visitor.visitVariable(mock, parameter);
        });
        return mock;
    }

    public static VariableElement variableElementMock(String fieldName) {
        VariableElement mock = variableElementMock();
        Name name = nameMock(fieldName);
        when(mock.getSimpleName()).thenReturn(name);
        return mock;
    }

    public static VariableElement variableElementMock(Class<?> fieldType) {
        VariableElement mock = variableElementMock();
        TypeMirror typeMirror = typeMirrorMock(fieldType);
        when(mock.asType()).thenReturn(typeMirror);
        return mock;
    }

    @SafeVarargs
    public static <T extends Annotation> VariableElement variableElementMock(Class<? extends T> type,
                                                                             Class<? extends T>... types) {
        VariableElement mock = mock(VariableElement.class);
        List<AnnotationMirror> annotationMirrors = Stream.concat(Stream.of(type), Arrays.stream(types))
                .map(ElementMocks::annotationMirrorMock)
                .collect(Collectors.toList());
        Mockito.<List<? extends AnnotationMirror>>when(mock.getAnnotationMirrors()).thenReturn(annotationMirrors);
        return mock;
    }

    public static <T extends Element> TypeElement typeElementMock(List<T> enclosedElements) {
        TypeElement mock = mock(TypeElement.class);
        when(mock.accept(any(), any())).then(invocationOnMock -> {
            ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
            Object parameter = invocationOnMock.getArgument(1);
            return visitor.visitType(mock, parameter);
        });
        @SuppressWarnings("unchecked")
        List<T> children = (List<T>) mock.getEnclosedElements();
        when(children).thenReturn(enclosedElements);
        return mock;
    }

//    Types

    public static <T> DeclaredType declaredTypeMock(Class<? extends T> type) {
        DeclaredType mock = mock(DeclaredType.class);
        when(mock.toString()).thenReturn(type.getName());
        return mock;
    }

//    Mirrors

    public static <T> TypeMirror typeMirrorMock(Class<? extends T> type) {
        TypeMirror mock = mock(TypeMirror.class);
        when(mock.toString()).thenReturn(type.getName());
        return mock;
    }

    public static <T extends Annotation> AnnotationMirror annotationMirrorMock(Class<? extends T> type) {
        AnnotationMirror mock = mock(AnnotationMirror.class);
        DeclaredType declaredType = declaredTypeMock(type);
        when(mock.getAnnotationType()).thenReturn(declaredType);
        return mock;
    }

//    Others

    public static Name nameMock(String name) {
        Name mock = mock(Name.class);
        when(mock.toString()).thenReturn(name);
        return mock;
    }
}
