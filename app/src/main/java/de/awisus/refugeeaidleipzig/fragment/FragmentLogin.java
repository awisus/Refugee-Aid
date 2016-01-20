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
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created on 15.01.16.
 *
 * A login fragment with a text field for the user name and a spinner with all accommodations to
 * choose.
 * @author Jens Awisus
 */
public class FragmentLogin extends DialogFragment implements DialogInterface.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Activity as the Context for the accommodation spinner
     */
    private Activity context;

    /**
     * Text field for the user name
     */
    private EditText etName;

    /**
     * Spinner to choose in which accommodation the users stays in
     */
    private Spinner spUnterkunft;

    /**
     * Reference to the model to log in the new user (or to be found in the chosen accommodation)
     */
    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method giving the model's reference
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
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, null);

        etName = (EditText) view.findViewById(R.id.etName);
        spUnterkunft = (Spinner) view.findViewById(R.id.spUnterkunft);

        initSpinnerAdapter();

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_login, this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);

        return builder.create();
    }

    /**
     * Private method that creates an Array Adapter the spinner uses to show accommodation names
     */
    private void initSpinnerAdapter() {
        ArrayAdapter<Unterkunft> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                model.getUnterkuenfte()
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
        context = activity;
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

            // Get inserted name and selected accommodation from views
            String eingabe = etName.getText().toString();
            Unterkunft unterkunft = (Unterkunft) spUnterkunft.getSelectedItem();

            if(eingabe != null && unterkunft != null) {
                eingabe = eingabe.trim();
                if(eingabe != null && !eingabe.equals("")) {

                    // login with chosen name and accommodation
                    model.anmelden(eingabe, unterkunft);
                }
            }
        }
        getDialog().cancel();
    }
}
