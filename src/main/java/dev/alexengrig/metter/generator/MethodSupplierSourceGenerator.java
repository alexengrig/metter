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

import dev.alexengrig.metter.processor.util.LineJoiner;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class MethodSupplierSourceGenerator {
    private final boolean withGeneratedDate;

    public MethodSupplierSourceGenerator() {
        this(true);
    }

    protected MethodSupplierSourceGenerator(boolean withGeneratedDate) {
        this.withGeneratedDate = withGeneratedDate;
    }

    public String generate(String className, String domainClassName, Map<String, String> field2Method) {
        String packageName = getPackageName(className);
        String simpleClassName = getSimpleName(className);
        return new LineJoiner()
                .ftIf(packageName != null, "package %s;\n", packageName)
                .ln("@javax.annotation.Generated(")
                .ftIf(withGeneratedDate, "        date = \"%s\",", LocalDateTime.now().toString())
                .ft("        value = \"%s\")", getClass().getName())
                .ft("public class %s implements", simpleClassName)
                .ln("        java.util.function.Supplier<")
                .ln("                java.util.Map<")
                .ln("                        java.lang.String,")
                .ft("                        %s", getMapValueType(domainClassName))
                .ln("                        >> {")
                .ln("    protected final java.util.Map<")
                .ln("            java.lang.String,")
                .ft("            %s", getMapValueType(domainClassName))
                .ln("            > getterByField;")
                .ft("    public %s() {", simpleClassName)
                .ln("        this.getterByField = createMap();")
                .ln("    }")
                .ln()
                .ln("    protected java.util.Map<")
                .ln("            java.lang.String,")
                .ft("            %s", getMapValueType(domainClassName))
                .ln("            > createMap() {")
                .ln("        java.util.HashMap<")
                .ln("                java.lang.String,")
                .ft("                %s", getMapValueType(domainClassName))
                .ft("                > map = new java.util.HashMap<>(%d);", field2Method.size())
                .mp("        map.put(\"%s\",\n                %s);", (f, g) -> new Object[]{f, g}, field2Method)
                .ln("        return map;")
                .ln("    }")
                .ln()
                .ln("    @Override")
                .ln("    public java.util.Map<")
                .ln("            java.lang.String,")
                .ft("            %s", getMapValueType(domainClassName))
                .ln("            > get() {")
                .ln("        return getterByField;")
                .ln("    }")
                .ln("}")
                .toString();
    }

    protected String getPackageName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot > 0) {
            return className.substring(0, lastIndexOfDot);
        }
        return null;
    }

    protected String getSimpleName(String className) {
        int lastIndexOfDot = className.lastIndexOf('.');
        return className.substring(lastIndexOfDot + 1);
    }

    protected abstract String getMapValueType(String className);
}
