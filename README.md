# metter

[![Build Status](https://travis-ci.com/alexengrig/metter.svg?branch=master)](https://travis-ci.com/alexengrig/metter)

Metter is an annotation processor for generation meta-information (getters and setters).

## Get Started

### Install

#### Gradle

Add this code to `dependencies` section in your `build.gradle`:

```groovy
compileOnly 'dev.alexengrig:metter:0.1.0-SNAPSHOT'
annotationProcessor 'dev.alexengrig:metter:0.1.0-SNAPSHOT'
```

#### Maven

Add this code to `dependencies` section in your `pom.xml`:

```xml
<dependency>
    <groupId>dev.alexengrig</groupId>
    <artifactId>metter</artifactId>
    <version>0.1.0-SNAPSHOT</version>
    <scope>provided</scope>
    <optional>true</optional>
</dependency>
```

Specify the annotation processor to `maven-compiler-plugin` plugin:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <annotationProcessorPaths>
                    <annotationProcessorPath>
                        <groupId>dev.alexengrig</groupId>
                        <artifactId>metter</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                    </annotationProcessorPath>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Using

Add to your class `@GetterSupplier` for to generate getters and/or
`@SetterSupplier` for to generate setters:

```java
import dev.alexengrig.metter.annotation.GetterSupplier;
import dev.alexengrig.metter.annotation.SetterSupplier;

@GetterSupplier
@SetterSupplier
public class Domain {
    private int integer;
    private boolean bool;
    private String string;

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
```

The generated suppliers have a name consisting of a prefix as a class name
and a suffix as the supplier name: `${CLASS_NAME}GetterSupplier` and `${CLASS_NAME}SetterSupplier`.

All fields that have getters/setter will be added to
the map that `DomainGetterSupplier`/`DomainSetterSupplier` stores
and to get it, you need to call `Supplier#get`:

```java
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DomainService {
    private final Map<String, Function<Domain, Object>> getterByField = new DomainGetterSupplier().get();
    private final Map<String, BiConsumer<Domain, Object>> setterByField = new DomainSetterSupplier().get();

    public void printFieldValues(Domain domain) {
        getterByField.forEach((field, getter) -> System.out.println(field + " = " + getter.apply(domain)));
    }

    public void transfer(Domain from, Domain to) {
        setterByField.forEach((field, setter) -> setter.accept(to, getterByField.get(field).apply(from)));
    }
}
```

Or you can extend:

```java
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public class CustomDomainGetterSupplier extends DomainGetterSupplier {
    @Override
    protected Map<String, Function<Domain, Object>> createMap() {
        Map<String, Function<Domain, Object>> generatedMap = super.createMap();
        generatedMap.put("name", domain -> "Name: domain.toString()");
        return Collections.unmodifiableMap(generatedMap);
    }

    public Function<Domain, Object> getGetter(String field) {
        return this.getterByField.get(field);
    }
}
```

Or you can create a bean (e.g. [Spring](https://github.com/spring-projects/spring-framework)):

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public Map<String, Function<Domain, Object>> getterByField() {
        return new DomainGetterSupplier().get();
    }

    @Bean
    public Map<String, BiConsumer<Domain, Object>> setterByField() {
        return new DomainSetterSupplier().get();
    }
}
```

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
        return new HashMap<>() {{
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

## License

This project is [licensed](LICENSE) under [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
