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
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.model.Unterkunft;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.BackgroundTask;
import de.awisus.refugeeaidleipzig.util.Datei;
import de.awisus.refugeeaidleipzig.util.MailValidator;
import de.awisus.refugeeaidleipzig.util.TextValidator;

/**
 * Created on 15.01.16.
 * <p/>
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 *
 * @author Jens Awisus
 */
public class FragmentEditUser extends FragmentAnmelden {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Spinner to choose in which accommodation the users stays in
     */
    private Spinner spUnterkunft;

    /**
     * Text field for the user name
     */
    private EditText etName;

    private EditText etMail;

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
    public static FragmentEditUser newInstance(Model model) {
        FragmentEditUser frag = new FragmentEditUser();
        frag.model = model;
        frag.nutzer = model.getNutzer();
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Inflates the layout, initialises text field and spinner, initialises the spinner adapter to
     * show up accommodations and sets the dialogue buttons
     *
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_user, null);

        warnungID = R.string.warnung_signup;

        spUnterkunft = (Spinner) view.findViewById(R.id.spUnterkunft);
        etName = (EditText) view.findViewById(R.id.etName);
        etMail = (EditText) view.findViewById(R.id.etMail);

        etName.setText(nutzer.getName());
        etMail.setText(nutzer.getMail());

        addListeners(view);
        initSpinnerAdapter();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.setTitle(R.string.titel_bearbeite_nutzer);

        return dialog;
    }

    private void addListeners(View view) {
        Button btSignup = (Button) view.findViewById(R.id.btSend);
        btSignup.setOnClickListener(new View.OnClickListener() {
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
                builder.setMessage("Are you sure?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });

        etName.addTextChangedListener(new TextValidator(etName) {
            @Override
            public void afterTextChanged(Editable s) {
                String name = etName.getText().toString();

                if (name.length() > 47) {
                    etName.setError(findString(R.string.warnung_name));
                }
            }
        });

        etMail.addTextChangedListener(new TextValidator(etMail) {
            @Override
            public void afterTextChanged(Editable s) {
                String mail = etMail.getText().toString();

                if (!MailValidator.getInstance().isValid(mail)) {
                    etMail.setError(findString(R.string.warnung_mail));
                }
            }
        });
    }

    private String findString(int index) {
        return getResources().getString(index);
    }

    /**
     * Private method that creates an Array Adapter the spinner uses to show accommodation names
     */
    private void initSpinnerAdapter() {

        LinkedList<Unterkunft> unterkuenfte;
        unterkuenfte = model.getUnterkuenfte().asList();
        Collections.sort(unterkuenfte);

        ArrayAdapter<Unterkunft> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                unterkuenfte
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spUnterkunft.setAdapter(adapter);
        spUnterkunft.setSelection(adapter.getPosition(nutzer.getUnterkunft()));
    }

    private void patch() {
        // Get inserted name and selected accommodation from views
        Unterkunft unterkunft = (Unterkunft) spUnterkunft.getSelectedItem();
        String name = etName.getText().toString();
        String mail = etMail.getText().toString();

        String id = String.valueOf(nutzer.getId());
        String unterkunftID = String.valueOf(unterkunft.getId());

        new NutzerPatch(getActivity(), R.string.meldung_anmelden).execute(
                "id",                       id,
                "name",                     name,
                "mail",                     mail,
                "accommodation_id",         unterkunftID);
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
