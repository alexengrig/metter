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

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationDescriptor {
    protected final AnnotationMirror annotationMirror;
    protected transient String qualifiedName;

    public AnnotationDescriptor(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    static Set<AnnotationDescriptor> of(Element element) {
        return element.getAnnotationMirrors().stream()
                .map(AnnotationDescriptor::new)
                .collect(Collectors.toSet());
    }

    public String getQualifiedName() {
        if (qualifiedName == null) {
            qualifiedName = annotationMirror.getAnnotationType().toString();
        }
        return qualifiedName;
    }
}
