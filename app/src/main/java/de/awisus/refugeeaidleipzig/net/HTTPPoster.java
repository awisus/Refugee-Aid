package de.awisus.refugeeaidleipzig.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPPoster extends AsyncTask<String, Void, String> {

    private String serverUrl;
    private List<NameValuePair> parameter;

    public HTTPPoster(String serverUrl) {
        this.serverUrl = serverUrl;
        this.parameter = new LinkedList<>();
    }

    public void addParameter(String key, String value) {
        parameter.add(new BasicNameValuePair(key, value));
    }

    @Override
    protected String doInBackground(String... str) {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(serverUrl +str[0]);
            httppost.addHeader("accept", "application/json");

            httppost.setEntity(new UrlEncodedFormEntity(parameter, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);

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
        parameter.clear();
    }
}
