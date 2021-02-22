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
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class ExceptionsTest {
    @Test
    void should_throws_on_constructor() throws NoSuchMethodException {
        Constructor<Exceptions> constructor = Exceptions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalAccessException, "Exception instance not IllegalAccessException");
        assertEquals("Exceptions is utility class", cause.getMessage(), "Exception message is incorrect");
    }

    @Test
    void should_return_stackTrace() {
        Throwable throwable = mock(Throwable.class);
        Mockito.doAnswer(invocation -> {
            PrintWriter printWriter = invocation.getArgument(0, PrintWriter.class);
            printWriter.write("Some stack trace");
            return null;
        }).when(throwable).printStackTrace(any(PrintWriter.class));
        String stackTrace = Exceptions.getStackTrace(throwable);
        assertEquals("Some stack trace", stackTrace, "Stack trace is incorrect");
    }
}