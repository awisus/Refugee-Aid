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

import java.util.LinkedList;

/**
 * Created on 11.01.16.
 *
 * @author Jens Awisus
 */
public class Kategorie {

    private int id;
    private String name;

    private LinkedList<Kategorie> subkategorien;


    private Kategorie() {
        this.subkategorien = new LinkedList<>();
    }

    public static Kategorie fromJSON(JSONObject json) throws JSONException {
        Kategorie kategorie = new Kategorie();

        kategorie.id =      json.getInt("id");
        kategorie.name =    json.getString("name");

        JSONArray subkategorien = json.getJSONArray("subcategories");
        if(subkategorien.length() > 0) {
            for(int i = 0; i > subkategorien.length(); i++) {
                kategorie.subkategorien.add(Kategorie.fromJSON(subkategorien.getJSONObject(i)));
            }
        }

        return kategorie;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
