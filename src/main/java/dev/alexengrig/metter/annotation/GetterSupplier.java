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

package dev.alexengrig.metter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for to generate a getters supplier.
 *
 * @author Grig Alex
 * @version 0.1.0
 * @see dev.alexengrig.metter.processor.GetterSupplierProcessor
 * @since 0.1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface GetterSupplier {
    /**
     * A supplier class name.
     * Default value consisting of a prefix as an annotated class name
     * and a suffix as the supplier name: {@code ${CLASS_NAME}GetterSupplier}.
     *
     * @return the supplier class name
     * @since 0.1.0
     */
    String value() default "";

    /**
     * Field names to be included in the map of getters supplier.
     * <p>
     * Primary relative to {@link #excludedFields()}.
     *
     * @return field names
     * @since 0.1.0
     */
    String[] includedFields() default {};

    /**
     * Field names to be excluded in the map of getters supplier.
     * <p>
     * Ignored if {@link #includedFields()} is specified.
     *
     * @return field names
     * @since 0.1.0
     */
    String[] excludedFields() default {};
}
