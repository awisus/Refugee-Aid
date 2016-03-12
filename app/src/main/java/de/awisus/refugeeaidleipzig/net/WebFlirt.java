package de.awisus.refugeeaidleipzig.net;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.model.DataMap;
import de.awisus.refugeeaidleipzig.model.Kategorie;
import de.awisus.refugeeaidleipzig.model.Model;
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


    private WebFlirt() {
    }

    public static WebFlirt getInstance() {
        return INSTANCE;
    }


    public DataMap<Unterkunft> getUnterkuenfte() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Unterkunft> unterkunftMap = new DataMap<>();

        HTTPGetter httpGetter;
        httpGetter = new HTTPGetter(SERVER_URL);

        JSONArray feld = new JSONArray(httpGetter.get("accommodations/json")); Log.d("Antwort", feld.toString(4));
        for (int i = 0; i < feld.length(); i++) {
            int id;
            Unterkunft unterkunft;
            JSONObject json;

            json = feld.getJSONObject(i);
            id = json.getInt("id");
            unterkunft = Unterkunft.fromJSON(json);

            unterkunftMap.add(id, unterkunft);
        }

        return unterkunftMap;
    }

    public DataMap<Kategorie> getKategorien() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Kategorie> kategorieMap = new DataMap<>();

        HTTPGetter httpGetter;
        httpGetter = new HTTPGetter(SERVER_URL);

        JSONArray feld = new JSONArray(httpGetter.get("categories/json"));
        for (int i = 0; i < feld.length(); i++) {
            int id;
            Kategorie kategorie;
            JSONObject json;

            json = feld.getJSONObject(i);
            id = json.getInt("id");
            kategorie = Kategorie.fromJSON(json);

            kategorieMap.add(id, kategorie);
        }

        return kategorieMap;
    }

    public Nutzer getNutzer(Model model, String name, String passwort) throws JSONException, InterruptedException, ExecutionException {
        HTTPGetter httpGetter;
        httpGetter = new HTTPGetter(SERVER_URL);

        return makeNutzer(model, httpGetter.get("getUser/" + name + "/" + passwort));
    }

    public Nutzer postNutzer(Model model, String... parameter) throws JSONException, InterruptedException, ExecutionException {
        HTTPPoster httpPoster;
        httpPoster = new HTTPPoster(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPoster.addParameter(parameter[i], parameter[i+1]);
        }
        httpPoster.execute("users/remote");

        return makeNutzer(model, httpPoster.get());
    }

    private Nutzer makeNutzer(Model model, String inhalt) throws JSONException {
        return Nutzer.fromJSON(model, new JSONObject(inhalt));
    }
}
