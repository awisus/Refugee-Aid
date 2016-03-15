package de.awisus.refugeeaidleipzig.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class DataMap<T extends IDObject> implements Iterable<T> {

    private HashMap<Integer, T> map;
    private LinkedList<T> list;
    private Vector<T> vector;

    public DataMap() {
        map = new HashMap<>();
        list = new LinkedList<>();
        vector = new Vector<>();
    }

    public void add(T daten) {
        map.put(daten.getId(), daten);
        list.add(daten);
        vector.add(daten);
    }

    public T get(int index) {
        return list.get(index);
    }

    public T getFromID(int id) {
        return map.get(id);
    }

    public T remove(Integer i) {
        T toRemove = map.remove(i);

        list.remove(toRemove);
        vector.remove(toRemove);

        return toRemove;
    }

    public int size() {
        return map.size();
    }

    public LinkedList<T> asList() {
        return list;
    }

    public Vector<T> asVector() {
        return vector;
    }

    @Override
    public Iterator<T> iterator() {
        return map.values().iterator();
    }
}
