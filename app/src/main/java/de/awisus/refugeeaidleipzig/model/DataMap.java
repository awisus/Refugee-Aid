package de.awisus.refugeeaidleipzig.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class DataMap<T> implements Iterable<T> {

    private HashMap<Integer, T> map;

    public DataMap() {
        map = new HashMap<>();
    }

    public void add(int id, T daten) {
        map.put(id, daten);
    }

    public T get(int index) {
        return asList().get(index);
    }

    public T getID(int id) {
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
        return map.values().iterator();
    }
}
