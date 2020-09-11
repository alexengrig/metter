package dev.alexengrig.metter.meta;

import java.util.function.Function;

@FunctionalInterface
public interface GetterMethod<T, R> extends Function<T, R> {
    R get(T object);

    default InstancedGetterMethod<R> forInstance(T object) {
        return () -> get(object);
    }

    @Override
    default R apply(T object) {
        return get(object);
    }
}
