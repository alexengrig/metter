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

package dev.alexengrig.metter.element.descriptor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A descriptor of annotation.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class AnnotationDescriptor {
    /**
     * Annotation mirror.
     *
     * @since 0.1.0
     */
    protected final AnnotationMirror annotationMirror;
    /**
     * Qualified name.
     *
     * @since 0.1.0
     */
    protected transient String qualifiedName;

    /**
     * Constructs with an annotation mirror.
     *
     * @param annotationMirror annotation mirror
     * @since 0.1.0
     */
    public AnnotationDescriptor(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    /**
     * Creates a set from an element.
     *
     * @param element element
     * @return set from {@code element}
     * @since 0.1.0
     */
    public static Set<AnnotationDescriptor> of(Element element) {
        return element.getAnnotationMirrors().stream()
                .map(AnnotationDescriptor::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an qualified name.
     *
     * @return qualified name
     * @since 0.1.0
     */
    public String getQualifiedName() {
        if (qualifiedName == null) {
            qualifiedName = annotationMirror.getAnnotationType().toString();
        }
        return qualifiedName;
    }
}
