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

package dev.alexengrig.metter.element.descriptor;

import dev.alexengrig.metter.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ElementDescriptorTest {

    @Test
    void should_return_annotation() {
        Element element = ElementMocks.annotatedElement(Deprecated.class);
        ElementDescriptor<Element> descriptor = new ElementDescriptor<>(element);
        Optional<Deprecated> annotation = descriptor.getAnnotation(Deprecated.class);
        assertTrue(annotation.isPresent(), "Annotation is not present");
    }

    @Test
    void should_check_hasAnnotation() {
        Element element = ElementMocks.annotatedElement(Deprecated.class);
        ElementDescriptor<Element> descriptor = new ElementDescriptor<>(element);
        assertTrue(descriptor.hasAnnotation(Deprecated.class), "Annotation is not present");
    }
}