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
import android.widget.TextView;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.Unterkunft;
import de.awisus.refugeeaidleipzig.util.LocationUtility;

/**
 * Created on 07.08.16.
 *
 * @author Jens Awisus
 */
public class FragmentAccommodationInfo extends DialogFragment {

    private Unterkunft unterkunft;

    public static FragmentAccommodationInfo newInstance(Unterkunft unterkunft) {
        FragmentAccommodationInfo frag = new FragmentAccommodationInfo();
        frag.unterkunft = unterkunft;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_show_accommodation, null);

        TextView tvAddress   = (TextView) view.findViewById(R.id.tvAddress);
        TextView tvSpace     = (TextView) view.findViewById(R.id.tvSpace);
        TextView tvResidents = (TextView) view.findViewById(R.id.tvResidents);
        TextView tvNeeds     = (TextView) view.findViewById(R.id.tvNeeds);

        setTvAddress(tvAddress);
        tvSpace.setText(String.valueOf(unterkunft.getGroesse()));
        tvResidents.setText(String.valueOf(unterkunft.getBewohner()));
        tvNeeds.setText(getNeeds());

        builder.setView(view);
        Dialog dialog;
        dialog = builder.create();
        dialog.setTitle(unterkunft.toString());
        return dialog;
    }

    private void setTvAddress(TextView tvAddress) {
        try {
            tvAddress.setText(
                    LocationUtility.latlngToAdress(unterkunft.getLatLng(), getActivity())
            );
        } catch (IOException ex) {
            Log.e("Accommodation Adress", ex.getMessage());
        }
    }

    private String getNeeds() {
        return unterkunft.hatBedarf()
                ? unterkunft.getBedarfeAlsString()
                : getResources().getString(R.string.label_keine_eintraege);
    }
}
