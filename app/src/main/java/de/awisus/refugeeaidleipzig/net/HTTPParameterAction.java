package de.awisus.refugeeaidleipzig.net;

import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created on 13.03.16.
 *
 * @author jens
 */
public abstract class HTTPParameterAction extends HTTPAction {

    protected List<NameValuePair> parameter;

    protected HTTPParameterAction(String serverUrl) {
        super(serverUrl);
        parameter = new LinkedList<>();
    }

    public void addParameter(String key, String value) {
        parameter.add(new BasicNameValuePair(key, value));
    }

    protected String paramUrl(String url) {
        if(parameter.size() > 0) {
            url += "?";
            for(int i = 0; i < parameter.size(); i++) {
                url += parameter.get(i).toString();
                if(i < parameter.size() - 1) url += "&";
            }
        }
        return url;
    }
}
