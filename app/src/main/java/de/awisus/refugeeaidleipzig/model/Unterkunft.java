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

import java.util.LinkedList;

/**
 * Created on 11.01.16.
 *
 * Class modelling an accommodation by storing its name, langitude, longitude, size and residents
 * @author Jens Awisus
 */
public class Unterkunft {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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

    /**
     * List of residents
     */
    private LinkedList<Nutzer> bewohner;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Private constructor initialising resident list
     */
    private Unterkunft() {
        bewohner = new LinkedList<>();
    }

    /**
     * Static factory method for instantiation of an accommodation by a given JSON Object
     * @param json given JSON Object
     * @return accommodation for the respective input json data
     * @throws JSONException Exception, if error parsing from json or during retrieval
     */
    public static Unterkunft fromJSON(JSONObject json) throws JSONException {
        // declare new accommodation
        Unterkunft unterkunft;

        // return null, if json is null
        if(json == null) {
            return null;
        } else {
            // Instatiate new accommodation
            unterkunft = new Unterkunft();

            // put data to it from json object
            unterkunft.name = json.getString("name");
            unterkunft.groesse = json.getInt("groesse");
            unterkunft.latLng = new LatLng(
                    json.getDouble("laengengrad"),
                    json.getDouble("breitengrad")
            );
        }

        // Better trim Strings, get rid of white spaces
        if(unterkunft.name != null) {
            unterkunft.name = unterkunft.name.trim();
        }

        return unterkunft;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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

    /**
     * Override toString() method
     * @return name of this accommodation
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Login method for new user living here
     * @param nutzer resident to live here
     */
    public void anmelden(Nutzer nutzer) {
        bewohner.add(nutzer);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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
        return bewohner.size();
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
        if(!hatBedarf()) {  // No need? return null
            return null;
        } else {            // Get needs of each resident as string and separate with comma
            String str = "";
            for(int i = 0; i < bewohner.size(); i++) {
            
                // Get needs user
                String bedarfNutzer = bewohner.get(i).getBedarfeAlsKommaString();
                
                // add user's needs, if not null
                if(bedarfNutzer != null) {
                    str += bedarfNutzer;
                    if(i < bewohner.size() - 1) {
                        // In the end of all strings no comma necessary
                        str += ", ";
                    }
                }
            }

            return str;
        }
    }

    /**
     * Getter looking for an existing resident in this accommodation
     * @param suchname name of desired resident
     * @return null, if resident is not found by name; resident else
     */
    public Nutzer findeBewohner(String suchname) {
        for(Nutzer nutzer : bewohner) {
            if(nutzer.getName().equals(suchname)) {
                return nutzer;
            }
        }
        return null;
    }

    /**
     * Getter indicating that residents have needs
     * @return true, if a resident has needs; false else
     */
    public boolean hatBedarf() {
        for(Nutzer nutzer : bewohner) {
            if(nutzer.hatBedarf()) {
                return true;
            }
        }

        return false;
    }
}
