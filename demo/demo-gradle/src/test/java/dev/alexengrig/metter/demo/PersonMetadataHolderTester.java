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
        assertEquals(person.getConstant(), metaHolder.andThen(t -> t.apply(person)).apply("constant"));
        assertEquals(person.getInteger(), metaHolder.andThen(t -> t.apply(person)).apply("integer"));
        assertEquals(person.getString(), metaHolder.andThen(t -> t.apply(person)).apply("string"));
        assertEquals(person.isEnable(), metaHolder.andThen(t -> t.apply(person)).apply("enable"));
    }
}