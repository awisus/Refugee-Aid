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

package de.awisus.refugeeaid.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 11.01.16.
 *
 * Class modelling an accommodation by storing its name, langitude, longitude, size and residents
 * @author Jens Awisus
 */
public class Unterkunft extends IDObject implements ILocationDataObject {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private String stadt;

    /**
     * Geographic coordinates
     */
    private LatLng latLng;

    /**
     * Space availyble for residents
     */
    private int groesse;

    private int anzahlBewohner;

    private DataMap<Bedarf> bedarf;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Private constructor
     */
    private Unterkunft() {
        bedarf = new DataMap<>();
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

        JSONArray jsonBedarf   = json.getJSONArray("needs");

        for(int i = 0; i < jsonBedarf.length(); i++) {
            unterkunft.bedarf.add(Bedarf.fromJSON(jsonBedarf.getJSONObject(i)));
        }

        // Better trim Strings, perform rid of white spaces
        unterkunft.stadt = unterkunft.stadt.trim();
        unterkunft.name = unterkunft.name.trim();

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

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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
    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * Turns list of needs of each resident into a string to be shown on map and profile page
     * @return comma-separated list if resident's needs
     */
    public String getBedarfeAlsString() {
        if(!hatBedarf()) {
            return null;
        } else {
            String str = "";
            for(int i = 0; i < bedarf.size(); i++) {
                str += bedarf.get(i);
                if(i < bedarf.size() - 1) {
                    str += "\n";
                }
            }

            return str;
        }
    }

    /**
     * Getter indicating that residents have needs
     * @return true, if a resident has needs; false else
     */
    public boolean hatBedarf() {
        return bedarf.size() != 0;
    }
}
