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

/**
 * Generator source of getter supplier.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @since 0.1.0
 */
public class GetterSupplierSourceGenerator extends MethodSupplierSourceGenerator {
    /**
     * Constructs.
     *
     * @since 0.1.0
     */
    public GetterSupplierSourceGenerator() {
        super();
    }

    /**
     * Constructs with mark about adding generated date.
     *
     * @param withGeneratedDate mark about adding generated date
     * @since 0.1.0
     */
    protected GetterSupplierSourceGenerator(boolean withGeneratedDate) {
        super(withGeneratedDate);
    }

    /**
     * Returns {@code getterByField}.
     *
     * @return {@code getterByField}
     */
    @Override
    protected String getMapFieldName() {
        return "getterByField";
    }

    /**
     * {@inheritDoc}
     *
     * <pre>{@code
     * java.util.Function<DomainClass, java.lang.Object>
     * }</pre>
     *
     * @since 0.1.0
     */
    @Override
    protected String getMapValueType(String domainClassName) {
        return String.format("java.util.function.Function<%s, java.lang.Object>", domainClassName);
    }

    /**
     * Returns {@code Getters}.
     *
     * @return {@code Getters}
     */
    @Override
    protected String getJavaDocTypeNameForClass() {
        return "Getters";
    }

    /**
     * Returns {@code getter}.
     *
     * @return {@code getter}
     */
    @Override
    protected String getJavaDocTypeName() {
        return "getter";
    }
}
