package de.awisus.refugeeaidleipzig.model;

import java.util.HashMap;

/**
 * Created on 10.03.16.
 *
 * @author jens
 */
public class UnterkunftMap {

    private HashMap<Integer, Unterkunft> map;

    public UnterkunftMap() {
        map = new HashMap<>();
    }

    public void add(int id, Unterkunft daten) {
        map.put(id, daten);
    }

    public Unterkunft get(int id) {
        return map.get(id);
    }
}
