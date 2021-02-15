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

/**
 * Utility class for {@link java.lang.String}.
 *
 * @author Grig Alex
 * @version 0.1.1
 * @since 0.1.1
 */
public final class Strings {
    /**
     * Private constructor.
     *
     * @throws IllegalAccessException on call
     * @since 0.1.1
     */
    private Strings() throws IllegalAccessException {
        throw new IllegalAccessException("Strings is utility class");
    }

    /**
     * Capitalize string.
     *
     * @param string string
     * @return capitalized {@code string}
     */
    public static String capitalize(String string) {
        if (string == null) {
            return null;
        } else if (string.length() < 2) {
            return string.toUpperCase();
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
