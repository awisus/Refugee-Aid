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

/**
 * Created on 12.01.16.
 *
 * Class defining the behaviour of the dialogue that gives opportunity to the user to add new needs.
 * This shows a text field to name a need.
 * @author Jens Awisus
 */
public class FragmentBedarfNeu extends DialogFragment implements DialogInterface.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Text field for name of new need
     */
    private EditText etBedarfNeu;

    /**
     * User currently adding new needs
     */
    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Static factory Method initialising with given user
     * @param nutzer user to be added new needs to
     * @return Fragment making up mask to enter new needs
     */
    public static FragmentBedarfNeu newInstance(Nutzer nutzer) {
        FragmentBedarfNeu frag = new FragmentBedarfNeu();
        frag.nutzer = nutzer;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called when this dialogue is created; Android-specific
     * Inflates the layout, initialises text field and sets button texts
     * @param savedInstanceState Bundle of saved instance state
     * @return dialogue created by the AlertDialog.Builder
     */
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

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Listens to clicks on dialogue buttons
     * if positive button, the given string in the text field will be trimmed before being added to
     * the user
     * @param dialog DialogInterface
     * @param which number indicating the pressed button
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            String eingabe = etBedarfNeu.getText().toString();
            if(eingabe != null) {
                eingabe = eingabe.trim();
                if(eingabe != null && !eingabe.equals("")) {
                    nutzer.addBedarf(eingabe);
                }
            }
        }
        getDialog().cancel();
    }
}