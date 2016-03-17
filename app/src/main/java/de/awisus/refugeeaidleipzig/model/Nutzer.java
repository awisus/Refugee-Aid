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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 12.01.16.
 *
 * Class providing an implementation of a user being resident of an accommodation
 * This class extends Observable to provide Observers with information about needs being added or
 * removed
 * @author Jens Awisus
 */
public class Nutzer extends IDObject {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private String mail;

    /**
     * Accommodation this user stays in
     */
    private Unterkunft unterkunft;

    /**
     * List of needs being uttered to the public
     */
    private DataMap<Bedarf> bedarf;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public constructor to instantiate with name and accommodation
     * instantiates list of needs, processes login into given accommodation
     */
    private Nutzer() {
        bedarf = new DataMap<>();
    }

    public static Nutzer fromJSON(DataMap<Unterkunft> unterkuenfte, JSONObject json) throws JSONException {
        // Instatiate new user
        Nutzer nutzer = new Nutzer();

        // put data to it from json object

        nutzer.id              = json.getInt("id");
        nutzer.name            = json.getString("name");
        nutzer.mail            = json.getString("mail");
        nutzer.unterkunft      = unterkuenfte.getFromID(json.getInt("accommodation_id"));

        JSONArray jsonBedarf   = json.getJSONArray("needs");
        for(int i = 0; i < jsonBedarf.length(); i++) {
            nutzer.bedarf.add(Bedarf.fromJSON(jsonBedarf.getJSONObject(i)));
        }

        // Better trim Strings, perform rid of white spaces
        nutzer.name = nutzer.name.trim();
        nutzer.mail = nutzer.mail.trim();

        return nutzer;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public void addBedarf(Bedarf bedarf) {
        this.bedarf.add(bedarf);
        setChanged();
        notifyObservers();
    }

    public void loescheBedarf(int id) {
        bedarf.remove(id);
        setChanged();
        notifyObservers();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public String getMail() {
        return mail;
    }

    /**
     * Get accommodation this user stays in
     * @return accommodation this user stays in
     */
    public Unterkunft getUnterkunft() {
        return unterkunft;
    }

    /**
     * Getter for personal needs
     * @return list of this user's needs
     */
    public DataMap<Bedarf> getBedarf() {
        return bedarf;
    }

    public boolean hatBedarf() {
        return bedarf.size() > 0;
    }
}
