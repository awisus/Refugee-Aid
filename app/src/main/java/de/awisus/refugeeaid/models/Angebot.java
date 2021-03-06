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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by on 2.8.16.
 *
 * @author Jens Awisus
 */
public class Angebot extends ImageDataObject implements ILocationDataObject {

    private String content;
    private LatLng latLng;

    private Angebot() {}

    public static Angebot fromJSON(JSONObject json) throws JSONException {
        // Instatiate new user
        Angebot angebot = new Angebot();

        // put data to it from json object
        angebot.id              = json.getInt("id");
        angebot.name            = json.getString("title");
        angebot.content         = json.getString("text");
        angebot.imageData       = json.getString("image");
        angebot.latLng = new LatLng(
                json.getDouble("latitude"),
                json.getDouble("longitude")
        );

        // Better trim Strings, perform rid of white spaces
        angebot.name = angebot.name.trim();
        angebot.content = angebot.content.trim();

        return angebot;
    }

    public void setTitle(String title) {
        this.name = title;
        this.name = this.name.trim();
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public void setContent(String content) {
        this.content = content;
        this.content = this.content.trim();
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getContent() {
        return content;
    }

    @Override
    public LatLng getLatLng() {
        return latLng;
    }
}
