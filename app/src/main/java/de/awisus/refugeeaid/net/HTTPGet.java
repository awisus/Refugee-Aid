package de.awisus.refugeeaid.net;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPGet extends HTTPAction {

    public HTTPGet(String serverUrl) {
        super(serverUrl);
    }

    @Override
    protected HttpResponse onPerform(HttpClient client, String... str) throws IOException {
        HttpGet httpget = new HttpGet(serverUrl +str[0]);
        httpget.addHeader("accept", "application/json");

        return client.execute(httpget);
    }
}
