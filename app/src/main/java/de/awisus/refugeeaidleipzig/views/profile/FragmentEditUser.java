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

package de.awisus.refugeeaidleipzig.views.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.LoginData;
import de.awisus.refugeeaidleipzig.ViewModel;
import de.awisus.refugeeaidleipzig.models.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.BackgroundTask;
import de.awisus.refugeeaidleipzig.util.Datei;
import de.awisus.refugeeaidleipzig.views.SuperFragmentEditUser;

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

    private TextView deleteName, deleteMail, deletePasswort, deleteConfirmation;

    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
     *
     * @param model ViewModel to log in user
     * @return new Login Fragment
     */
    public static FragmentEditUser newInstance(ViewModel model, int titelID, int layoutID) {
        FragmentEditUser frag = new FragmentEditUser();
        frag.model = model;
        frag.nutzer = model.getNutzer();
        frag.layoutID = layoutID;
        frag.titelID = titelID;
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog) getDialog();

        if(dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    patch();
                }
            });
        }
    }

    @Override
    protected void initElements(View view) {
        super.initElements(view);

        deleteName = (TextView) view.findViewById(R.id.btDeleteName);
        deleteMail = (TextView) view.findViewById(R.id.btDeleteMail);
        deletePasswort = (TextView) view.findViewById(R.id.btDeletePasswort);
        deleteConfirmation = (TextView) view.findViewById(R.id.btDeleteConfirmation);

        setTexts();
    }

    private void setTexts() {
        try {
            etName.setText(nutzer.getName());
            etMail.setText(nutzer.getMail());

            String datei = Datei.getInstance().lesen(getActivity(), "login.json");
            LoginData login = new Gson().fromJson(datei, LoginData.class);

            etPasswort.setText(login.getPasswort());
            etConfirmation.setText(login.getPasswort());
        } catch (IOException e) {
            Log.e("Laden", "Fehler beim Lesen der Logindatei");
        }
    }

    @Override
    protected void setButtonListeners(View view) {
        deleteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText(null);
            }
        });
        deleteMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMail.setText(null);
            }
        });
        deletePasswort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPasswort.setText(null);
                etConfirmation.setText(null);
            }
        });
        deleteConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etConfirmation.setText(null);
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

        String id = String.valueOf(nutzer.getId());
        String name = etName.getText().toString();
        String mail = etMail.getText().toString();
        String passwort = etPasswort.getText().toString();
        String confirmation = etConfirmation.getText().toString();

        new NutzerPatch(getActivity(), R.string.meldung_aktualisieren, new LoginData(name, mail, passwort))
                .execute(
                        "id",                       id,
                        "name",                     name,
                        "mail",                     mail,
                        "password",                 passwort,
                        "password_confirmation",    confirmation);
    }

    private class NutzerPatch extends BackgroundTask<String, String, Nutzer> {

        private LoginData login;

        NutzerPatch(Activity context, int textID, LoginData login) {
            super(context, textID);
            this.login = login;
        }

        @Override
        protected Nutzer doInBackground(String... params) {
            String antwort = WebFlirt.patch("users_remote", params);
            return antwort.equals("OK") ? nutzer : null;
        }

        @Override
        protected void doPostExecute(Nutzer result) {
            if(result == null) {
                Toast.makeText(context, R.string.warnung_signup, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Datei.getInstance().schreiben(context, "login.json", new Gson().toJson(login));

                    nutzer.setName(login.getName());
                    nutzer.setMail(login.getMail());
                } catch (IOException e) {
                    Log.e("Anmelden", "Fehler beim Speichern der Logindatei");
                }
                dismiss();
            }
        }
    }

    private void delete() {
        String id = String.valueOf(nutzer.getId());
        new NutzerDelete(getActivity(), R.string.meldung_entfernen).execute("id", id);
    }

    private class NutzerDelete extends BackgroundTask<String, Integer, String> {

        NutzerDelete(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected String doInBackground(String... params) {
            String antwort = WebFlirt.delete("users_remote", params);
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
