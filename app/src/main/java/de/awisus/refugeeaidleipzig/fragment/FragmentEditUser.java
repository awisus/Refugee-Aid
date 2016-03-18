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
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.BackgroundTask;
import de.awisus.refugeeaidleipzig.util.Datei;

/**
 * Created on 15.01.16.
 * <p/>
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 *
 * @author Jens Awisus
 */
public class FragmentEditUser extends SuperFragmentEditUser {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
     *
     * @param model Model to log in user
     * @return new Login Fragment
     */
    public static FragmentEditUser newInstance(Model model, int titelID, int layoutID) {
        FragmentEditUser frag = new FragmentEditUser();
        frag.model = model;
        frag.nutzer = model.getNutzer();
        frag.layoutID = layoutID;
        frag.titelID = titelID;
        return frag;
    }

    @Override
    protected void initElements(View view) {
        super.initElements(view);
        setTexts();
    }

    private void setTexts() {
        etName.setText(nutzer.getName());
        etMail.setText(nutzer.getMail());
    }

    @Override
    protected void setButtonListeners(View view) {
        Button btExecute = (Button) view.findViewById(R.id.btExecute);
        btExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patch();
            }
        });

        Button btDeleteAccount = (Button) view.findViewById(R.id.btDeleteAccount);
        btDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dialog_sicher);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.dialog_ja, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete();
                    }
                });
                builder.setNegativeButton(R.string.dialog_nein, null);
                builder.show();
            }
        });
    }


    private void patch() {
        // Get inserted name and selected accommodation from views
        String name = etName.getText().toString();
        String mail = etMail.getText().toString();

        String id = String.valueOf(nutzer.getId());

        new NutzerPatch(getActivity(), R.string.meldung_anmelden).execute(
                "id",                       id,
                "name",                     name,
                "mail",                     mail);
    }

    private class NutzerPatch extends BackgroundTask<String, String, String> {

        public NutzerPatch(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected String doInBackground(String... params) {
            String antwort = WebFlirt.getInstance().patch("users_remote", params);

            return antwort.equals("OK") ? antwort : null;
        }

        @Override
        protected void doPostExecute(String result) {
            if(result == null) {
                Toast.makeText(context, R.string.warnung_fehler, Toast.LENGTH_SHORT).show();
            } else {
                // IMPLEMENT LOGIC
                dismiss();
            }
        }
    }

    private void delete() {
        // Get inserted name and selected accommodation from views
        int id = model.getNutzer().getId();
        new NutzerDelete(getActivity(), R.string.meldung_entfernen).execute("id", "" + id);
    }

    private class NutzerDelete extends BackgroundTask<String, Integer, String> {

        public NutzerDelete(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected String doInBackground(String... params) {
            String antwort = WebFlirt.getInstance().delete("users_remote", params);
            return antwort.equals("OK") ? antwort : null;
        }

        @Override
        protected void doPostExecute(String result) {
            if(result == null) {
                Toast.makeText(context, R.string.warnung_fehler, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Datei.getInstance().loeschen(getActivity(), "login.json");
                } catch (IOException e) {
                    Log.e("Abmelden", "Fehler beim Löschen der Nutzerdaten");
                }

                dismiss();

                model.abmelden();
            }
        }
    }
}
