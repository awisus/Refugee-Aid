package de.awisus.refugeeaidleipzig.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Nutzer;

public class FragmentBedarfNeu extends DialogFragment implements DialogInterface.OnClickListener {

    private EditText etBedarfNeu;
    private Nutzer nutzer;

    public static FragmentBedarfNeu newInstance(Nutzer nutzer) {
        FragmentBedarfNeu frag = new FragmentBedarfNeu();
        frag.nutzer = nutzer;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_bedarf_neu, null);

        etBedarfNeu = (EditText) view.findViewById(R.id.etBedarfNeu);

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_hinzufuegen, this);
        builder.setNegativeButton(R.string.dialog_abbrechen, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            String eingabe = etBedarfNeu.getText().toString();
            if(eingabe != null) {
                if(eingabe.trim() != null && !eingabe.trim().equals("")) {
                    nutzer.addBedarf(eingabe);
                }
            }
        }
        getDialog().cancel();
    }
}