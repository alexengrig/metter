package dev.alexengrig.metter.meta;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface SetterMethod<T, V> extends BiConsumer<T, V> {
    void set(T object, V value);

    default InstancedSetterMethod<V> forInstance(T object) {
        return value -> set(object, value);
    }

    @Override
    default void accept(T object, V value) {
        set(object, value);
    }
}
