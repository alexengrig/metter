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

package dev.alexengrig.metter.generator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MethodSupplierSourceGeneratorTest {
    private static final String SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE;
    private static final String SNAPSHOT_OF_SOURCE_WITH_PACKAGE;

    static {
        SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE = "" +
                "@javax.annotation.Generated(\n" +
                "        value = \"dev.alexengrig.metter.generator.MethodSupplierSourceGeneratorTest$1\")\n" +
                "public class MyClass implements\n" +
                "        java.util.function.Supplier<\n" +
                "                java.util.Map<\n" +
                "                        java.lang.String,\n" +
                "                        Object\n" +
                "                        >> {\n" +
                "    protected final java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            Object\n" +
                "            > getterByField;\n" +
                "    public MyClass() {\n" +
                "        this.getterByField = createMap();\n" +
                "    }\n" +
                "\n" +
                "    protected java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            Object\n" +
                "            > createMap() {\n" +
                "        java.util.HashMap<\n" +
                "                java.lang.String,\n" +
                "                Object\n" +
                "                > map = new java.util.HashMap<>(1);\n" +
                "        map.put(\"nullField\",\n" +
                "                null);\n" +
                "        return map;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            Object\n" +
                "            > get() {\n" +
                "        return getterByField;\n" +
                "    }\n" +
                "}\n";
        SNAPSHOT_OF_SOURCE_WITH_PACKAGE = "" +
                "package my.company;\n" +
                "\n" +
                "@javax.annotation.Generated(\n" +
                "        value = \"dev.alexengrig.metter.generator.MethodSupplierSourceGeneratorTest$1\")\n" +
                "public class MyClass implements\n" +
                "        java.util.function.Supplier<\n" +
                "                java.util.Map<\n" +
                "                        java.lang.String,\n" +
                "                        Object\n" +
                "                        >> {\n" +
                "    protected final java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            Object\n" +
                "            > getterByField;\n" +
                "    public MyClass() {\n" +
                "        this.getterByField = createMap();\n" +
                "    }\n" +
                "\n" +
                "    protected java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            Object\n" +
                "            > createMap() {\n" +
                "        java.util.HashMap<\n" +
                "                java.lang.String,\n" +
                "                Object\n" +
                "                > map = new java.util.HashMap<>(3);\n" +
                "        map.put(\"booleanField\",\n" +
                "                my.company.MyDomain::isBooleanField);\n" +
                "        map.put(\"intField\",\n" +
                "                MyDomain::getIntField);\n" +
                "        map.put(\"stringField\",\n" +
                "                my.company.MyDomain::getStringField);\n" +
                "        return map;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            Object\n" +
                "            > get() {\n" +
                "        return getterByField;\n" +
                "    }\n" +
                "}\n";
    }

    private final MethodSupplierSourceGenerator generator = new MethodSupplierSourceGenerator(false) {
        @Override
        protected String getMapValueType(String className) {
            return "Object";
        }
    };

    @Test
    public void should_generate_source_withoutPackage() {
        String className = "MyClass";
        String domainClassName = "MyDomain";
        Map<String, String> field2Method = new HashMap<String, String>() {{
            put("nullField", "null");
        }};
        String source = generator.generate(className, domainClassName, field2Method);
        assertEquals(SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE, source, "Source is invalid");
    }

    @Test
    public void should_generate_sourceWithPackage() {
        String className = "my.company.MyClass";
        String domainClassName = "my.company.MyDomain";
        Map<String, String> field2Method = new HashMap<String, String>() {{
            put("intField", "MyDomain::getIntField");
            put("booleanField", "my.company.MyDomain::isBooleanField");
            put("stringField", "my.company.MyDomain::getStringField");
        }};
        String source = generator.generate(className, domainClassName, field2Method);
        assertEquals(SNAPSHOT_OF_SOURCE_WITH_PACKAGE, source, "Source is invalid");
    }

    @Test
    public void should_return_packageName() {
        assertEquals("my.company", generator.getPackageName("my.company.MyClass"),
                "Package name is not equal 'my.company'");
        assertNull(generator.getPackageName("MyClass"), "Package name is not null");
    }

    @Test
    public void should_return_simpleName() {
        assertEquals("MyClass", generator.getSimpleName("my.company.MyClass"),
                "Simple name is not equal 'MyClass'");
        assertEquals("MyClass", generator.getSimpleName("MyClass"),
                "Simple name is not equal 'MyClass'");
    }
}