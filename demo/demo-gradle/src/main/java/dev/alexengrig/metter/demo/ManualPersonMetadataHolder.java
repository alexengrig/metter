package dev.alexengrig.metter.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ManualPersonMetadataHolder implements Function<String, Function<Person, Object>> {
    private final Map<String, Function<Person, Object>> getterByField;

    public ManualPersonMetadataHolder() {
        this.getterByField = new HashMap<>(4);
        this.getterByField.put("constant", Person::getConstant);
        this.getterByField.put("integer", Person::getInteger);
        this.getterByField.put("string", Person::getString);
        this.getterByField.put("enable", Person::isEnable);
        this.getterByField.put("lombok", Person::getLombok);
    }

    @Override
    public Function<Person, Object> apply(String field) {
        if (!getterByField.containsKey(field)) {
            throw new IllegalArgumentException("No getter for field: " + field);
        }
        return getterByField.get(field);
    }
}
