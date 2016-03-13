package de.awisus.refugeeaidleipzig.net;

import android.util.Log;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created on 13.03.16.
 *
 * @author jens
 */
public abstract class HTTPAction {

    protected String serverUrl;

    protected HTTPAction(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String perform(String... str) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpResponse response = onPerform(client, str);
            return postPerformance(response);
        }
        catch (IOException e) {
            Log.e("GET: Error", e.toString());
            return null;
        }
    }

    protected abstract HttpResponse onPerform(HttpClient client, String... str) throws IOException;

    protected String postPerformance(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }
}
