package dev.alexengrig.metter.demo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ReflectionPersonMetadataHolder implements Function<String, Function<Person, Object>> {
    private final Map<String, Function<Person, Object>> getterByField;

    public ReflectionPersonMetadataHolder() {
        this.getterByField = new HashMap<>();
        for (Field field : Person.class.getDeclaredFields()) {
            String fieldName = field.getName();
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            for (Method method : Person.class.getDeclaredMethods()) {
                String methodName = method.getName();
                if ((methodName.startsWith("get") && methodName.equals("get" + capitalizedFieldName))
                        || (methodName.startsWith("is") && methodName.equals("is" + capitalizedFieldName))) {
                    this.getterByField.put(fieldName, object -> {
                        try {
                            return method.invoke(object);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IllegalArgumentException(object.toString());
                        }
                    });
                    break;
                }
            }
        }
    }

    @Override
    public Function<Person, Object> apply(String field) {
        if (!getterByField.containsKey(field)) {
            throw new IllegalArgumentException("No getter for field: " + field);
        }
        return getterByField.get(field);
    }
}
