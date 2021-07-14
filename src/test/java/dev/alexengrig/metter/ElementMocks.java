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

package dev.alexengrig.metter;

import org.mockito.Mockito;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ElementMocks {

    public static <A extends Annotation> Element annotatedElement(Class<A> type) {
        Element element = element();
        A annotation = annotationMock(type);
        when(element.getAnnotation(type)).thenReturn(annotation);
        return element;
    }

    public static Element element() {
        return mock(Element.class);
    }

    public static VariableElement fieldMock() {
        VariableElement mock = mock(VariableElement.class);
        when(mock.getKind()).thenReturn(ElementKind.FIELD);
        when(mock.accept(any(), any())).then(invocationOnMock -> {
            ElementVisitor<Object, Object> visitor = invocationOnMock.getArgument(0);
            Object parameter = invocationOnMock.getArgument(1);
            return visitor.visitVariable(mock, parameter);
        });
        return mock;
    }

    public static VariableElement fieldMock(String fieldName) {
        VariableElement mock = fieldMock();
        Name name = nameMock(fieldName);
        when(mock.getSimpleName()).thenReturn(name);
        return mock;
    }

    public static VariableElement fieldMock(Class<?> fieldType) {
        VariableElement mock = fieldMock();
        TypeMirror type = typeMirrorMock(fieldType);
        when(mock.asType()).thenReturn(type);
        return mock;
    }

    public static VariableElement fieldMock(String fieldName, Class<?> fieldType) {
        VariableElement mock = fieldMock();
        Name name = nameMock(fieldName);
        when(mock.getSimpleName()).thenReturn(name);
        TypeMirror type = typeMirrorMock(fieldType);
        when(mock.asType()).thenReturn(type);
        return mock;
    }

    public static ExecutableElement notPrivateMethodMock() {
        ExecutableElement mock = mock(ExecutableElement.class);
        when(mock.getModifiers()).thenReturn(Collections.emptySet());
        return mock;
    }

    public static ExecutableElement noParametersMethodMock() {
        ExecutableElement mock = mock(ExecutableElement.class);
        when(mock.getParameters()).thenReturn(Collections.emptyList());
        return mock;
    }

    public static ExecutableElement parameterizedMethodMock(Class<?> type) {
        ExecutableElement mock = mock(ExecutableElement.class);
        VariableElement variableElement = variableElementMock(type);
        Mockito.<List<? extends VariableElement>>when(mock.getParameters())
                .thenReturn(Collections.singletonList(variableElement));
        return mock;
    }

    @SafeVarargs
    public static <A extends Annotation> VariableElement annotatedFieldMock(
            Class<? extends A> type,
            Class<? extends A>... types) {
        VariableElement mock = fieldMock();
        Map<Class<? extends A>, ? extends A> annotationByType = Stream.concat(Stream.of(type), Arrays.stream(types))
                .collect(Collectors.toMap(Function.identity(), ElementMocks::annotationMock));
        when(mock.getAnnotation(any())).then(inv -> {
            @SuppressWarnings("unchecked")
            Class<A> annotationType = (Class<A>) inv.getArgument(0, Class.class);
            return annotationByType.get(annotationType);
        });
        return mock;
    }

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

    public static ExecutableElement executableElementMock(Class<?> returnType) {
        ExecutableElement mock = executableElementMock();
        TypeMirror type = typeMirrorMock(returnType);
        when(mock.getReturnType()).thenReturn(type);
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

    public static VariableElement variableElementMock(String fieldName, Class<?> fieldType) {
        VariableElement mock = variableElementMock();
        Name name = nameMock(fieldName);
        when(mock.getSimpleName()).thenReturn(name);
        TypeMirror typeMirror = typeMirrorMock(fieldType);
        when(mock.asType()).thenReturn(typeMirror);
        return mock;
    }

    @SafeVarargs
    public static <T extends Annotation> VariableElement variableElementMock(Class<? extends T> annotationType,
                                                                             Class<? extends T>... annotationTypes) {
        VariableElement mock = mock(VariableElement.class);
        List<AnnotationMirror> annotationMirrors =
                Stream.concat(Stream.of(annotationType), Arrays.stream(annotationTypes))
                        .map(ElementMocks::annotationMirrorMock)
                        .collect(Collectors.toList());
        Mockito.<List<? extends AnnotationMirror>>when(mock.getAnnotationMirrors()).thenReturn(annotationMirrors);
        return mock;
    }

    public static TypeElement typeElementMock() {
        return mock(TypeElement.class);
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

    public static <T> TypeElement typeElementMock(Class<? extends T> type) {
        TypeElement mock = mock(TypeElement.class);
        Name qualifiedName = nameMock(type.getName());
        when(mock.getQualifiedName()).thenReturn(qualifiedName);
        Name simpleName = nameMock(type.getSimpleName());
        when(mock.getSimpleName()).thenReturn(simpleName);
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

    public static <T extends Annotation> AnnotationMirror annotationMirrorMock(Class<? extends T> annotationType) {
        AnnotationMirror mock = mock(AnnotationMirror.class);
        DeclaredType declaredType = declaredTypeMock(annotationType);
        when(mock.getAnnotationType()).thenReturn(declaredType);
        return mock;
    }

//    Others

    public static Name nameMock(String name) {
        Name mock = mock(Name.class);
        when(mock.toString()).thenReturn(name);
        return mock;
    }

    public static <A extends Annotation> A annotationMock(Class<? extends A> type) {
        A mock = mock(type);
        //noinspection unchecked
        Mockito.<Class<? extends A>>when((Class<? extends A>) mock.annotationType()).thenReturn(type);
        return mock;
    }
}
