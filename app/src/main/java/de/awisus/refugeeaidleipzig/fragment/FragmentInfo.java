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
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import de.awisus.refugeeaidleipzig.R;

/**
 * Created on 12.01.16.
 *
 * Class being a blue print for all kinds of windows simply there to show a title and a content
 * string
 * @author Jens Awisus
 */
public class FragmentInfo extends DialogFragment {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * String to be set as content
     */
    private String inhalt;

    private String titel;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Static factory Method initialising with title and content
     * @param titel desired title string
     * @param inhalt desired content
     * @return Fragment making up information window
     */
    public static FragmentInfo newInstance(String titel, String inhalt) {
        FragmentInfo frag = new FragmentInfo();
        frag.inhalt = inhalt;
        frag.titel = titel;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Inflates the layout, initialises text fields and sets their texts as well as the positive
     * button
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_info, null);

        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);

        tvInfo.setText(inhalt);

        builder.setView(view);

        Dialog dialog;
        dialog = builder.create();
        dialog.setTitle(titel);

        return dialog;
    }
}
