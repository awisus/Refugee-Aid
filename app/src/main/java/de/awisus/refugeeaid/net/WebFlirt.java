package de.awisus.refugeeaid.net;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class WebFlirt {

    public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";

    private WebFlirt() {}


    public static String get(String path) throws JSONException, InterruptedException, ExecutionException {
        path = path.replaceAll(" ", "%20");

        HTTPGet httpGet;
        httpGet = new HTTPGet(SERVER_URL);

        return httpGet.perform(path);
    }


    public static String patch(String path, String... parameter) {
        path = path.replaceAll(" ", "%20");

        HTTPPatch httpPatch;
        httpPatch = new HTTPPatch(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPatch.addParameter(parameter[i], parameter[i+1]);
        }

        return httpPatch.perform(path);
    }

    public static String post(String path, String... parameter) {
        path = path.replaceAll(" ", "%20");

        HTTPPost httpPost;
        httpPost = new HTTPPost(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPost.addParameter(parameter[i], parameter[i+1]);
        }

        return httpPost.perform(path);
    }

    public static String delete(String path, String... parameter) {
        path = path.replaceAll(" ", "%20");

        HTTPDelete httpDelete;
        httpDelete = new HTTPDelete(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpDelete.addParameter(parameter[i], parameter[i+1]);
        }

        return httpDelete.perform(path);
    }
}
