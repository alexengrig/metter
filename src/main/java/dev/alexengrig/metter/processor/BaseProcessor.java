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
import javax.lang.model.SourceVersion;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public abstract class BaseProcessor extends AbstractProcessor {
    protected final Set<String> supportedAnnotationTypes;

    protected BaseProcessor(Class<? extends Annotation> annotation) {
        this(Collections.singleton(annotation.getName()));
    }

    protected BaseProcessor(Set<String> annotationTypes) {
        this.supportedAnnotationTypes = annotationTypes;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}
