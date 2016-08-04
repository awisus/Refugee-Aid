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

package de.awisus.refugeeaidleipzig.views.map;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.Angebot;
import de.awisus.refugeeaidleipzig.util.Utility;

/**
 * Created on 12.01.16.
 *
 * Class being a blue print for all kinds of windows simply there to show a title and a content
 * string
 * @author Jens Awisus
 */
public class FragmentOfferInfo extends DialogFragment {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private Angebot angebot;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public static FragmentOfferInfo newInstance(Angebot angebot) {
        FragmentOfferInfo frag = new FragmentOfferInfo();
        frag.angebot = angebot;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_show_offer, null);

        ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);

        tvContent.setText(angebot.getContent());
        setIvImage(ivImage, angebot.getImageData());
        setTvAddress(tvAddress);

        builder.setView(view);

        Dialog dialog;
        dialog = builder.create();

        dialog.setTitle(angebot.toString());

        return dialog;
    }

    private void setIvImage(ImageView ivImage, String imageData) throws IllegalArgumentException {
        try {
            ivImage.setImageBitmap(
                    Utility.getInstance().stringToImage(imageData)
            );
        } catch (IllegalArgumentException ex) {
            Log.e("Set offer image", ex.getMessage());
        }
    }

    private void setTvAddress(TextView tvAddress) {
        try {
            tvAddress.setText(
                    Utility.getInstance().latlngToAdress(angebot.getLatLng(), getActivity())
            );
        } catch (IOException ex) {
            Log.e("Set offer Adress", ex.getMessage());
        }
    }
}
