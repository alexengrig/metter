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

    protected abstract PersonMetadataHolder getMetadataHolder();

    @Test
    public void should_return_expectedValues() {
        Person person = getPerson();
        PersonMetadataHolder metaHolder = getMetadataHolder();
        Function<Person, ?> constantGetter = metaHolder.getter("constant");
        assertEquals(person.getConstant(), constantGetter.apply(person));
        Function<Person, ?> integerGetter = metaHolder.getter("integer");
        assertEquals(person.getInteger(), integerGetter.apply(person));
        Function<Person, ?> stringGetter = metaHolder.getter("string");
        assertEquals(person.getString(), stringGetter.apply(person));
        Function<Person, ?> enableGetter = metaHolder.getter("enable");
        assertEquals(person.isEnable(), enableGetter.apply(person));
    }
}