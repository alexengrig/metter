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
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class PersonSetterSupplierTester {
    protected Person getPerson() {
        Person person = new Person();
        person.setInteger(100);
        person.setString("string");
        person.setEnable(true);
        person.setLombok("lombok");
        return person;
    }

    protected Person getOtherPerson() {
        Person person = new Person();
        person.setInteger(-100);
        person.setString("other_string");
        person.setEnable(false);
        person.setLombok("other_lombok");
        return person;
    }

    protected abstract Supplier<Map<String, BiConsumer<Person, Object>>> getSetterSupplier();

    @Test
    public void should_set_values() {
        Person person = getPerson();
        Person other = getOtherPerson();
        Supplier<Map<String, BiConsumer<Person, Object>>> setterSupplier = getSetterSupplier();
        Map<String, BiConsumer<Person, Object>> setterByField = setterSupplier.get();
        assertNotEquals(other.getInteger(), person.getInteger());
        setterByField.get("integer").accept(person, other.getInteger());
        assertEquals(other.getInteger(), person.getInteger());
        assertNotEquals(other.getString(), person.getString());
        setterByField.get("string").accept(person, other.getString());
        assertEquals(other.getString(), person.getString());
        assertNotEquals(other.isEnable(), person.isEnable());
        setterByField.get("enable").accept(person, other.isEnable());
        assertEquals(other.isEnable(), person.isEnable());
        // TODO: Add support of lombok
//        assertNotEquals(other.getLombok(), person.getLombok());
//        setterByField.get("lombok").accept(person, other.getLombok());
//        assertEquals(other.getLombok(), person.getLombok());
    }
}