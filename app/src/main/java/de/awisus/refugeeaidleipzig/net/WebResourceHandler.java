package de.awisus.refugeeaidleipzig.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.model.DataMap;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class WebResourceHandler {

    public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";


    private HTTPGetter httpGetter;


    public WebResourceHandler() {
        httpGetter = new HTTPGetter(SERVER_URL);
    }


    public DataMap<Unterkunft> ladeUnterkuenfte() throws IOException, JSONException, InterruptedException, ExecutionException {

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
}
