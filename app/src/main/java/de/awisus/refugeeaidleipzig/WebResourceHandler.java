package de.awisus.refugeeaidleipzig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.model.Unterkunft;
import de.awisus.refugeeaidleipzig.model.UnterkunftMap;
import de.awisus.refugeeaidleipzig.net.HTTPHandler;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class WebResourceHandler {

    public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";

    private HTTPHandler httpHandler;

    public WebResourceHandler() {
        httpHandler = new HTTPHandler(SERVER_URL);
    }

    public UnterkunftMap ladeUnterkuenfte() throws IOException, JSONException {
        UnterkunftMap unterkunftMap = new UnterkunftMap();

        String inhalt = httpHandler.get("/accommodations/json");
        JSONArray feld = new JSONArray(inhalt);

        for(int i = 0; i < feld.length(); i++) {
            int id;
            Unterkunft unterkunft;
            JSONObject unterkunftDaten;

            unterkunftDaten = feld.getJSONObject(i);
            unterkunft = Unterkunft.fromJSON(unterkunftDaten);
            id = unterkunftDaten.getInt("id");

            unterkunftMap.add(id, unterkunft);
        }

        return unterkunftMap;
    }
}
