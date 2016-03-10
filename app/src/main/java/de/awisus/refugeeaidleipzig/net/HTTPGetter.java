package de.awisus.refugeeaidleipzig.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created on 10.03.16.
 *
 * @author Jens Awisus
 */
public class HTTPGetter {

    public String get(String url) throws IOException {

        HttpClient client = HttpClients.createDefault();

        HttpGet request = new HttpGet(url);
        request.addHeader("accept", "application/json");

        HttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity());
    }
}
