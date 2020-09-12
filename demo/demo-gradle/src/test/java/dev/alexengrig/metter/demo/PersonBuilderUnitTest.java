package dev.alexengrig.metter.demo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonBuilderUnitTest {

    @Test
    public void whenBuildPersonWithBuilder_thenObjectHasPropertyValues() {

        Person person = new PersonBuilder().setInteger(25).setString("John").build();

        assertEquals(25, person.getInteger());
        assertEquals("John", person.getString());

    }

}
