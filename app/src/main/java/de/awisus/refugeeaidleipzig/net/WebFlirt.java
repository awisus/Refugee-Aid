package de.awisus.refugeeaidleipzig.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.model.DataMap;
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


    private HTTPGetter httpGetter;
    private HTTPPoster httpPoster;


    private WebFlirt() {
        httpGetter = new HTTPGetter(SERVER_URL);
        httpPoster = new HTTPPoster(SERVER_URL);
    }

    public static WebFlirt getInstance() {
        return INSTANCE;
    }


    public DataMap<Unterkunft> getUnterkuenfte() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Unterkunft> unterkunftMap = new DataMap<>();

        httpGetter.execute("/accommodations/json");
        JSONArray feld = new JSONArray(httpGetter.get());
        for (int i = 0; i < feld.length(); i++) {
            int id;
            Unterkunft unterkunft;
            JSONObject unterkunftDaten;

            unterkunftDaten = feld.getJSONObject(i);
            unterkunft = Unterkunft.fromNetJSON(unterkunftDaten);
            id = unterkunftDaten.getInt("id");

            unterkunftMap.add(id, unterkunft);
        }

        return unterkunftMap;
    }

    public Nutzer getNutzer(Model model, String name, String passwort) throws JSONException, InterruptedException, ExecutionException {
        httpGetter.execute("getUser/" + name + "/" + passwort);
        return makeNutzer(model, httpGetter.get());
    }

    private Nutzer makeNutzer(Model model, String inhalt) throws JSONException {
        JSONObject object = new JSONObject(inhalt);
        return Nutzer.fromJSON(model, object);
    }
}
