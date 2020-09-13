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
    }
}