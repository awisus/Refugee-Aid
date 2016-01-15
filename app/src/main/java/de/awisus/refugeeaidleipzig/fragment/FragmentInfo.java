package de.awisus.refugeeaidleipzig.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import de.awisus.refugeeaidleipzig.R;

/**
 * Created by Jens Awisus on 12.01.16.
 */
public class FragmentInfo extends DialogFragment implements DialogInterface.OnClickListener {

    private String titel;
    private String inhalt;

    public static FragmentInfo newInstance(String titel, String inhalt) {
        FragmentInfo frag = new FragmentInfo();
        frag.titel = titel;
        frag.inhalt = inhalt;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_info, null);

        TextView tvTitelInfo = (TextView) view.findViewById(R.id.tvTitelInfo);
        TextView tvInfo = (TextView) view.findViewById(R.id.tvInfo);

        tvTitelInfo.setText(titel);
        tvInfo.setText(inhalt);

        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_ok, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        getDialog().cancel();
    }
}