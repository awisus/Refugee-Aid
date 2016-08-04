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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by on 11.01.16.
 *
 * @author Jens Awisus
 */
public class Bedarf extends ImageDataObject {

    private Bedarf() {}

    public static Bedarf fromJSON(JSONObject json) throws JSONException {
        // Instatiate new user
        Bedarf bedarf = new Bedarf();

        // put data to it from json object
        bedarf.id              = json.getInt("id");
        bedarf.name            = json.getString("name");
        bedarf.imageData       = json.getString("imagedata");

        // Better trim Strings, perform rid of white spaces
        bedarf.name = bedarf.name.trim();

        return bedarf;
    }
}
