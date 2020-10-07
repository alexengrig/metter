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

import java.util.Map;
import java.util.function.BiFunction;

/**
 * An useful wrapper over {@link java.lang.StringBuilder}.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class LineJoiner {
    /**
     * New line.
     *
     * @since 0.1.0
     */
    protected static final String NL = "\n";
    /**
     * Builder.
     *
     * @since 0.1.0
     */
    protected final StringBuilder builder = new StringBuilder();

    /**
     * Appends a new line.
     *
     * @return this
     * @since 0.1.0
     */
    public LineJoiner ln() {
        builder.append(NL);
        return this;
    }

    /**
     * Appends a text and a new line.
     *
     * @param line text
     * @return this
     * @since 0.1.0
     */
    public LineJoiner ln(String line) {
        builder.append(line);
        return ln();
    }

    /**
     * Appends a formatted text and a new line.
     *
     * @param line text
     * @param args arguments for {@link java.lang.String#format(String, Object...)}
     * @return this
     * @since 0.1.0
     */
    public LineJoiner ft(String line, Object... args) {
        return ln(String.format(line, args));
    }

    /**
     * Appends a formatted text and new line if condition is truth.
     *
     * @param condition appends or not appends
     * @param line      text
     * @param args      arguments for {@link java.lang.String#format(String, Object...)}
     * @return this
     * @since 0.1.0
     */
    public LineJoiner ftIf(boolean condition, String line, Object... args) {
        return condition ? ln(String.format(line, args)) : this;
    }

    /**
     * Appends a map via function and {@link #ft(String, Object...)}.
     *
     * @param template template for formatting
     * @param mapper   function for getting arguments for {@link java.lang.String#format(String, Object...)}
     * @param map      map with values
     * @param <K>      type of map key
     * @param <V>      type of map value
     * @return this
     * @since 0.1.0
     */
    public <K, V> LineJoiner mp(String template,
                                BiFunction<? super K, ? super V, Object[]> mapper,
                                Map<? extends K, ? extends V> map) {
        map.forEach(mapper.andThen(args -> ft(template, args))::apply);
        return this;
    }

    /**
     * Returns a result string.
     *
     * @return result string
     * @since 0.1.0
     */
    @Override
    public String toString() {
        return builder.toString();
    }
}
