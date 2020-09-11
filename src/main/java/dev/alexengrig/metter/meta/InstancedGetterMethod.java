package dev.alexengrig.metter.meta;

import java.util.function.Supplier;

@FunctionalInterface
public interface InstancedGetterMethod<T> extends Supplier<T> {
    @Override
    T get();
}
