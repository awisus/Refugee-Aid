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
    private DataMap<IDObject> data;

    private int rolle;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public constructor to instantiate with name and accommodation
     * instantiates list of needs, processes login into given accommodation
     */
    private Nutzer() {
        data = new DataMap<>();
    }

    public static Nutzer fromJSON(DataMap<Unterkunft> unterkuenfte, JSONObject json) throws JSONException {
        // Instatiate new user
        Nutzer nutzer = new Nutzer();

        // put data to it from json object

        nutzer.id              = json.getInt("id");
        nutzer.name            = json.getString("name");
        nutzer.mail            = json.getString("mail");
        nutzer.rolle           = json.getInt("role");

        if(nutzer.rolle == 0) {
            nutzer.unterkunft = unterkuenfte.getFromID(json.getInt("accommodation_id"));

            JSONArray array = json.getJSONArray("needs");
            for (int i = 0; i < array.length(); i++) {
                nutzer.data.add(Bedarf.fromJSON(array.getJSONObject(i)));
            }
        } else {
            JSONArray array = json.getJSONArray("offers");
            for (int i = 0; i < array.length(); i++) {
                nutzer.data.add(Angebot.fromJSON(array.getJSONObject(i)));
            }
        }

        // Better trim Strings, get rid of white spaces
        nutzer.name = nutzer.name.trim();
        nutzer.mail = nutzer.mail.trim();

        return nutzer;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public void addBedarf(Bedarf bedarf) {
        this.data.add(bedarf);
        setChanged();
        notifyObservers();
    }

    public void loescheBedarf(int id) {
        data.remove(id);
        setChanged();
        notifyObservers();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Setters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }

    public void setMail(String mail) {
        this.mail = mail;
        setChanged();
        notifyObservers();
    }


    ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    public int getRolle() {
        return rolle;
    }

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
    public DataMap<IDObject> getData() {
        return data;
    }

    public boolean hatBedarf() {
        return data.size() > 0;
    }
}
