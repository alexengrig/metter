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

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public abstract class PersonMetadataHolderTester {
    protected Person getPerson() {
        Person person = new Person();
        int integer = 100;
        person.setInteger(integer);
        String string = "string";
        person.setString(string);
        person.setEnable(true);
        return person;
    }

    protected abstract Function<String, Function<Person, Object>> getMetadataHolder();

    @Test
    public void should_return_expectedValues() {
        Person person = getPerson();
        Function<String, Function<Person, Object>> metaHolder = getMetadataHolder();
        Function<String, Object> getterByFieldForPerson = metaHolder.andThen(t -> t.apply(person));
        assertEquals(person.getConstant(), getterByFieldForPerson.apply("constant"));
        assertEquals(person.getInteger(), getterByFieldForPerson.apply("integer"));
        assertEquals(person.getString(), getterByFieldForPerson.apply("string"));
        assertEquals(person.isEnable(), getterByFieldForPerson.apply("enable"));
        // TODO: Add support of lombok
//        assertEquals(person.getLombok(), getterByFieldForPerson.apply("lombok"));
    }
}