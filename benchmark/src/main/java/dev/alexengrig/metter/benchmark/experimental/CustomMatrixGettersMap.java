/*
 * Copyright 2020-2021 Alexengrig Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.alexengrig.metter.benchmark.experimental;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Name of field -> Getter of field
 *
 * @param <T> type of domain
 */
public class CustomMatrixGettersMap<T> extends AbstractMap<String, Function<T, Object>> {
    private final Node<T>[][] matrix;
    private final int length;

    private transient Set<String> keySet;
    private transient Collection<Function<T, Object>> values;
    private transient Set<Entry<String, Function<T, Object>>> entrySet;

    public CustomMatrixGettersMap(Map<String, Function<T, Object>> map) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException("Map is empty");
        }

        length = map.size();
        @SuppressWarnings("unchecked")
        Node<T>[][] matrix = new Node[length][];
        this.matrix = matrix;

        ArrayList<List<Node<T>>> lists = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            lists.add(null);
        }
        for (Entry<String, Function<T, Object>> entry : map.entrySet()) {
            int index = index(entry.getKey());
            List<Node<T>> nodes = lists.get(index);
            if (nodes == null) {
                nodes = new ArrayList<>(8);
                lists.set(index, nodes);
            }
            nodes.add(new Node<>(entry.getKey(), entry.getValue()));
        }

        @SuppressWarnings("unchecked")
        Node<T>[] dummy = new Node[0];
        for (int i = 0; i < length; i++) {
            List<Node<T>> nodes = lists.get(i);
            if (nodes != null) {
                this.matrix[i] = nodes.toArray(dummy);
            }
        }
    }

    private int index(Object key) {
        return Math.floorMod(key.hashCode(), length);
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Function) {
            for (Function<T, Object> getter : values()) {
                if (getter.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            int index = index(key);
            Node<T>[] nodes = this.matrix[index];
            if (nodes != null) {
                for (Node<T> node : nodes) {
                    if (node.getKey().equals(key)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Function<T, Object> get(Object key) {
        if (key instanceof String) {
            int index = index(key);
            Node<T>[] nodes = this.matrix[index];
            if (nodes != null) {
                for (Node<T> node : nodes) {
                    if (node.getKey().equals(key)) {
                        return node.getValue();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Function<T, Object> getOrDefault(Object key, Function<T, Object> defaultValue) {
        Function<T, Object> getter = get(key);
        return getter != null ? getter : defaultValue;
    }
// Unsupported

    @Override
    public Function<T, Object> put(String key, Function<T, Object> value) {
        throw new UnsupportedOperationException("put");
    }

    @Override
    public Function<T, Object> remove(Object key) {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public void putAll(Map<? extends String, ? extends Function<T, Object>> m) {
        throw new UnsupportedOperationException("putAll");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("clear");
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Function<T, Object>, ? extends Function<T, Object>> function) {
        throw new UnsupportedOperationException("replaceAll");
    }

    @Override
    public Function<T, Object> putIfAbsent(String key, Function<T, Object> value) {
        throw new UnsupportedOperationException("putIfAbsent");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public boolean replace(String key, Function<T, Object> oldValue, Function<T, Object> newValue) {
        throw new UnsupportedOperationException("replace");
    }

    @Override
    public Function<T, Object> replace(String key, Function<T, Object> value) {
        throw new UnsupportedOperationException("replace");
    }

    @Override
    public Function<T, Object> computeIfAbsent(String key, Function<? super String, ? extends Function<T, Object>> mappingFunction) {
        throw new UnsupportedOperationException("computeIfAbsent");
    }

    @Override
    public Function<T, Object> computeIfPresent(String key, BiFunction<? super String, ? super Function<T, Object>, ? extends Function<T, Object>> remappingFunction) {
        throw new UnsupportedOperationException("computeIfPresent");
    }

    @Override
    public Function<T, Object> compute(String key, BiFunction<? super String, ? super Function<T, Object>, ? extends Function<T, Object>> remappingFunction) {
        throw new UnsupportedOperationException("compute");
    }

    @Override
    public Function<T, Object> merge(String key, Function<T, Object> value, BiFunction<? super Function<T, Object>, ? super Function<T, Object>, ? extends Function<T, Object>> remappingFunction) {
        throw new UnsupportedOperationException("merge");
    }

    // Collections

    @Override
    public void forEach(BiConsumer<? super String, ? super Function<T, Object>> action) {
        for (Entry<String, Function<T, Object>> entry : entrySet()) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<String> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

    @Override
    public Collection<Function<T, Object>> values() {
        if (values == null) {
            values = new Values();
        }
        return values;
    }

    @Override
    public Set<Entry<String, Function<T, Object>>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    private static final class Node<T> extends AbstractMap.SimpleImmutableEntry<String, Function<T, Object>> {
        public Node(String key, Function<T, Object> value) {
            super(key, value);
        }
    }

    private final class KeySet extends AbstractSet<String> {
        @Override
        public Iterator<String> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return CustomMatrixGettersMap.this.size();
        }
    }

    private final class KeyIterator extends NodeIterator implements Iterator<String> {
        @Override
        public String next() {
            return nextNode().getKey();
        }
    }

    private final class Values extends AbstractCollection<Function<T, Object>> {
        @Override
        public Iterator<Function<T, Object>> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return CustomMatrixGettersMap.this.size();
        }
    }

    private final class ValueIterator extends NodeIterator implements Iterator<Function<T, Object>> {
        @Override
        public Function<T, Object> next() {
            Node<T> node = nextNode();
            return node.getValue();
        }
    }

    private final class EntrySet extends AbstractSet<Entry<String, Function<T, Object>>> {
        @Override
        public Iterator<Entry<String, Function<T, Object>>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return CustomMatrixGettersMap.this.size();
        }
    }

    private final class EntryIterator extends NodeIterator implements Iterator<Entry<String, Function<T, Object>>> {
        @Override
        public Entry<String, Function<T, Object>> next() {
            return nextNode();
        }
    }

    private class NodeIterator {
        private Node<T> next;
        private int rowIndex;
        private int colIndex;

        public NodeIterator() {
            next = null;
            rowIndex = colIndex = 0;
            while (rowIndex < matrix.length) {
                Node<T>[] nodes = matrix[rowIndex];
                if (nodes != null) {
                    next = nodes[colIndex++];
                    break;
                } else {
                    rowIndex++;
                }
            }
        }

        public boolean hasNext() {
            return next != null;
        }

        public Node<T> nextNode() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            Node<T> current = this.next;
            while (rowIndex < matrix.length) {
                Node<T>[] nodes = matrix[rowIndex];
                if (nodes != null && colIndex < nodes.length) {
                    next = nodes[colIndex++];
                    return current;
                } else {
                    rowIndex++;
                    colIndex = 0;
                }
            }
            next = null;
            return current;
        }
    }
}
