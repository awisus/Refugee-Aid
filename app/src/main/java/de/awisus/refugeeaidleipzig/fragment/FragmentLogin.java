/*
 * Copyright 2016 Jens Awisus <awisus.gdev@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package de.awisus.refugeeaidleipzig.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import de.awisus.refugeeaidleipzig.MainActivity;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.util.Utility;

/**
 * Created on 12.03.16.
 *
 * @author Jens Awisus
 */
public abstract class FragmentLogin extends DialogFragment {

    /**
     * Activity as the Context for the accommodation spinner
     */
    protected MainActivity context;

    /**
     * Reference to the model to log in the new user (or to be found in the chosen accommodation)
     */
    protected Model model;

    protected int warnungID;

    /**
     * This is called to set the parent Activity as the Context of the Fragment
     *
     * @param activity Activity to be set as context
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (MainActivity) activity;
    }

    protected abstract class NutzerGet extends AsyncTask<String, Integer, Nutzer> {

        protected ProgressDialog ladebalken;

        @Override
        protected void onPreExecute() {
            ladebalken = Utility.getInstance().zeigeLadebalken(context, getResources().getString(R.string.meldung_anmelden));
        }

        @Override
        protected void onPostExecute(Nutzer result) {

            if(result == null) {
                Toast.makeText(context, warnungID, Toast.LENGTH_SHORT).show();
            } else {
                model.anmelden(result);
            }

            dismiss();
            ladebalken.dismiss();
        }
    }
}
