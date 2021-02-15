/*
 * Copyright 2021 Alexengrig Dev.
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

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * An element descriptor.
 *
 * @param <E>
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.1
 */
public class ElementDescriptor<E extends Element> {
    /**
     * Element.
     *
     * @since 0.1.1
     */
    protected final E element;

    /**
     * Constructs with an element.
     *
     * @param element element
     * @since 0.1.1
     */
    public ElementDescriptor(E element) {
        this.element = Objects.requireNonNull(element, "Element must not be null");
    }

    /**
     * Check if has an annotation by an annotation type.
     *
     * @param annotationType annotation type
     * @param <A>            type of annotation
     * @return if has an annotation by {@code annotationType}
     * @since 0.1.1
     */
    public <A extends Annotation> boolean hasAnnotation(Class<? extends A> annotationType) {
        return getAnnotation(annotationType).isPresent();
    }

    /**
     * Returns {@link java.util.Optional} with an annotation by a annotation type.
     *
     * @param annotationType annotation type
     * @param <A>            type of annotation
     * @return {@link java.util.Optional} with an annotation by a annotation type.
     * @since 0.1.1
     */
    public <A extends Annotation> Optional<A> getAnnotation(Class<? extends A> annotationType) {
        return Optional.ofNullable(element.getAnnotation(annotationType));
    }
}
