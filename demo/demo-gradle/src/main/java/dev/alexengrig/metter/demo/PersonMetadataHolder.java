package dev.alexengrig.metter.demo;

import java.util.function.Function;

public interface PersonMetadataHolder {
   Function<Person, Object> getter(String field);
}
