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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.io.InputStream;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.Angebot;
import de.awisus.refugeeaidleipzig.util.Utility;

/**
 * Created on 05.08.16.
 *
 * @author Jens Awisus
 */
public class FragmentEditOffer extends DialogFragment implements View.OnClickListener {

    private static final int WAEHLE_BILD = 100;

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

        initView(view);
        setListeners(view);

        if(angebot == null) {
            forNewOffer(dialog);
        } else {
            forExistingOffer(dialog);
        }

        return dialog;
    }

    private void initView(View view) {
        ivOffer = (ImageView) view.findViewById(R.id.ivOffer);
        etTitel = (EditText) view.findViewById(R.id.etTitel);
        etStreet = (EditText) view.findViewById(R.id.etStreet);
        etPostal = (EditText) view.findViewById(R.id.etPostal);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
    }

    private void setListeners(View view) {
        ivOffer.setOnClickListener(this);

        FloatingActionButton fabSend;
        fabSend = (FloatingActionButton) view.findViewById(R.id.fab_send);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etStreet.getText() +", " +etPostal.getText();
                try {
                    LatLng coordinates = Utility.getInstance().getLocationFromAddress(address, getContext());

                    Log.w("Coords", "" +coordinates.latitude +", " +coordinates.longitude);
                } catch (IOException ex) {
                    Log.e("Coords from address", "Wrong address format");
                } catch (NullPointerException ex) {
                    Log.e("Coords from address", "no address received");
                }
            }
        });
    }

    private void forNewOffer(Dialog dialog) {
        dialog.setTitle("New offer");
    }

    private void forExistingOffer(Dialog dialog) {
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

    @Override
    public void onClick(View view) {
        Intent intentGalerie;
        intentGalerie = new Intent(Intent.ACTION_PICK);
        intentGalerie.setType("image/*");

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, intentGalerie);
        chooser.putExtra(Intent.EXTRA_TITLE, R.string.titel_select_image);

        Intent[] intentArray =  {new Intent(MediaStore.ACTION_IMAGE_CAPTURE)};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooser, WAEHLE_BILD);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == WAEHLE_BILD) {
            setBild(data.getData());
        }
    }

    private void setBild(Uri uri) {
        if(uri == null) {
            ivOffer.setImageResource(R.drawable.add_image);
        } else {
            try {
                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                assert is != null;
                is.close();

                ivOffer.setImageBitmap(bitmap);
            } catch (IOException ex) {
                ivOffer.setImageResource(R.drawable.add_image);
                Log.e("Set offer image", ex.getMessage());
            }
        }
    }
}
