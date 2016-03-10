package de.awisus.refugeeaidleipzig.net;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPHandler {

    private String serverUrl;

    public HTTPHandler(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    // "users/remote"
    public String get(String url) throws IOException {

        HttpClient client = HttpClients.createDefault();

        HttpGet request = new HttpGet(serverUrl +url);
        request.addHeader("accept", "application/json");

        HttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }

    // "needs/remote"
    public String post(String url, List<NameValuePair> parameter) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(serverUrl +url);
        httppost.addHeader("accept", "application/json");

        httppost.setEntity(new UrlEncodedFormEntity(parameter, "UTF-8"));

        HttpResponse response = httpclient.execute(httppost);

        return EntityUtils.toString(response.getEntity());
    }

    // "needs/remote?user_id=1&category_id=1"
    public String destroy(String url) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(serverUrl +url);
        delete.addHeader("accept", "application/json");

        HttpResponse response = httpclient.execute(delete);

        return EntityUtils.toString(response.getEntity());
    }
}
