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

package dev.alexengrig.metter.exception;

/**
 * Metter exception.
 *
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.1
 */
public class MetterException extends RuntimeException {
    /**
     * Constructs with the detail message.
     *
     * @param message the detail message
     * @since 0.1.1
     */
    public MetterException(String message) {
        super(message);
    }

    /**
     * Constructs with the detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     * @since 0.1.1
     */
    public MetterException(String message, Throwable cause) {
        super(message, cause);
    }
}
