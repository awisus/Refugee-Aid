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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;

/**
 * Created on 15.01.16.
 * <p>
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 *
 * @author Jens Awisus
 */
public class FragmentAnmelden extends FragmentLogin implements DialogInterface.OnClickListener, View.OnClickListener {

    ////////////////////////////////////////////////////////////////////////////////
    // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Text field for the user name
     */
    private EditText etName;

    /**
     * Spinner to choose in which accommodation the users stays in
     */
    private EditText etPasswort;

    private Button btNeu;

    ////////////////////////////////////////////////////////////////////////////////
    // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
     *
     * @param model Model to log in user
     * @return new Login Fragment
     */
    public static FragmentAnmelden newInstance(Model model) {
        FragmentAnmelden frag = new FragmentAnmelden();
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
     *
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, null);

        etName = (EditText) view.findViewById(R.id.etName);
        etPasswort = (EditText) view.findViewById(R.id.etPassword);
        btNeu = (Button) view.findViewById(R.id.btNeu);

        btNeu.setOnClickListener(this);

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_login, this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);

        return builder.create();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Listeners ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btNeu) {
            FragmentSignup fragmentSignup;
            fragmentSignup = FragmentSignup.newInstance(model);
            fragmentSignup.show(context.getSupportFragmentManager(), "Neues Konto");

            dismiss();
        }
    }

    /**
     * This is called when one of the dialogue buttons is clicked.
     * If positive button, the string from the text field and the accommodation chosen in the
     * spinner are passed to the login method of the model. This causes the model to either find an
     * existing resident by name or to create a new user, if the sears is not successful.
     *
     * @param dialog Dialog Interface
     * @param which  number indicating the button pressed
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            login();
        } else {
            context.checkNavigationMapItem();
        }
    }

    private void login() {
        // Get inserted name and selected accommodation from views
        String name = etName.getText().toString();
        String passwort = etPasswort.getText().toString();

        new NutzerGet().execute(name, passwort);
    }
}
