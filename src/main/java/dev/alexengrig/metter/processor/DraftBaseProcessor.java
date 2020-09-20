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

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public abstract class DraftBaseProcessor<A extends Annotation, E extends Element> extends AbstractProcessor {
    protected final Class<? extends A> annotationClass;

    public DraftBaseProcessor(Class<? extends A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotationClass);
        for (Element annotatedElement : annotatedElements) {
            @SuppressWarnings("unchecked")
            E element = (E) annotatedElement;
            process(element);
        }
        return true;
    }

    protected abstract void process(E annotatedElement);

    protected void note(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    protected void error(String message, Throwable throwable) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
        throwable.printStackTrace();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(annotationClass.getName());
    }
}
