package de.awisus.refugeeaidleipzig.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class DataMap<T extends IDObject> implements Iterable<T> {

    private HashMap<Integer, T> map;

    public DataMap() {
        map = new HashMap<>();
    }

    public void add(T daten) {
        map.put(daten.getId(), daten);
    }

    public T get(int index) {
        return asList().get(index);
    }

    public T getFromID(int id) {
        return map.get(id);
    }

    public T remove(Integer i) {
        return map.remove(i);
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
