package de.awisus.refugeeaidleipzig.net;

import android.util.Log;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPGetter {

    private String serverUrl;

    public HTTPGetter(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String get(String... str) {
        try {
            HttpClient client = HttpClients.createDefault();

            HttpGet request = new HttpGet(serverUrl +str[0]);
            request.addHeader("accept", "application/json");

            HttpResponse response = client.execute(request);

            return EntityUtils.toString(response.getEntity());
        }
        catch (IOException e) {
            Log.e("GET: Error", e.toString());
        }
        return null;
    }
}
