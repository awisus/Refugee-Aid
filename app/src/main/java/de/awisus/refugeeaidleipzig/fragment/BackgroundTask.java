package de.awisus.refugeeaidleipzig.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import de.awisus.refugeeaidleipzig.util.Utility;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public abstract class BackgroundTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected ProgressDialog ladebalken;
    protected Activity context;
    protected int textID;

    protected BackgroundTask(Activity context, int textID) {
        this.context = context;
        this.textID = textID;
    }

    @Override
    protected void onPreExecute() {
        ladebalken = Utility.getInstance().zeigeLadebalken(
                context, context.getResources().getString(textID));
    }

    @Override
    protected void onPostExecute(Result result) {
        doPostExecute(result);
        ladebalken.dismiss();
    }

    protected abstract void doPostExecute(Result result);
}
