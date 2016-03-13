package de.awisus.refugeeaidleipzig.net;

import android.util.Log;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPDelete extends HTTPParameterAction {

    public HTTPDelete(String serverUrl) {
        super(serverUrl);
    }

    public String perform(String url) {
        try {

            HttpClient httpclient = HttpClients.createDefault();
            HttpDelete httpdelete = new HttpDelete(serverUrl + url);
            httpdelete.addHeader("accept", "application/json");
            
            HttpResponse response = httpclient.execute(httpdelete);

            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            Log.e("GET: Error", e.toString());
            return null;
        } finally {
            parameter.clear();
        }
    }

    protected HttpResponse onPerform(HttpClient client, String... str) throws IOException {
        HttpDelete httpdelete = new HttpDelete(serverUrl +str[0]);
        httpdelete.addHeader("accept", "application/json");

        return client.execute(httpdelete);
    }
}
