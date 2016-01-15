package de.awisus.refugeeaidleipzig;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import de.awisus.refugeeaidleipzig.model.Kategorie;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created by Jens Awisus on 12.01.16.
 */
public class RessourcenLader {

    private Activity activity;

    private Unterkunft[] unterkuenfte;
    private Kategorie[] kategorien;

    public RessourcenLader(Activity activity) throws IOException, JSONException {
        this.activity = activity;

        ladeUnterkuenfte();
        ladeKategorien();
    }


    private void ladeUnterkuenfte() throws IOException, JSONException {
        Unterkunft[] unterkuenfte;

        // Datei mit den Unterkuenften auslesen
        String inhalt = lesen(R.raw.unterkuenfte);

        // Unterkuenfte aus der Datei lesen und initialisieren
        JSONObject json = new JSONObject(inhalt);
        JSONArray feld = json.getJSONArray("unterkuenfte");

        unterkuenfte = new Unterkunft[feld.length()];

        for(int i = 0; i < feld.length(); i++) {
            unterkuenfte[i] = Unterkunft.fromJSON(feld.getJSONObject(i));
        }

        this.unterkuenfte = unterkuenfte;
    }

    private void ladeKategorien() throws IOException, JSONException {
        Kategorie[] kategorien;

        String inhalt = lesen(R.raw.kategorien);

        JSONObject json = new JSONObject(inhalt);
        JSONArray feld = json.getJSONArray("kategorien");

        kategorien = new Kategorie[feld.length()];

        for(int i = 0; i < feld.length(); i++) {
            kategorien[i] = new Kategorie(feld.getJSONObject(i).getString("bezeichnung"));
        }

        this.kategorien = kategorien;
    }


    public Unterkunft[] getUnterkuenfte() {
        return unterkuenfte;
    }

    public Kategorie[] getKategorien() {
        return kategorien;
    }


    private String lesen(int dateiID) throws IOException {

        InputStream is = activity.getResources().openRawResource(dateiID);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer).trim();
    }
}
