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
 * Created on 12.01.16.
 *
 * Class being a blue print for all kinds of windows simply there to show a title and a content
 * string
 * @author Jens Awisus
 */
public class FragmentInfo extends DialogFragment implements DialogInterface.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * String to be set as title
     */
    private String titel;

    /**
     * String to be set as content
     */
    private String inhalt;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Static factory Method initialising with title and content
     * @param titel desired title string
     * @param inhalt desired content
     * @return Fragment making up information window
     */
    public static FragmentInfo newInstance(String titel, String inhalt) {
        FragmentInfo frag = new FragmentInfo();
        frag.titel = titel;
        frag.inhalt = inhalt;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Infaltes the layout, initialises text fields and sets their texts as well as the positive
     * button
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
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

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Listens to clicks on dialogue buttons
     * just closing the window
     * @param dialog DialogInterface
     * @param which number indicating the pressed button
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        getDialog().cancel();
    }
}