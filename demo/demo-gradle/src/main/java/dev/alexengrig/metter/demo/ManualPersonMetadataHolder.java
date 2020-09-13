package dev.alexengrig.metter.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ManualPersonMetadataHolder implements PersonMetadataHolder {
    private final Map<String, Function<Person, Object>> getterByField;

    public ManualPersonMetadataHolder() {
        this.getterByField = new HashMap<>();
        this.getterByField.put("constant", Person::getConstant);
        this.getterByField.put("integer", Person::getInteger);
        this.getterByField.put("string", Person::getString);
        this.getterByField.put("enable", Person::isEnable);
    }

    @Override
    public Function<Person, Object> getter(String field) {
        if (!getterByField.containsKey(field)) {
            throw new IllegalArgumentException("No getter for field: " + field);
        }
        return getterByField.get(field);
    }
}
