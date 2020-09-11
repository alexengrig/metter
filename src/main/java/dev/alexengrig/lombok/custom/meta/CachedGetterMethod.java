package dev.alexengrig.lombok.custom.meta;

public interface CachedGetterMethod<T, R> extends GetterMethod<T, R> {
    boolean hasCache(T object);

    R getCache(T object);

    R cache(T object, R value);

    @Override
    default R get(T object) {
        if (hasCache(object)) {
            return getCache(object);
        }
        return cache(object, GetterMethod.super.apply(object));
    }
}
