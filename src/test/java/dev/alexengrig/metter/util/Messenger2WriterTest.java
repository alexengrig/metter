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

package dev.alexengrig.metter.util;

import org.junit.jupiter.api.Test;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class Messenger2WriterTest {
    Messager messager = mock(Messager.class);
    Messenger2Writer messenger2Writer = new Messenger2Writer(messager);

    @Test
    void should_print_errorMessage() {
        PrintWriter writer = messenger2Writer.errorWriter();
        writer.println("Error message");
        verify(messager).printMessage(Diagnostic.Kind.ERROR, "Error message");
        // coverage
        writer.flush();
        writer.close();
    }
}