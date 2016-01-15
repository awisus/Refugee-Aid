package de.awisus.refugeeaidleipzig.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import de.awisus.refugeeaidleipzig.MainActivity;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created by Jens Awisus on 15.01.16.
 */
public class FragmentLogin extends DialogFragment implements DialogInterface.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private MainActivity context;
    private EditText etName;
    private Spinner spUnterkunft;
    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public static FragmentLogin newInstance(Model model) {
        FragmentLogin frag = new FragmentLogin();
        frag.model = model;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, null);

        etName = (EditText) view.findViewById(R.id.etName);
        spUnterkunft = (Spinner) view.findViewById(R.id.spUnterkunft);

        initSpinnerAdapter();

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_login, this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);

        return builder.create();
    }

    private void initSpinnerAdapter() {
        ArrayAdapter<Unterkunft> adapter = new ArrayAdapter<Unterkunft>(
                context,
                android.R.layout.simple_spinner_item,
                model.getUnterkuenfte()
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spUnterkunft.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = (MainActivity)activity;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listeners ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {

            // Get inserted name and selected accommodation from views
            String eingabe = etName.getText().toString();
            Unterkunft unterkunft = (Unterkunft) spUnterkunft.getSelectedItem();

            if(eingabe != null && unterkunft != null) {
                if(eingabe.trim() != null && !eingabe.trim().equals("")) {

                    // login with chosen name and accommodation
                    model.anmelden(eingabe, unterkunft);
                }
            }
        }
        getDialog().cancel();
    }
}