package dev.alexengrig.lombok.custom.meta;

public interface CachedSetterMethod<T, V> extends SetterMethod<T, V> {
    boolean hasCache(T object, V value);

    void cache(T object, V value);

    @Override
    default void set(T object, V value) {
        if (!hasCache(object, value)) {
            cache(object, value);
        }
    }
}
