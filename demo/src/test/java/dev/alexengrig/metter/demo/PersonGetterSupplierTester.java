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

package dev.alexengrig.metter.demo;

import org.junit.Test;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class PersonGetterSupplierTester {
    protected Person getPerson() {
        Person person = new Person();
        person.setInteger(100);
        person.setString("string");
        person.setEnable(true);
        return person;
    }

    protected abstract Supplier<Map<String, Function<Person, Object>>> getGetterSupplier();

    @Test
    public void should_return_valuesFromGetters() {
        Person person = getPerson();
        Supplier<Map<String, Function<Person, Object>>> getterSupplier = getGetterSupplier();
        Map<String, Function<Person, Object>> getterByField = getterSupplier.get();
        assertEquals(person.getConstant(), getterByField.get("constant").apply(person));
        assertEquals(person.getInteger(), getterByField.get("integer").apply(person));
        assertEquals(person.getString(), getterByField.get("string").apply(person));
        assertEquals(person.isEnable(), getterByField.get("enable").apply(person));
        // TODO: Add support of lombok
//        assertEquals(person.getLombok(), getterByField.get("lombok").apply(person));
    }

    @Test
    public void should_ignore_excludedFields() {
        Supplier<Map<String, Function<Person, Object>>> getterSupplier = getGetterSupplier();
        Map<String, Function<Person, Object>> getterByField = getterSupplier.get();
        assertFalse(getterByField.containsKey("excluded"));
    }
}