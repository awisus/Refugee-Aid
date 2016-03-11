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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.MainActivity;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;

/**
 * Created on 15.01.16.
 * <p>
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 *
 * @author Jens Awisus
 */
public class FragmentLogin extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {

    public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";

    ////////////////////////////////////////////////////////////////////////////////
    // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Activity as the Context for the accommodation spinner
     */
    private MainActivity context;

    /**
     * Text field for the user name
     */
    private EditText etName;

    /**
     * Spinner to choose in which accommodation the users stays in
     */
    private EditText etPasswort;

    private Button btNeu;

    /**
     * Reference to the model to log in the new user (or to be found in the chosen accommodation)
     */
    private Model model;

    ////////////////////////////////////////////////////////////////////////////////
    // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
     *
     * @param model Model to log in user
     * @return new Login Fragment
     */
    public static FragmentLogin newInstance(Model model) {
        FragmentLogin frag = new FragmentLogin();
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

    /**
     * This is called to set the parent Activity as the Context for the spinner
     *
     * @param activity Activity to be set as context
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (MainActivity) activity;
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

            ProgressDialog ladebalken = Utility.zeigeLadebalken(context, getResources().getString(R.string.meldung_anmelden));

            // Get inserted name and selected accommodation from views
            String name = etName.getText().toString();
            String passwort = etPasswort.getText().toString();

            // remote login with name and pasword
            try {
                Nutzer nutzer = WebFlirt.getInstance().getNutzer(model, name, passwort);
                ladebalken.cancel();
                model.anmelden(nutzer);
            } catch (JSONException | InterruptedException | ExecutionException e) {
                ladebalken.cancel();
                context.checkNavigationMapItem();
                Toast.makeText(context, "Name oder Passwort falsch", Toast.LENGTH_SHORT).show();
            }
        } else {
            context.checkNavigationMapItem();
        }
    }
}
