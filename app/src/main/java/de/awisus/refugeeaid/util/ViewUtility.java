package de.awisus.refugeeaid.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created on 11.03.16.
 *
 * @author Jens Awisus
 */
public class ViewUtility {

    private ViewUtility() {}

    public static ProgressDialog zeigeLadebalken(Activity activity, String nachricht) {
        return ProgressDialog.show(activity, null, nachricht, true, false);
    }
}
