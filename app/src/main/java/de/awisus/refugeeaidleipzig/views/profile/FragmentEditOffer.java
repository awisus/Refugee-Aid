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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

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

    private ImageView ivOffer;
    private EditText etTitel;
    private EditText etStreet;
    private EditText etPostal;
    private EditText etDescription;

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

        ivOffer = (ImageView) view.findViewById(R.id.ivOffer);
        etTitel = (EditText) view.findViewById(R.id.etTitel);
        etStreet = (EditText) view.findViewById(R.id.etStreet);
        etPostal = (EditText) view.findViewById(R.id.etPostal);
        etDescription = (EditText) view.findViewById(R.id.etDescription);

        if(angebot == null) {
            forNewOffer(dialog);
        } else {
            forExistingOffer(view, dialog);
        }

        FloatingActionButton fabSend = (FloatingActionButton) view.findViewById(R.id.fab_send);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etStreet.getText() +", " +etPostal.getText();
                try {
                    LatLng coordinates = Utility.getInstance().getLocationFromAddress(address, getContext());

                    Log.w("Latitude", "" +coordinates.latitude);
                    Log.w("Longitude", "" +coordinates.longitude);
                } catch (IOException ex) {
                    Log.e("Coords from address", "Wrong address format");
                } catch (NullPointerException ex) {
                    Log.e("Coords from address", "no address received");
                }
            }
        });

        return dialog;
    }

    private void forNewOffer(Dialog dialog) {

        dialog.setTitle("New offer");
    }

    private void forExistingOffer(View view, Dialog dialog) {

        dialog.setTitle("Edit offer");

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
            Log.e("Set offer Address", ex.getMessage());
        }

        etDescription.setText(angebot.getContent());
    }
}
