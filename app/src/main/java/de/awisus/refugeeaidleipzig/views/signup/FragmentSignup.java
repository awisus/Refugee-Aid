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

package de.awisus.refugeeaidleipzig.views.signup;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;

import de.awisus.refugeeaidleipzig.LoginData;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.ViewModel;
import de.awisus.refugeeaidleipzig.models.Nutzer;
import de.awisus.refugeeaidleipzig.models.Unterkunft;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.views.SuperFragmentEditUser;

/**
 * Created on 15.01.16.
 * <p/>
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 *
 * @author Jens Awisus
 */
public class FragmentSignup extends SuperFragmentEditUser {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Spinner to choose in which accommodation the users stays in
     */
    private Spinner spUnterkunft;

    private boolean isRefugee;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
     *
     * @param model ViewModel to log in user
     * @return new Login Fragment
     */
    public static FragmentSignup newInstance(ViewModel model, boolean isRefugee) {
        FragmentSignup frag = new FragmentSignup();
        frag.model = model;
        frag.isRefugee = isRefugee;
        frag.layoutID = R.layout.dialogue_signup;
        frag.titelID = R.string.titel_signup;
        return frag;
    }

    @Override
    protected void initElements(View view) {
        super.initElements(view);

        warnungID = R.string.warnung_signup;

        TextView tvUnterkunft;
        tvUnterkunft = (TextView) view.findViewById(R.id.tvUnterkunft);
        spUnterkunft = (Spinner) view.findViewById(R.id.spUnterkunft);

        if(isRefugee) {
            initSpinnerAdapter();
        } else {
            tvUnterkunft.setVisibility(View.GONE);
            spUnterkunft.setVisibility(View.GONE);
        }
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
                    signup();
                }
            });
        }
    }

    @Override
    protected void setButtonListeners(View view) {}

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
        String idUnterkunft = null;
        if(isRefugee) {
            Unterkunft unterkunft = (Unterkunft) spUnterkunft.getSelectedItem();
            idUnterkunft = String.valueOf(unterkunft.getId());
        }

        String name = etName.getText().toString();
        String mail = etMail.getText().toString();
        String passwort = etPasswort.getText().toString();
        String confirmation = etConfirmation.getText().toString();

        new NutzerPost(getActivity(), R.string.meldung_anmelden,
                new LoginData(name, passwort)).execute(
                "name",                     name,
                "mail",                     mail,
                "password",                 passwort,
                "password_confirmation",    confirmation,
                "role",                     isRefugee ? "0" : "1",
                "accommodation_id",         idUnterkunft);
    }

    private class NutzerPost extends NutzerGet {

        NutzerPost(Activity context, int textID, LoginData login) {
            super(context, textID, login);
        }

        @Override
        protected Nutzer doInBackground(String... params) {
            try {
                String antwort = WebFlirt.post("users_remote", params);
                return Nutzer.fromJSON(model.getUnterkuenfte(), new JSONObject(antwort));
            } catch (JSONException e) {
                return null;
            }
        }
    }
}
