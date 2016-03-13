package de.awisus.refugeeaidleipzig.net;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPPost extends HTTPParameterAction {

    public HTTPPost(String serverUrl) {
        super(serverUrl);
    }

    protected HttpResponse onPerform(HttpClient client, String... str) throws IOException {
        HttpPost httppost = new HttpPost(serverUrl +str[0]);
        httppost.addHeader("accept", "application/json");

        httppost.setEntity(new UrlEncodedFormEntity(parameter, "UTF-8"));

        return client.execute(httppost);
    }
}
