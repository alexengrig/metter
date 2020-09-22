package dev.alexengrig.metter.util;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LineJoinerTest {
    @Test
    public void should_create_lines() {
        assertEquals("No two lines",
                "\n\n", new LineJoiner().ln().ln().toString());
    }

    @Test
    public void should_create_lines_with_text() {
        assertEquals("No two lines with numbers",
                "1\n2\n", new LineJoiner().ln("1").ln("2").toString());
    }

    @Test
    public void should_create_formatLines() {
        assertEquals("No two lines with numbers",
                "1\n2\n", new LineJoiner().ft("%d", 1).ft("%d", 2).toString());
    }

    @Test
    public void should_create_formatLines_by_condition() {
        assertEquals("No two lines with numbers",
                "1\n2\n", new LineJoiner().ftIf(true, "%d", 1).ftIf(false, "%d", 3).ftIf(true, "%d", 2).toString());
    }

    @Test
    public void should_create_lines_from_map() {
        Map<Integer, String> map = new LinkedHashMap<Integer, String>(2) {{
            put(1, "one");
            put(2, "two");
        }};
        assertEquals("No two lines with numbers",
                "1 - one\n2 - two\n", new LineJoiner().mp("%d - %s", (k, v) -> new Object[]{k, v}, map).toString());
    }
}