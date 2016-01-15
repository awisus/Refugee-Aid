package de.awisus.refugeeaidleipzig.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Jens Awisus on 11.01.16.
 */
public class Unterkunft {

    private String name;
    private LatLng latLng;

    private int groesse;
    private LinkedList<Nutzer> bewohner;


    private Unterkunft() {
        bewohner = new LinkedList<>();
    }

    public static Unterkunft fromJSON(JSONObject json) throws JSONException {
        Unterkunft unterkunft;

        if(json == null) {
            return null;
        } else {
            unterkunft = new Unterkunft();
            unterkunft.name = json.getString("name");
            unterkunft.groesse = json.getInt("groesse");
            unterkunft.latLng = new LatLng(json.getDouble("laengengrad"), json.getDouble("breitengrad"));
        }

        return unterkunft;
    }


    public void anmelden(Nutzer nutzer) {
        bewohner.add(nutzer);
    }

    public String getName() {
        return name;
    }

    public int getGroesse() {
        return groesse;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getBedarfeAlsString() {
        if(!hatBedarf()) {
            return null;
        } else {
            String str = "";
            for(int i = 0; i < bewohner.size(); i++) {
                str += bewohner.get(i).getBedarfeAlsString();
                if(i < bewohner.size() - 1) {
                    str += ", ";
                }
            }

            return str;
        }
    }

    public LinkedList<Nutzer> getNutzer() {
        return bewohner;
    }

    public boolean hatBedarf() {
        for(Nutzer nutzer : bewohner) {
            if(nutzer.hatBedarf()) {
                return true;
            }
        }

        return false;
    }
}
