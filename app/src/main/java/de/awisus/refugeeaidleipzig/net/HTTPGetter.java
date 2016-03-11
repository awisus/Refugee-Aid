package de.awisus.refugeeaidleipzig.net;

import android.os.AsyncTask;
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
public class HTTPGetter extends AsyncTask<String, Void, String> {

    private String serverUrl;

    public HTTPGetter(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    protected String doInBackground(String... str) {
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
        return "Cannot Connect";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
