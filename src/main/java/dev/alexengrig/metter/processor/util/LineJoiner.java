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

package dev.alexengrig.metter.processor.util;

import java.util.Map;
import java.util.function.BiFunction;

public class LineJoiner {
    protected static final String NL = "\n";
    protected final StringBuilder builder = new StringBuilder();

    public LineJoiner ln() {
        builder.append(NL);
        return this;
    }

    public LineJoiner ln(String line) {
        builder.append(line);
        return ln();
    }

    public LineJoiner ft(String line, Object... args) {
        return ln(String.format(line, args));
    }

    public LineJoiner ftIf(boolean condition, String line, Object... args) {
        return condition ? ln(String.format(line, args)) : this;
    }

    public <K, V> LineJoiner mp(String template,
                                BiFunction<? super K, ? super V, Object[]> mapper,
                                Map<? extends K, ? extends V> map) {
        map.forEach(mapper.andThen(args -> ft(template, args))::apply);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
