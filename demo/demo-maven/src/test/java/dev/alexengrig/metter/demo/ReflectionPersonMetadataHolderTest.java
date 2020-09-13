package dev.alexengrig.metter.demo;

import java.util.function.Function;

public class ReflectionPersonMetadataHolderTest extends PersonMetadataHolderTester {
    @Override
    protected Function<String, Function<Person, Object>> getMetadataHolder() {
        return new ReflectionPersonMetadataHolder();
    }
}