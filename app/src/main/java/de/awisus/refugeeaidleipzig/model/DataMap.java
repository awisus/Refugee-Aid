package de.awisus.refugeeaidleipzig.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public abstract class DataMap<T> implements Iterable<T> {

    protected HashMap<Integer, T> map;

    protected DataMap() {
        map = new HashMap<>();
    }

    public void add(int id, T daten) {
        map.put(id, daten);
    }

    public T get(int id) {
        return map.get(id);
    }

    public int size() {
        return map.size();
    }

    public LinkedList<T> asList() {
        return new LinkedList<>(map.values());
    }

    @Override
    public Iterator<T> iterator() {

        Iterator<T> it = new Iterator<T>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < map.size() && map.get(currentIndex) != null;
            }

            @Override
            public T next() {
                return map.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }
}
