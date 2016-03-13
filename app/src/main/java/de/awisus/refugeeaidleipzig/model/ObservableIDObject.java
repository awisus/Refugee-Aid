package de.awisus.refugeeaidleipzig.model;

import java.util.Observable;

/**
 * Created on 13.03.16.
 *
 * @author jens
 */
public abstract class ObservableIDObject extends Observable {

    protected int id;

    public int getId() {
        return id;
    }
}
