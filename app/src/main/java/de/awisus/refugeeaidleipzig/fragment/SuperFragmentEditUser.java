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
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.util.MailValidator;
import de.awisus.refugeeaidleipzig.util.TextValidator;

/**
 * Created on 12.03.16.
 *
 * @author Jens Awisus
 */
public abstract class SuperFragmentEditUser extends SuperFragmentGetUser {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    protected EditText etName;
    protected EditText etMail;
    protected EditText etPasswort;
    protected EditText etConformation;

    protected int layoutID;
    protected int titelID;

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Inflates the layout, initialises text field and spinner, initialises the spinner adapter to
     * show up accommodations and sets the dialogue buttons
     *
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(layoutID, null);

        initElements(view);
        setButtonListeners(view);
        setTextFieldListeners();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.setTitle(titelID);

        return dialog;
    }

    protected void initElements(View view) {
        etName = (EditText) view.findViewById(R.id.etName);
        etMail = (EditText) view.findViewById(R.id.etMail);
        etPasswort = (EditText) view.findViewById(R.id.etPassword);
        etConformation = (EditText) view.findViewById(R.id.etConfirmation);
    }

    protected abstract void setButtonListeners(View view);

    protected void setTextFieldListeners() {

        etName.addTextChangedListener(new TextValidator(etName) {
            @Override
            public void afterTextChanged(Editable s) {
                String name = etName.getText().toString();

                if (name.length() > 47) {
                    etName.setError(findString(R.string.warnung_name));
                }
            }
        });
        etMail.addTextChangedListener(new TextValidator(etMail) {
            @Override
            public void afterTextChanged(Editable s) {
                String mail = etMail.getText().toString();

                if (!MailValidator.getInstance().isValid(mail)) {
                    etMail.setError(findString(R.string.warnung_mail));
                }
            }
        });
        etPasswort.addTextChangedListener(new TextValidator(etPasswort) {
            @Override
            public void afterTextChanged(Editable s) {
                String passwort = etPasswort.getText().toString();
                String confirmation = etConformation.getText().toString();

                if (passwort.length() < 6) {
                    etPasswort.setError(findString(R.string.warnung_passwort));
                    return;
                }
                if(!confirmation.isEmpty()) {
                    if(confirmation.equals(passwort)) {
                        etConformation.setError(null);
                    } else {
                        etConformation.setError(findString(R.string.warnung_confirmation));
                    }
                }
            }
        });
        etConformation.addTextChangedListener(new TextValidator(etConformation) {
            @Override
            public void afterTextChanged(Editable s) {
                String password = etPasswort.getText().toString();
                String confirmation = etConformation.getText().toString();

                if (!password.equals(confirmation)) {
                    etConformation.setError(findString(R.string.warnung_confirmation));
                }
            }
        });
    }

    protected String findString(int index) {
        return getResources().getString(index);
    }
}
