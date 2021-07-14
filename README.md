<img alt="Metter Logo" src="/docs/images/metter-logo.png" width="100" />

# metter

[![Maven Central](https://img.shields.io/maven-central/v/dev.alexengrig/metter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22dev.alexengrig%22%20AND%20a:%22metter%22)
[![Javadocs](https://www.javadoc.io/badge/dev.alexengrig/metter.svg)](https://www.javadoc.io/doc/dev.alexengrig/metter)
[![GitHub](https://img.shields.io/github/license/alexengrig/metter?style=flat&&color=informational)](LICENSE)

[![Build Status](https://travis-ci.com/alexengrig/metter.svg?branch=master)](https://travis-ci.com/alexengrig/metter)
[![Codecov Coverage Status](https://codecov.io/gh/alexengrig/metter/branch/master/graph/badge.svg)](https://codecov.io/gh/alexengrig/metter)
[![Coveralls Coverage Status](https://coveralls.io/repos/github/alexengrig/metter/badge.svg?branch=master)](https://coveralls.io/github/alexengrig/metter?branch=master)
[![Codacy Coverage](https://app.codacy.com/project/badge/Coverage/a90d2d32c03e4d83860bf4d73eae47bb)](https://www.codacy.com/gh/alexengrig/metter/dashboard?utm_source=github.com&utm_medium=referral&utm_content=alexengrig/metter&utm_campaign=Badge_Coverage)
[![Codacy Grade](https://app.codacy.com/project/badge/Grade/a90d2d32c03e4d83860bf4d73eae47bb)](https://www.codacy.com/gh/alexengrig/metter/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=alexengrig/metter&amp;utm_campaign=Badge_Grade)
[![BCH Compliance](https://bettercodehub.com/edge/badge/alexengrig/metter?branch=master)](https://bettercodehub.com/)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Falexengrig%2Fmetter.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Falexengrig%2Fmetter?ref=badge_shield)

Metter (***met***a get***ter*** / set***ter***) is an annotation processor for generating getter and setter suppliers.

-   Supports [Lombok](https://github.com/rzwitserloot/lombok) annotations: `@Data`, `@Getter` and `@Setter`.
-   Supports inheritance (getters/setters of superclasses).
-   Ignores private methods.

[See](demo) examples.

## Table of Contents

-   [Get Started](#get-started)
    -   [Install](#install)
        -   [Gradle](#gradle)
        -   [Maven](#maven)
    -   [Using](#using)
        -   [Instance](#instance)
        -   [Inheritance](#inheritance)
        -   [Bean](#bean)
-   [API](#api)
    -   [GetterSupplier](#gettersupplier)
    -   [SetterSupplier](#settersupplier)
-   [Motivation](#motivation)
    -   [Problem](#problem)
    -   [Solution](#solution)
        -   [Manual](#manual)
        -   [Reflection](#reflection)
        -   [Generation](#generation)
    -   [Conclusion](#conclusion)
-   [License](#license)

## Get Started

### Install

#### Gradle

Add this code to `dependencies` section in your `build.gradle`:

```groovy
compileOnly 'dev.alexengrig:metter:0.1.1'
annotationProcessor 'dev.alexengrig:metter:0.1.1'
```

#### Maven

Add this code to `dependencies` section in your `pom.xml`:

```xml
<dependency>
    <groupId>dev.alexengrig</groupId>
    <artifactId>metter</artifactId>
    <version>0.1.1</version>
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
                        <version>0.1.1</version>
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

The generated suppliers have a default name consisting of a prefix as a class name,
and a suffix as the supplier name: `${CLASS_NAME}GetterSupplier` and `${CLASS_NAME}SetterSupplier`.
You can set a custom name using the annotation parameter `value`.

All fields that have getters/setter will be added to
the map that `DomainGetterSupplier`/`DomainSetterSupplier` stores.
You can set included/excluded field names using the annotation parameters `includeFields`/`excludeFields`.

#### Instance

The generated suppliers implement the
[Supplier](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)
functional interface and to get the map of getters/setters, you need to call `Supplier#get`:

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

#### Inheritance

You can extend:

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

#### Bean

You can create a bean (e.g. [Spring](https://github.com/spring-projects/spring-framework)):

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

## API

### GetterSupplier

An annotation for to generate a getters supplier.

| Field          | Type       | Default                       | Description                                |
|----------------|------------|-------------------------------|--------------------------------------------|
| value          | `String`   | `${CLASS_NAME}GetterSupplier` | Supplier class name                        |
| includedFields | `String[]` | empty                         | Array of fields to include in the supplier |
| excludedFields | `String[]` | empty                         | Array of fields to exclude in the supplier |

### SetterSupplier

An annotation for to generate a setters supplier.

| Field          | Type       | Default                       | Description                                |
|----------------|------------|-------------------------------|--------------------------------------------|
| value          | `String`   | `${CLASS_NAME}SetterSupplier` | Supplier class name                        |
| includedFields | `String[]` | empty                         | Array of fields to include in the supplier |
| excludedFields | `String[]` | empty                         | Array of fields to exclude in the supplier |

## Motivation

### Problem

Has a domain:

```java
class Man {
    private String name;
    private int age;

    /*Getters and Setters*/
}
```

Need a change journal:

```
// field: Old value -> New value
name: Tomas -> Tom
age: 18  -> 19
```

### Solution

[See full code](demo/src/main/java/dev/alexengrig/metter/motivation).

#### Manual

Each field:

```java
import java.util.StringJoiner;

class ManualManChangeLogGenerator extends BaseChangeLogGenerator<Man> {
    @Override
    public String generate(Man man, Man newMan) {
        StringJoiner joiner = new StringJoiner("\n");
        if (!man.getName().equals(newMan.getName())) {
            joiner.add(changeLog("name", man.getName(), newMan.getName()));
        }
        if (man.getAge() != newMan.getAge()) {
            joiner.add(changeLog("age", man.getAge(), newMan.getAge()));
        }
        return joiner.toString();
    }
}
```

Using `Map`:

```java
import java.util.*;
import java.util.function.Function;

class MapManChangeLogGenerator extends BaseChangeLogGenerator<Man> {
    protected Map<String, Function<Man, Object>> getterByField = createMap();

    protected Map<String, Function<Man, Object>> createMap() {
        return new HashMap<String, Function<Man, Object>>() {{
            put("name", Man::getName);
            put("age", Man::getAge);
        }};
    }

    @Override
    public String generate(Man man, Man newMan) {
        StringJoiner joiner = new StringJoiner("\n");
        getterByField.forEach((field, getter) -> {
            Object value = getter.apply(man);
            Object newValue = getter.apply(newMan);
            if (!Objects.equals(value, newValue)) {
                joiner.add(changeLog(field, value, newValue));
            }
        });
        return joiner.toString();
    }
}
```

#### Reflection

Each field via Java Reflection:

```java
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

class ReflectionManChangeLogGenerator extends MapManChangeLogGenerator {
    @Override
    protected Map<String, Function<Man, Object>> createMap() {
        Field[] fields = Man.class.getDeclaredFields();
        HashMap<String, Function<Man, Object>> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            String fieldName = field.getName();
            String capitalizedFieldName = getCapitalized(fieldName);
            for (Method method : Man.class.getDeclaredMethods()) {
                String methodName = method.getName();
                if (isGetter(methodName, capitalizedFieldName)) {
                    map.put(fieldName, createGetter(method));
                    break;
                }
            }
        }
        return map;
    }

    private String getCapitalized(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private boolean isGetter(String methodName, String lastMethodNamePart) {
        return (methodName.startsWith("get") && methodName.equals("get" + lastMethodNamePart))
                || (methodName.startsWith("is") && methodName.equals("is" + lastMethodNamePart));
    }

    private Function<Man, Object> createGetter(Method method) {
        return instance -> {
            try {
                return method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(instance.toString(), e);
            }
        };
    }
}
```

#### Generation

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

class GenerationManChangeLogGenerator extends MapManChangeLogGenerator {
    @Override
    protected Map<String, Function<Man, Object>> createMap() {
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

[JetBrains Mono typeface](https://www.jetbrains.com/lp/mono)
is [licensed](https://www.jetbrains.com/lp/mono/#license)
under [Apache License, version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
and it used in [logo](docs/images/metter-logo.png)
and [preview](docs/images/metter-preview.png).

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Falexengrig%2Fmetter.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Falexengrig%2Fmetter?ref=badge_large)
