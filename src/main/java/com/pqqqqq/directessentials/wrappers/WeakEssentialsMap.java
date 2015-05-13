package com.pqqqqq.directessentials.wrappers;

import com.pqqqqq.directessentials.wrappers.interfaces.IWeakValue;

import java.util.*;

/**
 * Created by Kevin on 2015-05-12.
 */
public class WeakEssentialsMap<T, U extends IWeakValue> implements Iterable<U>, Cloneable {
    private final Map<T, U> items = new HashMap<T, U>();

    public WeakEssentialsMap() {
    }

    public WeakEssentialsMap(Map<T, U> map) {
        this.items.putAll(map);
    }

    public Map<T, U> getItems() {
        return this.items;
    }

    public Set<T> keySet() {
        return this.items.keySet();
    }

    public Collection<U> values() {
        return this.items.values();
    }

    public U put(T key, U value) {
        return this.items.put(key, value);
    }

    public void putAll(Map<T, U> map) {
        this.items.putAll(map);
    }

    public U remove(T key) {
        return this.items.remove(key);
    }

    public U get(T key) {
        return this.items.get(key);
    }

    public boolean contains(T key) {
        return this.items.containsKey(key);
    }

    public U getOrCreate(T key, U empty, Object... values) {
        U value = get(key);
        if (value != null) {
            return value;
        }

        // Create
        ((IWeakValue) empty).init(values);
        items.put(key, empty);
        return empty;
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        this.items.clear();
    }

    public Iterator<U> iterator() {
        return this.values().iterator();
    }

    @Override
    public WeakEssentialsMap<T, U> clone() {
        Map<T, U> items = new HashMap<T, U>();
        items.putAll(this.items);

        return new WeakEssentialsMap<T, U>(items);
    }
}
