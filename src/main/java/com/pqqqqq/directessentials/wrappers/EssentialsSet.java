package com.pqqqqq.directessentials.wrappers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Kevin on 2015-05-12.
 */
public class EssentialsSet<T> implements Iterable<T>, Cloneable {
    private final Set<T> items = new HashSet<T>();

    public EssentialsSet() {
    }

    public EssentialsSet(Collection<T> col) {
        this.items.addAll(col);
    }

    public Set<T> getItems() {
        return this.items;
    }

    public boolean add(T item) {
        return this.items.add(item);
    }

    public boolean remove(T item) {
        return this.items.remove(item);
    }

    public boolean contains(T item) {
        return this.items.contains(item);
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<T> iterator() {
        return this.items.iterator();
    }

    @Override
    public EssentialsSet<T> clone() {
        Set<T> items = new HashSet<T>();
        items.addAll(this.items);

        return new EssentialsSet<T>(items);
    }
}
