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
import android.widget.EditText;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;

/**
 * Created on 12.01.16.
 *
 * Class defining the behaviour of the dialogue that gives opportunity to the user to add new needs.
 * This shows a text field to name a need.
 * @author Jens Awisus
 */
public class FragmentBedarfNeu extends DialogFragment implements DialogInterface.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Text field for name of new need
     */
    private EditText etBedarfNeu;

    /**
     * User currently adding new needs
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
    public static FragmentBedarfNeu newInstance(Nutzer nutzer) {
        FragmentBedarfNeu frag = new FragmentBedarfNeu();
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_bedarf_neu, null);

        etBedarfNeu = (EditText) view.findViewById(R.id.etBedarfNeu);

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_hinzufuegen, this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);

        Dialog dialog = builder.create();
        dialog.setTitle(R.string.titel_bedarf_neu);

        return dialog;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Listens to clicks on dialogue buttons
     * if positive button, the given string in the text field will be trimmed before being added to
     * the user
     * @param dialog DialogInterface
     * @param which number indicating the pressed button
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            try {
                int nutzerID = nutzer.getId();
                int bedarfID = Integer.parseInt(etBedarfNeu.getText().toString());

                new BedarfPost(getActivity(), R.string.meldung_hinzufuegen).execute(
                        "user_id",      String.valueOf(nutzerID),
                        "category_id",  String.valueOf(bedarfID));
            } catch (Exception e) {
                return;
            }
        }
    }

    private class BedarfPost extends BackgroundTask<String, Integer, Integer> {

        public BedarfPost(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                return WebFlirt.getInstance().postBedarf(params[0], params[1], params[2], params[3]);
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void doPostExecute(Integer result) {

            if(result == null) {
//                Toast.makeText(getActivity(), "Konnte nicht hinzugefügt werden", Toast.LENGTH_SHORT).show();
            } else {
                //
            }

            dismiss();
        }
    }
}
