package de.awisus.refugeeaidleipzig.net;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPatch;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPPatch extends HTTPParameterAction {

    public HTTPPatch(String serverUrl) {
        super(serverUrl);
    }

    @Override
    protected HttpResponse onPerform(HttpClient client, String... str) throws IOException {
        HttpPatch httpPatch = new HttpPatch(serverUrl +str[0]);
        httpPatch.setHeader("accept", "application/json");

        httpPatch.setEntity(new UrlEncodedFormEntity(parameter, "UTF-8"));

        return client.execute(httpPatch);
    }
}
