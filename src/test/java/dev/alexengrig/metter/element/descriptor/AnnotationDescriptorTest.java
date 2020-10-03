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

import dev.alexengrig.metter.element.ElementMocks;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.AnnotationMirror;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class AnnotationDescriptorTest {
    @Test
    void should_return_qualifiedName() {
        AnnotationMirror annotationMirror = ElementMocks.annotationMirrorMock(Deprecated.class);
        AnnotationDescriptor descriptor = new AnnotationDescriptor(annotationMirror);
        assertEquals("java.lang.Deprecated", descriptor.getQualifiedName(),
                "Annotation qualified name does not equal to 'java.lang.Deprecated'");
        descriptor.getQualifiedName();
        verify(annotationMirror).getAnnotationType();
    }
}