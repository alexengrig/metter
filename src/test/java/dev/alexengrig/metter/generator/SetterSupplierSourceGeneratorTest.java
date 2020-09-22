package dev.alexengrig.metter.generator;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SetterSupplierSourceGeneratorTest {
    public static final String SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE;
    public static final String SNAPSHOT_OF_SOURCE_WITH_PACKAGE;

    static {
        SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE = "" +
                "@javax.annotation.Generated(\n" +
                "        value = \"dev.alexengrig.metter.generator.SetterSupplierSourceGenerator\")\n" +
                "public class MyClass implements\n" +
                "        java.util.function.Supplier<\n" +
                "                java.util.Map<\n" +
                "                        java.lang.String,\n" +
                "                        java.util.function.BiConsumer<MyDomain, java.lang.Object>\n" +
                "                        >> {\n" +
                "    protected final java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.BiConsumer<MyDomain, java.lang.Object>\n" +
                "            > getterByField;\n" +
                "    public MyClass() {\n" +
                "        this.getterByField = createMap();\n" +
                "    }\n" +
                "\n" +
                "    protected java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.BiConsumer<MyDomain, java.lang.Object>\n" +
                "            > createMap() {\n" +
                "        java.util.HashMap<\n" +
                "                java.lang.String,\n" +
                "                java.util.function.BiConsumer<MyDomain, java.lang.Object>\n" +
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
                "    @Override\n" +
                "    public java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.BiConsumer<MyDomain, java.lang.Object>\n" +
                "            > get() {\n" +
                "        return getterByField;\n" +
                "    }\n" +
                "}\n";
        SNAPSHOT_OF_SOURCE_WITH_PACKAGE = "" +
                "package my.company;\n" +
                "\n" +
                "@javax.annotation.Generated(\n" +
                "        value = \"dev.alexengrig.metter.generator.SetterSupplierSourceGenerator\")\n" +
                "public class MyClass implements\n" +
                "        java.util.function.Supplier<\n" +
                "                java.util.Map<\n" +
                "                        java.lang.String,\n" +
                "                        java.util.function.BiConsumer<my.company.MyDomain, java.lang.Object>\n" +
                "                        >> {\n" +
                "    protected final java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.BiConsumer<my.company.MyDomain, java.lang.Object>\n" +
                "            > getterByField;\n" +
                "    public MyClass() {\n" +
                "        this.getterByField = createMap();\n" +
                "    }\n" +
                "\n" +
                "    protected java.util.Map<\n" +
                "            java.lang.String,\n" +
                "            java.util.function.BiConsumer<my.company.MyDomain, java.lang.Object>\n" +
                "            > createMap() {\n" +
                "        java.util.HashMap<\n" +
                "                java.lang.String,\n" +
                "                java.util.function.BiConsumer<my.company.MyDomain, java.lang.Object>\n" +
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
                "            java.util.function.BiConsumer<my.company.MyDomain, java.lang.Object>\n" +
                "            > get() {\n" +
                "        return getterByField;\n" +
                "    }\n" +
                "}\n";
    }

    private final SetterSupplierSourceGenerator generator = new SetterSupplierSourceGenerator(false);

    @Test
    public void should_generate_sourceWithoutPackage() {
        String className = "MyClass";
        String domainClassName = "MyDomain";
        Map<String, String> field2Setter = new HashMap<String, String>() {{
            put("intField", "MyDomain::getIntField");
            put("booleanField", "MyDomain::isBooleanField");
            put("stringField", "MyDomain::getStringField");
        }};
        String source = generator.generate(className, domainClassName, field2Setter);
        assertEquals("Source is invalid", SNAPSHOT_OF_SOURCE_WITHOUT_PACKAGE, source);
    }

    @Test
    public void should_generate_sourceWithPackage() {
        String className = "my.company.MyClass";
        String domainClassName = "my.company.MyDomain";
        Map<String, String> field2Setter = new HashMap<String, String>() {{
            put("intField", "MyDomain::getIntField");
            put("booleanField", "my.company.MyDomain::isBooleanField");
            put("stringField", "my.company.MyDomain::getStringField");
        }};
        String source = generator.generate(className, domainClassName, field2Setter);
        assertEquals("Source is invalid", SNAPSHOT_OF_SOURCE_WITH_PACKAGE, source);
    }
}