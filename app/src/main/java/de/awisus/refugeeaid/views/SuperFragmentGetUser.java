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

package de.awisus.refugeeaid.views;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import de.awisus.refugeeaid.LoginData;
import de.awisus.refugeeaid.ViewModel;
import de.awisus.refugeeaid.models.Nutzer;
import de.awisus.refugeeaid.util.BackgroundTask;
import de.awisus.refugeeaid.util.Datei;

/**
 * Created on 12.03.16.
 *
 * @author Jens Awisus
 */
public abstract class SuperFragmentGetUser extends DialogFragment {

    /**
     * Reference to the model to log in the new user (or to be found in the chosen accommodation)
     */
    protected ViewModel model;

    protected int warnungID;

    protected abstract class NutzerGet extends BackgroundTask<String, Integer, Nutzer> {

        private LoginData login;

        protected NutzerGet(Activity context, int textID, LoginData login) {
            super(context, textID);
            this.login = login;
        }

        @Override
        protected void doPostExecute(Nutzer result) {

            if(result == null) {
                Toast.makeText(context, warnungID, Toast.LENGTH_SHORT).show();
            } else {
                model.anmelden(result);
                dismiss();

                // save login data
                try {
                    Datei.getInstance().schreiben(context, "login.json", new Gson().toJson(login));
                } catch (IOException e) {
                    Log.e("Anmelden", "Fehler beim Speichern der Logindatei");
                }
            }
        }
    }
}
