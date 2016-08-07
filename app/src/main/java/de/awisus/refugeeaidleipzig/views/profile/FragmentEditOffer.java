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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.Angebot;
import de.awisus.refugeeaidleipzig.models.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.BackgroundTask;
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

    private Bitmap imageBitmap;

    private Nutzer nutzer;
    private Angebot angebot;

    public static FragmentEditOffer newInstance(Nutzer nutzer, Angebot angebot) {
        FragmentEditOffer frag = new FragmentEditOffer();
        frag.nutzer = nutzer;
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

        if(angebot == null) {
            forNewOffer(dialog);
            setListeners(view, true);
        } else {
            forExistingOffer(dialog);
            setListeners(view, false);
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

    private void forNewOffer(Dialog dialog) {
        dialog.setTitle("New offer");
    }

    private void forExistingOffer(Dialog dialog) {
        dialog.setTitle("Edit offer");
        LatLng latLng = angebot.getLatLng();

        Utility.getInstance().setIvImage(ivOffer, angebot.getImageData());

        etTitel.setText(angebot.toString());
        try {
            String address = Utility.getInstance().latlngToStreet(latLng, getActivity());
            String city    = Utility.getInstance().latlngToCity(latLng, getActivity());
            etStreet.setText(address);
            etPostal.setText(city);
        } catch (IOException ex) {
            Log.e("Set offer Address", ex.getMessage());
        }

        etDescription.setText(angebot.getContent());
    }

    private void setListeners(View view, final boolean neu) {
        ivOffer.setOnClickListener(this);

        FloatingActionButton fabSend;
        fabSend = (FloatingActionButton) view.findViewById(R.id.fab_send);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etStreet.getText() +", " +etPostal.getText();
                try {
                    LatLng coordinates = Utility.getInstance().getLocationFromAddress(address, getContext());

                    if(neu) {
                        new OfferPost(getActivity(), R.string.meldung_hinzufuegen).execute(
                                "title",     "" + etTitel.getText(),
                                "text",      "" + etDescription.getText(),
                                "image",     "" + Utility.getInstance().imageToString(imageBitmap),
                                "latitude",  "" + coordinates.latitude,
                                "longitude", "" + coordinates.longitude,
                                "user_id",   "" + nutzer.getId()
                        );
                    } else {
                        String imageData = imageBitmap == null ?
                                angebot.getImageData() :
                                Utility.getInstance().imageToString(imageBitmap);
                        new OfferPatch(getActivity(), R.string.meldung_aktualisieren).execute(
                                "id",        "" + angebot.getId(),
                                "title",     "" + etTitel.getText(),
                                "text",      "" + etDescription.getText(),
                                "image",     "" + imageData,
                                "latitude",  "" + coordinates.latitude,
                                "longitude", "" + coordinates.longitude
                        );
                    }
                } catch (IOException | NullPointerException ex) {
                    Toast.makeText(getActivity(), R.string.warnung_address, Toast.LENGTH_SHORT ).show();
                }
            }
        });
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
                imageBitmap = Utility.getInstance().uriToBitmap(getActivity(), uri, ivOffer.getWidth());
                ivOffer.setImageBitmap(imageBitmap);
            } catch (IOException ex) {
                ivOffer.setImageResource(R.drawable.add_image);
            }
        }
    }

    private class OfferPost extends BackgroundTask<String, Integer, Angebot> {

        private OfferPost(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected Angebot doInBackground(String... params) {
            try {
                String antwort = WebFlirt.getInstance().post("offers_remote", params);
                return Angebot.fromJSON(new JSONObject(antwort));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void doPostExecute(Angebot result) {
            if(result == null) {
                Toast.makeText(context, R.string.warnung_bedarf_vorhanden, Toast.LENGTH_SHORT).show();
            } else {
                nutzer.addData(result);
                dismiss();
            }
        }
    }

    private class OfferPatch extends BackgroundTask<String, String, Angebot> {

        public OfferPatch(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected Angebot doInBackground(String... params) {
            String antwort = WebFlirt.getInstance().patch("offers_remote", params);
            return antwort.equals("OK") ? angebot : null;
        }

        @Override
        protected void doPostExecute(Angebot result) {
            if(result == null) {
                Toast.makeText(context, R.string.warnung_signup, Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String address = etStreet.getText() + ", " + etPostal.getText();

                    angebot.setTitle(etTitel.getText().toString());
                    angebot.setContent(etDescription.getText().toString());
                    angebot.setImageData(Utility.getInstance().imageToString(imageBitmap));
                    angebot.setLatLng(Utility.getInstance().getLocationFromAddress(address, getContext()));

                    nutzer.report();

                    dismiss();
                } catch (IOException | NullPointerException ex) {
                    Toast.makeText(getActivity(), R.string.warnung_address, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
