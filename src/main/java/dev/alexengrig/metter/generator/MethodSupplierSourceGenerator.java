/*
 * Copyright 2020-2021 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.metter.generator;

import dev.alexengrig.metter.util.LineJoiner;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Base generator source of method supplier.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class MethodSupplierSourceGenerator {
    /**
     * Mark about adding generated date to {@link javax.annotation.Generated#date()}.
     *
     * @since 0.1.0
     */
    private final boolean withGeneratedDate;

    /**
     * Constructs generator with generated date.
     *
     * @since 0.1.0
     */
    public MethodSupplierSourceGenerator() {
        this(true);
    }

    /**
     * Constructs generator.
     *
     * @param withGeneratedDate mark about adding generated date
     * @since 0.1.0
     */
    protected MethodSupplierSourceGenerator(boolean withGeneratedDate) {
        this.withGeneratedDate = withGeneratedDate;
    }

    /**
     * Generates method supplier source.
     *
     * @param className       supplier class name
     * @param domainClassName domain class name
     * @param field2Method    map of field to method
     * @return method supplier source
     * @since 0.1.0
     */
    public String generate(String className, String domainClassName, Map<String, String> field2Method) {
        String packageName = getPackageName(className);
        String simpleClassName = getSimpleName(className);
        String mapValueType = getMapValueType(domainClassName);
        String javaDocTypeName = getJavaDocTypeName();
        return new LineJoiner()
                .ftIf(packageName != null, "package %s;\n", packageName)
                .ln("/**")
                .ft(" * %s supplier of {@link %s}.", getJavaDocTypeNameForClass(), domainClassName)
                .ln(" */")
                .ln("@javax.annotation.Generated(")
                .ftIf(withGeneratedDate, "        date = \"%s\",", LocalDateTime.now().toString())
                .ft("        value = \"%s\")", getClass().getName())
                .ft("public class %s implements", simpleClassName)
                .ln("        java.util.function.Supplier<")
                .ln("                java.util.Map<")
                .ln("                        java.lang.String,")
                .ft("                        %s", mapValueType)
                .ln("                        >> {")
                .ln()
                .ln("    /**")
                .ft("     * Map, %s function by field name.", javaDocTypeName)
                .ln("     */")
                .ln("    protected final java.util.Map<")
                .ln("            java.lang.String,")
                .ft("            %s", mapValueType)
                .ln("            > getterByField;")
                .ln()
                .ln("    /**")
                .ln("     * Constructs this.")
                .ln("     */")
                .ft("    public %s() {", simpleClassName)
                .ln("        this.getterByField = createMap();")
                .ln("    }")
                .ln()
                .ln("    /**")
                .ft("     * Creates map, %s function by field name.", javaDocTypeName)
                .ln("     *")
                .ft("     * @return map, %s function by field name", javaDocTypeName)
                .ln("     */")
                .ln("    protected java.util.Map<")
                .ln("            java.lang.String,")
                .ft("            %s", mapValueType)
                .ln("            > createMap() {")
                .ln("        java.util.HashMap<")
                .ln("                java.lang.String,")
                .ft("                %s", mapValueType)
                .ft("                > map = new java.util.HashMap<>(%d);", field2Method.size())
                .mp("        map.put(\"%s\",\n                %s);", (f, g) -> new Object[]{f, g}, field2Method)
                .ln("        return map;")
                .ln("    }")
                .ln()
                .ln("    /**")
                .ft("     * Returns map, %s function by field name.", javaDocTypeName)
                .ln("     *")
                .ft("     * @return map, %s function by field name", javaDocTypeName)
                .ln("     */")
                .ln("    @Override")
                .ln("    public java.util.Map<")
                .ln("            java.lang.String,")
                .ft("            %s", mapValueType)
                .ln("            > get() {")
                .ln("        return getterByField;")
                .ln("    }")
                .ln("}")
                .toString();
    }

    /**
     * Returns a package name from a class name
     *
     * @param className class name
     * @return package name
     * @since 0.1.0
     */
    protected String getPackageName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return className.substring(0, lastIndexOfDot);
        }
        return null;
    }

    /**
     * Returns simple class name (without a package name).
     *
     * @param className class name
     * @return simple class name (without a package name)
     * @since 0.1.0
     */
    protected String getSimpleName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        return className.substring(lastIndexOfDot + 1);
    }

    /**
     * Returns a type of map value from a domain class name.
     *
     * @param domainClassName domain class name
     * @return type of map value
     * @since 0.1.0
     */
    protected abstract String getMapValueType(String domainClassName);

    /**
     * Returns a type name for JavaDoc in class.
     *
     * @return type name for JavaDoc in class
     * @since 0.2.0
     */
    protected abstract String getJavaDocTypeNameForClass();

    /**
     * Returns a type name for JavaDoc.
     *
     * @return type name for JavaDoc
     * @since 0.2.0
     */
    protected abstract String getJavaDocTypeName();
}
