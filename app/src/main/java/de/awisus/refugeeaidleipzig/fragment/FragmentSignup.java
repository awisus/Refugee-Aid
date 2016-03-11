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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.MainActivity;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.model.Unterkunft;
import de.awisus.refugeeaidleipzig.net.HTTPPoster;

/**
 * Created on 15.01.16.
 *
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 * @author Jens Awisus
 */
public class FragmentSignup extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String SERVER_URL = "https://refugee-aid.herokuapp.com/";

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Activity as the Context for the accommodation spinner
     */
    private MainActivity context;

    /**
     * Reference to the model to log in the new user (or to be found in the chosen accommodation)
     */
    private Model model;

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

        spUnterkunft = (Spinner) view.findViewById(R.id.spUnterkunft);
        etName = (EditText) view.findViewById(R.id.etName);
        etMail = (EditText) view.findViewById(R.id.etMail);
        etPasswort = (EditText) view.findViewById(R.id.etPassword);
        etConformation = (EditText) view.findViewById(R.id.etConfirmation);

        initSpinnerAdapter();

        builder.setView(view);
        builder.setPositiveButton("Konto erstellen", this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);

        return builder.create();
    }

    /**
     * Private method that creates an Array Adapter the spinner uses to show accommodation names
     */
    private void initSpinnerAdapter() {

        LinkedList<Unterkunft> unterkuenfte;
        unterkuenfte = model.getUnterkuenfte().asList();
        Collections.sort(unterkuenfte);

        ArrayAdapter<Unterkunft> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                unterkuenfte
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spUnterkunft.setAdapter(adapter);
    }

    /**
     * This is called to set the parent Activity as the Context for the spinner
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

    /**
     * This is called when one of the dialogue buttons is clicked.
     * If positive button, the string from the text field and the accommodation chosen in the
     * spinner are passed to the login method of the model. This causes the model to either find an
     * existing resident by name or to create a new user, if the sears is not successful.
     * @param dialog Dialog Interface
     * @param which number indicating the button pressed
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {

            ProgressDialog ladebalken = Utility.zeigeLadebalken(context, getResources().getString(R.string.meldung_anmelden));

            // Get inserted name and selected accommodation from views
            Unterkunft unterkunft = (Unterkunft) spUnterkunft.getSelectedItem();

            String unterkunftID = String.valueOf(unterkunft.getID());
            String name = etName.getText().toString();
            String mail = etMail.getText().toString();
            String password = etPasswort.getText().toString();
            String confirmation = etConformation.getText().toString();

            Nutzer nutzer = signup(
                    "name", name,
                    "mail", mail,
                    "accommodation_id",unterkunftID,
                    "password", password,
                    "password_confirmation", confirmation
            );

            if (nutzer == null) {
                ladebalken.cancel();
                context.checkNavigationMapItem();
                Toast.makeText(context, "Fehler vom Server", Toast.LENGTH_SHORT).show();
            } else {
                ladebalken.cancel();
                model.anmelden(nutzer);
            }
        } else {
            context.checkNavigationMapItem();
        }
    }

    private Nutzer signup(String... parameter) {
        HTTPPoster httpPoster = new HTTPPoster(SERVER_URL);

        for(int i = 0; i < parameter.length; i += 2) {
            httpPoster.addParameter(parameter[i], parameter[i+1]);
        }
        httpPoster.execute("users/remote");

        try {
            JSONObject object = new JSONObject(httpPoster.get());

            int unterkunftID = object.getInt("accommodation_id");
            Unterkunft unterkunft = model.getUnterkunftFromID(unterkunftID);

            return Nutzer.fromJSON(unterkunft, object);
        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("GET: Error", e.toString());
            return null;
        }
    }
}
