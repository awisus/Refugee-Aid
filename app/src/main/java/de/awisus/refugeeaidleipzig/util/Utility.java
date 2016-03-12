package de.awisus.refugeeaidleipzig.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class Utility {

    public static final Utility INSTANCE = new Utility();

    public static Utility getInstance() {
        return INSTANCE;
    }

    public ProgressDialog zeigeLadebalken(Activity activity, String nachricht) {
        return ProgressDialog.show(activity, null, nachricht, true, false);
    }
}
