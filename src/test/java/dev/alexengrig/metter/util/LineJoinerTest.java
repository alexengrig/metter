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

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineJoinerTest {
    @Test
    void should_create_lines() {
        assertEquals("\n\n", new LineJoiner().ln().ln().toString(), "No two lines");
    }

    @Test
    void should_create_lines_with_text() {
        assertEquals("1\n2\n", new LineJoiner().ln("1").ln("2").toString(), "No two lines with numbers");
    }

    @Test
    void should_create_formatLines() {
        assertEquals("1\n2\n", new LineJoiner().ft("%d", 1).ft("%d", 2).toString(), "No two lines with numbers");
    }

    @Test
    void should_create_formatLines_by_condition() {
        assertEquals("1\n2\n", new LineJoiner().ftIf(true, "%d", 1).ftIf(false, "%d", 3).ftIf(true, "%d", 2).toString(),
                "No two lines with numbers");
    }

    @Test
    void should_create_lines_from_map() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>(2) {{
            put(1, "one");
            put(2, "two");
        }};
        assertEquals("1 - one\n2 - two\n", new LineJoiner().mp("%d - %s", (k, v) -> new Object[]{k, v}, map).toString(),
                "No two lines with numbers");
    }
}