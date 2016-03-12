package de.awisus.refugeeaidleipzig.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public abstract class Utility {

    public static ProgressDialog zeigeLadebalken(Activity activity, String nachricht) {
        ProgressDialog ladebalken;
        ladebalken = new ProgressDialog(activity);
        ladebalken.setMessage(nachricht);
        ladebalken.setCancelable(false);
        ladebalken.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ladebalken.setIndeterminate(true);
        ladebalken.show();
        return ladebalken;
    }
}
