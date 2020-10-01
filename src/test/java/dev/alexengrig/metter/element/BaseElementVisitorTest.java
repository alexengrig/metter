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

import org.junit.jupiter.api.Test;

class BaseElementVisitorTest {
    @Test
    void should_do_nothing() {
        BaseElementVisitor visitor = new BaseElementVisitor();
        visitor.visitElement(null);
        visitor.visitPackage(null);
        visitor.visitPackage(null, null);
        visitor.visitType(null);
        visitor.visitType(null, null);
        visitor.visitVariable(null);
        visitor.visitVariable(null, null);
        visitor.visitExecutable(null);
        visitor.visitExecutable(null, null);
        visitor.visitTypeParameter(null);
        visitor.visitTypeParameter(null, null);
        visitor.visitUnknown(null);
        visitor.visitUnknown(null, null);
        visitor.visit(null);
        visitor.visit(null, null);
        visitor.visitPackage(null);
        visitor.visitPackage(null, null);
        visitor.visitType(null);
        visitor.visitType(null, null);
        visitor.visitVariable(null);
        visitor.visitVariable(null, null);
        visitor.visitExecutable(null);
        visitor.visitExecutable(null, null);
        visitor.visitTypeParameter(null);
        visitor.visitTypeParameter(null, null);
        visitor.visitUnknown(null);
        visitor.visitUnknown(null, null);
    }
}