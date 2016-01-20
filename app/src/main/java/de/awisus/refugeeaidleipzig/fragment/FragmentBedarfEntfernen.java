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
import android.widget.Spinner;

import de.awisus.refugeeaidleipzig.MainActivity;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Nutzer;

/**
 * Created on 16.01.16.
 *
 * This class describes a fragment that makes it possible to the user to delete needs added
 * beforehand.
 * This mainly shows a spinner to choose a need to be deleted.
 * @author Jens Awisus
 */
public class FragmentBedarfEntfernen extends DialogFragment implements DialogInterface.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * An Activity for a spinner as context
     */
    private MainActivity context;

    /**
     * A Spinners to choose a particular need of the user
     */
    private Spinner spBedarf;

    /**
     * User currently removing new needs
     */
    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Static factory Method initialising with given user
     * @param nutzer user to be added new needs to
     * @return Fragment making up mask to enter new needs
     */
    public static FragmentBedarfEntfernen newInstance(Nutzer nutzer) {
        FragmentBedarfEntfernen frag = new FragmentBedarfEntfernen();
        frag.nutzer = nutzer;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Inflates the layout, initialises text field and sets button texts
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_bedarf_entfernen, null);

        spBedarf = (Spinner) view.findViewById(R.id.spBedarf);
        initSpinnerAdapter();

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_loeschen, this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);
        return builder.create();
    }

    /**
     * This private method applies an adapter to the spinner to list all the user's needs
     */
    private void initSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                nutzer.getBedarfe()
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spBedarf.setAdapter(adapter);
    }

    /**
     * This is called, when a parent Activity is attached. We want to use this Activity as context
     * for our spinner.
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (MainActivity)activity;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Listens to clicks on dialogue buttons
     * if positive button, we take the selected need in the spinner and call the user to remove it
     * @param dialog DialogInterface
     * @param which number indicating the pressed button
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            String bedarf = (String) spBedarf.getSelectedItem();

            if(bedarf != null) {
                nutzer.loescheBedarf(bedarf);
            }
        }
        getDialog().cancel();
    }
}
