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

package dev.alexengrig.metter.generator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetterSupplierSourceGeneratorTest {
    static final String SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE;
    static final String SNAPSHOT_OF_SOURCE_WITH_PACKAGE;

    static {
        SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE = "" +
                "/**\n" +
                " * Getters supplier of {@link MyDomain}.\n" +
                " */\n" +
                "@javax.annotation.Generated(\n" +
                "        value = \"dev.alexengrig.metter.generator.GetterSupplierSourceGenerator\")\n" +
                "public class MyClass implements\n" +
                "        java.util.function.Supplier<\n" +
                "                java.util.Map<\n" +
                "                        java.lang.String,\n" +
                "                        java.util.function.Function<MyDomain, java.lang.Object>\n" +
                "                        >> {\n" +
                "\n" +
                "    /**\n" +
                "     * Map, getter function by field name.\n" +
                "     */\n" +
                "    protected final java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.Function<MyDomain, java.lang.Object>\n" +
                "            > getterByField;\n" +
                "\n" +
                "    /**\n" +
                "     * Constructs this.\n" +
                "     */\n" +
                "    public MyClass() {\n" +
                "        this.getterByField = createMap();\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Creates map, getter function by field name.\n" +
                "     *\n" +
                "     * @return map, getter function by field name\n" +
                "     */\n" +
                "    protected java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.Function<MyDomain, java.lang.Object>\n" +
                "            > createMap() {\n" +
                "        java.util.HashMap<\n" +
                "                java.lang.String,\n" +
                "                java.util.function.Function<MyDomain, java.lang.Object>\n" +
                "                > map = new java.util.HashMap<>(3);\n" +
                "        map.put(\"booleanField\",\n" +
                "                MyDomain::isBooleanField);\n" +
                "        map.put(\"intField\",\n" +
                "                MyDomain::getIntField);\n" +
                "        map.put(\"stringField\",\n" +
                "                MyDomain::getStringField);\n" +
                "        return map;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Returns map, getter function by field name.\n" +
                "     *\n" +
                "     * @return map, getter function by field name\n" +
                "     */\n" +
                "    @Override\n" +
                "    public java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.Function<MyDomain, java.lang.Object>\n" +
                "            > get() {\n" +
                "        return getterByField;\n" +
                "    }\n" +
                "}\n";
        SNAPSHOT_OF_SOURCE_WITH_PACKAGE = "" +
                "package my.company;\n" +
                "\n" +
                "/**\n" +
                " * Getters supplier of {@link my.company.MyDomain}.\n" +
                " */\n" +
                "@javax.annotation.Generated(\n" +
                "        value = \"dev.alexengrig.metter.generator.GetterSupplierSourceGenerator\")\n" +
                "public class MyClass implements\n" +
                "        java.util.function.Supplier<\n" +
                "                java.util.Map<\n" +
                "                        java.lang.String,\n" +
                "                        java.util.function.Function<my.company.MyDomain, java.lang.Object>\n" +
                "                        >> {\n" +
                "\n" +
                "    /**\n" +
                "     * Map, getter function by field name.\n" +
                "     */\n" +
                "    protected final java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.Function<my.company.MyDomain, java.lang.Object>\n" +
                "            > getterByField;\n" +
                "\n" +
                "    /**\n" +
                "     * Constructs this.\n" +
                "     */\n" +
                "    public MyClass() {\n" +
                "        this.getterByField = createMap();\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Creates map, getter function by field name.\n" +
                "     *\n" +
                "     * @return map, getter function by field name\n" +
                "     */\n" +
                "    protected java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.Function<my.company.MyDomain, java.lang.Object>\n" +
                "            > createMap() {\n" +
                "        java.util.HashMap<\n" +
                "                java.lang.String,\n" +
                "                java.util.function.Function<my.company.MyDomain, java.lang.Object>\n" +
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
                "    /**\n" +
                "     * Returns map, getter function by field name.\n" +
                "     *\n" +
                "     * @return map, getter function by field name\n" +
                "     */\n" +
                "    @Override\n" +
                "    public java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.Function<my.company.MyDomain, java.lang.Object>\n" +
                "            > get() {\n" +
                "        return getterByField;\n" +
                "    }\n" +
                "}\n";
    }

    final GetterSupplierSourceGenerator generator = new GetterSupplierSourceGenerator(false);

    @Test
    void should_create_instance() {
        // coverage
        GetterSupplierSourceGenerator generator = new GetterSupplierSourceGenerator();
        assertNotNull(generator);
    }

    @Test
    void should_generate_sourceWithoutPackage() {
        String className = "MyClass";
        String domainClassName = "MyDomain";
        Map<String, String> field2Getter = new HashMap<String, String>() {{
            put("intField", "MyDomain::getIntField");
            put("booleanField", "MyDomain::isBooleanField");
            put("stringField", "MyDomain::getStringField");
        }};
        String source = generator.generate(className, domainClassName, field2Getter);
        assertEquals(SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE, source, "Source is invalid");
    }

    @Test
    void should_generate_sourceWithPackage() {
        String className = "my.company.MyClass";
        String domainClassName = "my.company.MyDomain";
        Map<String, String> field2Getter = new HashMap<String, String>() {{
            put("intField", "MyDomain::getIntField");
            put("booleanField", "my.company.MyDomain::isBooleanField");
            put("stringField", "my.company.MyDomain::getStringField");
        }};
        String source = generator.generate(className, domainClassName, field2Getter);
        assertEquals(SNAPSHOT_OF_SOURCE_WITH_PACKAGE, source, "Source is invalid");
    }
}