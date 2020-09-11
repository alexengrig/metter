package dev.alexengrig.metter.meta;

import java.util.function.Consumer;

@FunctionalInterface
public interface InstancedSetterMethod<T> extends Consumer<T> {
    void set(T value);

    @Override
    default void accept(T value) {
        set(value);
    }
}
