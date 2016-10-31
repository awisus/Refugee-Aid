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

package de.awisus.refugeeaid.views.signup;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import de.awisus.refugeeaid.R;
import de.awisus.refugeeaid.ViewModel;

/**
 * Created on 05.08.16.
 *
 * @author Jens Awisus
 */
public class FragmentSelectRole extends DialogFragment {

    private ViewModel model;

    public static FragmentSelectRole newInstance(ViewModel model) {
        FragmentSelectRole frag = new FragmentSelectRole();
        frag.model = model;
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogue_select_user_role, null);

        setButtonListeners(view);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.setTitle(R.string.titel_select_role);

        return dialog;
    }

    protected void setButtonListeners(View view) {
        Button btRef = (Button) view.findViewById(R.id.btRefugee);
        Button btSup = (Button) view.findViewById(R.id.btSupporter);

        btRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performClick(true);
            }
        });
        btSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performClick(false);
            }
        });
    }

    private void performClick(boolean isRefugee) {
        dismiss();

        FragmentSignup fragmentSignup;
        fragmentSignup = FragmentSignup.newInstance(model, isRefugee);
        fragmentSignup.show(getActivity().getSupportFragmentManager(), "Neues Konto");
    }
}
