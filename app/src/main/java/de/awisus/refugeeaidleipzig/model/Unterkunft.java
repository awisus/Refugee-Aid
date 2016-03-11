/*
 * Copyright 2016 Jens Awisus <awisus.gdev@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package de.awisus.refugeeaidleipzig.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 11.01.16.
 *
 * Class modelling an accommodation by storing its name, langitude, longitude, size and residents
 * @author Jens Awisus
 */
public class Unterkunft implements Comparable<Unterkunft> {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private int id;

    private String stadt;

    /**
     * Name of the accommodation (e.g. Eythstrasse)
     */
    private String name;

    /**
     * Geographic coordinates
     */
    private LatLng latLng;

    /**
     * Space availyble for residents
     */
    private int groesse;

    private int anzahlBewohner;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Private constructor
     */
    private Unterkunft() {
    }

    public static Unterkunft fromJSON(JSONObject json) throws JSONException {

        // Instatiate new accommodation
        Unterkunft unterkunft = new Unterkunft();

        // put data to it from json object
        unterkunft.id               = json.getInt("id");
        unterkunft.stadt            = json.getString("city");
        unterkunft.name             = json.getString("name");
        unterkunft.groesse          = json.getInt("space");
        unterkunft.anzahlBewohner   = json.getInt("residents");
        unterkunft.latLng = new LatLng(
                                      json.getDouble("longitude"),
                                      json.getDouble("latitude")
        );

        // Better trim Strings, get rid of white spaces
        unterkunft.stadt = unterkunft.stadt.trim();
        unterkunft.name = unterkunft.name.trim();

        return unterkunft;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    @Override
    public int compareTo(Unterkunft another) {
        return -another.toString().compareTo(toString());
    }

    /**
     * Overridden equals function.
     * @param o Object to compare with
     * @return true, if o is accommodation and has same name; super.equals() else
     */
    @Override
    public boolean equals(Object o) {
        // Check for null and class
        if(o != null && o instanceof Unterkunft) {
            Unterkunft unterkunft = (Unterkunft) o;
            return unterkunft.name.equals(this.name);
        }
        return super.equals(o);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public int getID() {
        return id;
    }

    /**
     * Override toString() method
     * @return name of this accommodation
     */
    @Override
    public String toString() {
        return stadt +", " +name;
    }

    /**
     * Getter for size
     * @return Size of accommodation
     */
    public int getGroesse() {
        return groesse;
    }

    /**
     * Getter for number of residents
     * @return number of residents
     */
    public int getBewohner() {
        return anzahlBewohner;
    }

    /**
     * Getter for coordinates
     * @return latitude and longitude for map interpretation
     */
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * Turns list of needs of each resident into a string to be shown on map and profile page
     * @return comma-separated list if resident's needs
     */
    public String getBedarfeAlsString() {
        return null;
    }

    /**
     * Getter indicating that residents have needs
     * @return true, if a resident has needs; false else
     */
    public boolean hatBedarf() {
        return getBedarfeAlsString() != null;
    }
}
