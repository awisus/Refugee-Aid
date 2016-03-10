package de.awisus.refugeeaidleipzig.model;

import java.util.HashMap;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public abstract class DataMap<T> {

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
}
