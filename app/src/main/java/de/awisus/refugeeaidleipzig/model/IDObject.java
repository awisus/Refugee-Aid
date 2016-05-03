package de.awisus.refugeeaidleipzig.model;

import java.util.Observable;

/**
 * Created on 13.03.16.
 *
 * @author jens
 */
public abstract class IDObject extends Observable implements Comparable<IDObject> {

    protected int id;
    protected String name;

    @Override
    public int compareTo(IDObject another) {
        return -another.toString().compareTo(toString());
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
