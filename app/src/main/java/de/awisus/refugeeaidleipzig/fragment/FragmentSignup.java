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
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.LoginData;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.model.Unterkunft;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.MailValidator;
import de.awisus.refugeeaidleipzig.util.TextValidator;

/**
 * Created on 15.01.16.
 *
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 * @author Jens Awisus
 */
public class FragmentSignup extends FragmentAnmelden {

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

    private EditText etPasswort;

    private EditText etConformation;


      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
     * @param model Model to log in user
     * @return new Login Fragment
     */
    public static FragmentSignup newInstance(Model model) {
        FragmentSignup frag = new FragmentSignup();
        frag.model = model;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Inflates the layout, initialises text field and spinner, initialises the spinner adapter to
     * show up accommodations and sets the dialogue buttons
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_signup, null);

        warnungID = R.string.warnung_signup;

        spUnterkunft = (Spinner) view.findViewById(R.id.spUnterkunft);
        etName = (EditText) view.findViewById(R.id.etName);
        etMail = (EditText) view.findViewById(R.id.etMail);
        etPasswort = (EditText) view.findViewById(R.id.etPassword);
        etConformation = (EditText) view.findViewById(R.id.etConfirmation);

        Button btSignup = (Button) view.findViewById(R.id.btSignup);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        addListeners();
        initSpinnerAdapter();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.setTitle(R.string.titel_signup);

        return dialog;
    }

    private void addListeners() {
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
        etPasswort.addTextChangedListener(new TextValidator(etPasswort) {
            @Override
            public void afterTextChanged(Editable s) {
                String passwort = etPasswort.getText().toString();
                String confirmation = etConformation.getText().toString();

                if (passwort.length() < 6) {
                    etPasswort.setError(findString(R.string.warnung_passwort));
                    return;
                }
                if(!confirmation.isEmpty()) {
                    if(confirmation.equals(passwort)) {
                        etConformation.setError(null);
                    } else {
                        etConformation.setError(findString(R.string.warnung_confirmation));
                    }
                }
            }
        });
        etConformation.addTextChangedListener(new TextValidator(etConformation) {
            @Override
            public void afterTextChanged(Editable s) {
                String password = etPasswort.getText().toString();
                String confirmation = etConformation.getText().toString();

                if (!password.equals(confirmation)) {
                    etConformation.setError(findString(R.string.warnung_confirmation));
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
    }

    private void signup() {
        // Get inserted name and selected accommodation from views
        Unterkunft unterkunft = (Unterkunft) spUnterkunft.getSelectedItem();

        String name = etName.getText().toString();
        String passwort = etPasswort.getText().toString();

        new NutzerPost(getActivity(), R.string.meldung_anmelden, new LoginData(name, passwort)).execute(
                "name",                     name,
                "mail",                     etMail.getText().toString(),
                "password",                 passwort,
                "password_confirmation",    etConformation.getText().toString(),
                "accommodation_id",         String.valueOf(unterkunft.getId()));
    }

    private class NutzerPost extends FragmentLogin.NutzerGet {

        public NutzerPost(Activity context, int textID, LoginData login) {
            super(context, textID, login);
        }

        @Override
        protected Nutzer doInBackground(String... params) {
            try {
                String antwort = WebFlirt.getInstance().create("users_remote", params);
                Nutzer nutzer = Nutzer.fromJSON(model.getUnterkuenfte(), new JSONObject(antwort));

                return nutzer;
            } catch (JSONException e){
                return null;
            }
        }
    }
}
