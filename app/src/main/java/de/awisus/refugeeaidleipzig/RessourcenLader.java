package de.awisus.refugeeaidleipzig;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import de.awisus.refugeeaidleipzig.model.Kategorie;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created on 12.01.16.
 *
 * This class is responsible for getting data stored locally as json files.
 * These json files are stores in the /raw resources directory and can be read by using an Activity
 * of the app. This is necessary to gain access to the raw resources.
 * @author Jens Awisus
 */
public class RessourcenLader {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * An Activity to get raw resources (json files) from
     */
    private Activity activity;

    /**
     * List of accommodations
     */
    private LinkedList<Unterkunft> unterkuenfte;

    /**
     * Array of categories of needs
     */
    private Kategorie[] kategorien;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public constructor.
     * Sets the Activity and starts reading of the local json files.
     * @param activity Activity to gain access to raw resources
     * @throws IOException Exception, if something is wrong with a file
     * @throws JSONException Exception, if there occurs a mistake while working with json objects
     */
    public RessourcenLader(Activity activity) throws IOException, JSONException {
        this.activity = activity;
        this.unterkuenfte = new LinkedList<>();

        ladeUnterkuenfte();
        ladeKategorien();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * This method loads the accommodation's json file.
     * This runs through all accommodations lying in an array and creates instances of
     * accommodations corresponding to the data read
     * @throws IOException Exception, if something is wrong with a file
     * @throws JSONException Exception, if there occurs a mistake while working with json objects
     */
    private void ladeUnterkuenfte() throws IOException, JSONException {
        // Get content of accommodation's json file
        String inhalt = lesen(R.raw.unterkuenfte);

        // Initialise a json array by the accommodation's array name
        JSONObject json = new JSONObject(inhalt);
        JSONArray feld = json.getJSONArray("unterkuenfte");

        // Put a newly created accommodation to their list
        for(int i = 0; i < feld.length(); i++) {
            unterkuenfte.add(Unterkunft.fromJSON(feld.getJSONObject(i)));
        }
    }

    /**
     * This method loads the category's json file.
     * This runs through all categories lying in an array and creates instances of categories
     * corresponding to the data read
     * @throws IOException Exception, if something is wrong with a file
     * @throws JSONException Exception, if there occurs a mistake while working with json objects
     */
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

    /**
     * Private method that generally reads any raw json file by a given resource id
     * @param dateiID resource id of the desired json file
     * @return String with the content got from file
     * @throws IOException Exception, if something is wrong with the file
     */
    private String lesen(int dateiID) throws IOException {

        // Opening an input stream by resource id
        InputStream is = activity.getResources().openRawResource(dateiID);

        // Check file length
        int size = is.available();
        byte[] buffer = new byte[size];

        // read the content and store in the buffer
        is.read(buffer);
        is.close();

        // Build new String from buffer and trim that -> NullPointerExc?!
        return new String(buffer).trim();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter for the accommodation array
     * @return accommodation array
     */
    public LinkedList<Unterkunft> getUnterkuenfte() {
        return unterkuenfte;
    }

    /**
     * Getter for the category array
     * @return category array
     */
    public Kategorie[] getKategorien() {
        return kategorien;
    }
}
