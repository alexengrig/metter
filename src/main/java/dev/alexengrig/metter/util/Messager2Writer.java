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

package dev.alexengrig.metter.util;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;

/**
 * An adapter of {@link javax.annotation.processing.Messager} for {@link java.io.PrintWriter}.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class Messager2Writer {
    /**
     * Messager.
     *
     * @since 0.1.0
     */
    protected final Messager messager;
    /**
     * Error print writer.
     *
     * @since 0.1.0
     */
    protected transient PrintWriter errorWriter;

    /**
     * Constructs for a messager.
     *
     * @param messager target messager for adapting
     * @since 0.1.0
     */
    public Messager2Writer(Messager messager) {
        this.messager = messager;
    }

    /**
     * Returns an error print writer.
     *
     * @return error print writer
     * @since 0.1.0
     */
    public PrintWriter errorWriter() {
        if (errorWriter == null) {
            errorWriter = new PrintWriter(new Writer() {
                @Override
                public void write(char[] cbuf, int off, int len) {
                    String message = new String(Arrays.copyOfRange(cbuf, off, off + len));
                    if (!message.equals("\r\n")) {
                        messager.printMessage(Diagnostic.Kind.ERROR, message);
                    }
                }

                @Override
                public void flush() {
                }

                @Override
                public void close() {
                }
            });
        }
        return errorWriter;
    }
}
