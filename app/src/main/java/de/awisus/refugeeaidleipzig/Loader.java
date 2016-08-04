package de.awisus.refugeeaidleipzig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.models.Angebot;
import de.awisus.refugeeaidleipzig.models.DataMap;
import de.awisus.refugeeaidleipzig.models.Kategorie;
import de.awisus.refugeeaidleipzig.models.Unterkunft;
import de.awisus.refugeeaidleipzig.net.HTTPGet;

/**
 * Created on 18.03.16.
 *
 * @author jens
 */
public class Loader {

    // public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";
    public static final String SERVER_URL = "http://klaue:3000/";
    public static final Loader INSTANCE = new Loader();


    private Loader() {}

    public static Loader getInstance() {
        return INSTANCE;
    }


    public DataMap<Unterkunft> getUnterkuenfte() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Unterkunft> unterkunftMap = new DataMap<>();

        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        JSONArray feld = new JSONArray(httpGet.perform("accommodations/format/json"));
        for (int i = 0; i < feld.length(); i++) {
            JSONObject json = feld.getJSONObject(i);
            unterkunftMap.add(Unterkunft.fromJSON(json));
        }

        return unterkunftMap;
    }

    public DataMap<Angebot> getAngebote() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Angebot> angebotMap = new DataMap<>();

        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        JSONArray feld = new JSONArray(httpGet.perform("offers/format/json"));
        for (int i = 0; i < feld.length(); i++) {
            JSONObject json = feld.getJSONObject(i);
            angebotMap.add(Angebot.fromJSON(json));
        }

        return angebotMap;
    }

    public DataMap<Kategorie> getKategorien() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Kategorie> kategorieMap = new DataMap<>();

        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        JSONArray feld = new JSONArray(httpGet.perform("categories/format/json"));
        for (int i = 0; i < feld.length(); i++) {

            JSONObject json = feld.getJSONObject(i);

            kategorieMap.add(Kategorie.fromJSON(json));
        }

        return kategorieMap;
    }
}
