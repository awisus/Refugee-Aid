package de.awisus.refugeeaidleipzig.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.model.DataMap;
import de.awisus.refugeeaidleipzig.model.Kategorie;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class WebFlirt {

    public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";
    public static final WebFlirt INSTANCE = new WebFlirt();


    private WebFlirt() {}

    public static WebFlirt getInstance() {
        return INSTANCE;
    }


    public DataMap<Unterkunft> getUnterkuenfte(DataMap<Kategorie> kategorien) throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Unterkunft> unterkunftMap = new DataMap<>();

        HTTPGetter httpGetter;
        httpGetter = new HTTPGetter(SERVER_URL);

        JSONArray feld = new JSONArray(httpGetter.get("accommodations/json"));
        for (int i = 0; i < feld.length(); i++) {

            JSONObject json = feld.getJSONObject(i);

            unterkunftMap.add(json.getInt("id"), Unterkunft.fromJSON(kategorien, json));
        }

        return unterkunftMap;
    }

    public DataMap<Kategorie> getKategorien() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Kategorie> kategorieMap = new DataMap<>();

        HTTPGetter httpGetter;
        httpGetter = new HTTPGetter(SERVER_URL);

        JSONArray feld = new JSONArray(httpGetter.get("categories/json"));
        for (int i = 0; i < feld.length(); i++) {

            JSONObject json = feld.getJSONObject(i);

            kategorieMap.add(json.getInt("id"), Kategorie.fromJSON(json));
        }

        return kategorieMap;
    }

    public Nutzer getNutzer(DataMap<Unterkunft> unterkuenfte, DataMap<Kategorie> kategorien, String name, String passwort) throws JSONException, InterruptedException, ExecutionException {
        HTTPGetter httpGetter;
        httpGetter = new HTTPGetter(SERVER_URL);

        return makeNutzer(unterkuenfte, kategorien, httpGetter.get("getUser/" + name + "/" + passwort));
    }

    public Nutzer postNutzer(DataMap<Unterkunft> unterkuenfte, DataMap<Kategorie> kategorien, String... parameter) throws JSONException, InterruptedException, ExecutionException {
        HTTPPoster httpPoster;
        httpPoster = new HTTPPoster(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPoster.addParameter(parameter[i], parameter[i+1]);
        }

        return makeNutzer(unterkuenfte, kategorien, httpPoster.post("users/remote"));
    }

    private Nutzer makeNutzer(DataMap<Unterkunft> unterkuenfte, DataMap<Kategorie> kategorien, String inhalt) throws JSONException {
        return Nutzer.fromJSON(unterkuenfte, kategorien, new JSONObject(inhalt));
    }
}
