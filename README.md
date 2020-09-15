# metter

Metter is an annotation processor for generation meta-information (getters and setters).

## Motivation

### Problem

Has a domain:

```java
class Man {
    private String name;
    private int age;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getAge() {
        return age;
    }

    void setAge(int age) {
        this.age = age;
    }
}
```

Need a change journal:

```
// field: Old value -> New value
name: Tom -> Tomas
age: 18  -> 19
```

### Solution

[See full code](src/test/java/dev/alexengrig/metter/motivation).

#### Manual solution

Each field:

```java
import java.util.StringJoiner;

class ManualManChangeLogGenerator {
    String generate(Man man, Man newMan) {
        StringJoiner joiner = new StringJoiner("\n");
        if (!man.getName().equals(newMan.getName())) {
            joiner.add(change("name", man.getName(), newMan.getName()));
        }
        if (man.getAge() != newMan.getAge()) {
            joiner.add(change("age", man.getAge(), newMan.getAge()));
        }
        return joiner.toString();
    }

    String change(String field, Object value, Object newValue) {
        return field + ": " + value.toString() + " -> " + newValue.toString();
    }
}
```

Using `Map`:

```java
import java.util.*;
import java.util.function.Function;

class MapManChangeLogGenerator extends ManualManChangeLogGenerator {
    Map<String, Function<Man, Object>> getterByField = createMap();

    Map<String, Function<Man, Object>> createMap() {
        return new HashMap<String, Function<Man, Object>>() {{
            put("name", Man::getName);
            put("age", Man::getAge);
        }};
    }

    @Override
    String generate(Man man, Man newMan) {
        StringJoiner joiner = new StringJoiner("\n");
        getterByField.forEach((field, getter) -> {
            Object value = getter.apply(man);
            Object newValue = getter.apply(newMan);
            if (!Objects.equals(value, newValue)) {
                joiner.add(change(field, value, newValue));
            }
        });
        return joiner.toString();
    }
}
```

#### Reflection solution

```java
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.function.Function;

class ReflectionManChangeLogGenerator extends MapManChangeLogGenerator {
    @Override
    HashMap<String, Function<Man, Object>> createMap() {
        HashMap<String, Function<Man, Object>> map = new HashMap<>();
        Field[] fields = Man.class.getDeclaredFields();
        this.getterByField = new HashMap<>(fields.length);
        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = getCapitalized(fieldName);
            for (Method method : Man.class.getDeclaredMethods()) {
                String methodName = method.getName();
                if (isGetter(methodName, capitalizedFieldName)) {
                    this.getterByField.put(fieldName, createGetter(method));
                    break;
                }
            }
        }
        return map;
    }

    String getCapitalized(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    boolean isGetter(String methodName, String lastMethodNamePart) {
        return (methodName.startsWith("get") && methodName.equals("get" + lastMethodNamePart))
                || (methodName.startsWith("is") && methodName.equals("is" + lastMethodNamePart));
    }

    Function<Man, Object> createGetter(Method method) {
        return instance -> {
            try {
                return method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(instance.toString());
            }
        };
    }
}
```

#### Generation solution

Add `@GetterSupplier` annotation:

```java
import dev.alexengrig.metter.annotation.GetterSupplier;

@GetterSupplier
class Man {/*...*/}
```

Using `ManGetterSupplier`:

```java
import java.util.Map;
import java.util.function.Function;

class GeneratedManChangeLogGenerator extends MapManChangeLogGenerator {
    @Override
    Map<String, Function<Man, Object>> createMap() {
        return new ManGetterSupplier().get();
    }
}
```

### Conclusion

If you add a new field to `Man`,
then the reflection solution and the generation solution will continue to work,
unlike the manual solution.
The generation solution is faster than the reflection solution (reflection is slow).
