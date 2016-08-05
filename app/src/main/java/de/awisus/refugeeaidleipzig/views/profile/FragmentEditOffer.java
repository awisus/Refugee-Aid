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

package de.awisus.refugeeaidleipzig.views.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.Angebot;
import de.awisus.refugeeaidleipzig.util.Utility;

/**
 * Created on 05.08.16.
 *
 * @author Jens Awisus
 */
public class FragmentEditOffer extends DialogFragment {

    private Angebot angebot;

    public static FragmentEditOffer newInstance(Angebot angebot) {
        FragmentEditOffer frag = new FragmentEditOffer();
        frag.angebot = angebot;
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_offer, null);

        builder.setView(view);

        Dialog dialog = builder.create();

        if(angebot == null) {
            forNewOffer(dialog);
        } else {
            forExistingOffer(view, dialog);
        }

        return dialog;
    }

    private void forNewOffer(Dialog dialog) {

        dialog.setTitle("New offer");
    }

    private void forExistingOffer(View view, Dialog dialog) {

        dialog.setTitle("Edit offer");

        ImageView ivOffer = (ImageView) view.findViewById(R.id.ivOffer);
        EditText etTitel = (EditText) view.findViewById(R.id.etTitel);
        EditText etStreet = (EditText) view.findViewById(R.id.etStreet);
        EditText etPostal = (EditText) view.findViewById(R.id.etPostal);
        EditText etDescription = (EditText) view.findViewById(R.id.etDescription);

        Utility.getInstance().setIvImage(ivOffer, angebot.getImageData());

        etTitel.setText(angebot.toString());
        try {
            etStreet.setText(
                    Utility.getInstance().latlngToStreet(angebot.getLatLng(), getActivity())
            );
            etPostal.setText(
                    Utility.getInstance().latlngToCity(angebot.getLatLng(), getActivity())
            );
        } catch (IOException ex) {
            Log.e("Set offer Adress", ex.getMessage());
        }

        etDescription.setText(angebot.getContent());
    }
}
