package de.awisus.refugeeaidleipzig.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.model.DataMap;
import de.awisus.refugeeaidleipzig.model.Kategorie;
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


    public DataMap<Unterkunft> getUnterkuenfte() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Unterkunft> unterkunftMap = new DataMap<>();

        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        JSONArray feld = new JSONArray(httpGet.perform("accommodations/json"));
        for (int i = 0; i < feld.length(); i++) {

            JSONObject json = feld.getJSONObject(i);

            unterkunftMap.add(Unterkunft.fromJSON(json));
        }

        return unterkunftMap;
    }

    public DataMap<Kategorie> getKategorien() throws IOException, JSONException, InterruptedException, ExecutionException {

        DataMap<Kategorie> kategorieMap = new DataMap<>();

        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        JSONArray feld = new JSONArray(httpGet.perform("categories/json"));
        for (int i = 0; i < feld.length(); i++) {

            JSONObject json = feld.getJSONObject(i);

            kategorieMap.add(Kategorie.fromJSON(json));
        }

        return kategorieMap;
    }


    public String get(String path) throws JSONException, InterruptedException, ExecutionException {
        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        return httpGet.perform(path);
    }


    public String patch(String path, String... parameter) {
        HTTPPatch httpPatch;
        httpPatch = new HTTPPatch(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPatch.addParameter(parameter[i], parameter[i+1]);
        }

        return httpPatch.perform(path);
    }

    public String post(String path, String... parameter) {
        HTTPPost httpPost;
        httpPost = new HTTPPost(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPost.addParameter(parameter[i], parameter[i+1]);
        }

        return httpPost.perform(path);
    }

    public String delete(String path, String... parameter) {
        HTTPDelete httpDelete;
        httpDelete = new HTTPDelete(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpDelete.addParameter(parameter[i], parameter[i+1]);
        }

        return httpDelete.perform(path);
    }
}
