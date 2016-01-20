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

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;

/**
 * Created on 12.01.16.
 *
 * This Fragment makes up a profile view for the user.
 * Three Labels show the user's information like name, accommodation and needs.
 * Two FloatingActionButtons are there for adding or removal of needs to be shown on the map of
 * accommodations.
 * A LogOut button performs log out from the model
 * @author Jens Awisus
 */
public class FragmentProfil extends Fragment implements Observer, View.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Fragment for adding new needs by the user
     */
    private FragmentBedarfNeu fragmentBedarfNeu;

    /**
     * Fragment for removing new needs by the user
     */
    private FragmentBedarfEntfernen fragmentBedarfEntfernen;

    /**
     * Label showing the user name
     */
    private TextView tvName;

    /**
     * Label showing the accommodation the user stays in
     */
    private TextView tvEinrichtung;

    /**
     * Label listung the user's needs
     */
    private TextView tvBedarfe;

    /**
     * Log-Out button
     */
    private Button btAbmelden;

    /**
     * Reference to the model for logging the user out
     */
    private Model model;

    /**
     * The user reference
     */
    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method setting the model and the user reference in it
     * @param model Model to be set
     * @return new Profile Fragment
     */
    public static FragmentProfil newInstance(Model model) {
        FragmentProfil frag = new FragmentProfil();
        frag.model = model;
        frag.nutzer = model.getNutzerAktuell();
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Automatically called method inflating the xml-layout.
     * This initialises add UI components, sets the user information to the labls and changes the
     * Activity's title to PROFILE
     * @param inflater who cares
     * @param container who cares
     * @param savedInstanceState who cares
     * @return View inflated with the layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        initUI(view);
        initNutzerInfo();

        getActivity().setTitle(R.string.titel_profil);

        return view;
    }

    /**
     * Private method initialising all UI components from the inflated View
     * @param view inflated View
     */
    private void initUI(View view) {
        fragmentBedarfNeu = FragmentBedarfNeu.newInstance(nutzer);
        fragmentBedarfEntfernen = FragmentBedarfEntfernen.newInstance(nutzer);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEinrichtung = (TextView) view.findViewById(R.id.tvEinrichtung);
        tvBedarfe = (TextView) view.findViewById(R.id.tvBedarfe);

        btAbmelden = (Button) view.findViewById(R.id.btAbmelden);
        btAbmelden.setOnClickListener(this);

        FloatingActionButton btPlus = (FloatingActionButton) view.findViewById(R.id.fab_plus);
        FloatingActionButton btMinus = (FloatingActionButton) view.findViewById(R.id.fab_minus);
        btPlus.setOnClickListener(this);
        btMinus.setOnClickListener(this);
    }

    /**
     * This shows user information (name, accommodation, needs) in the UI labels
     */
    private void initNutzerInfo() {
        if(nutzer != null) {
            tvName.setText(nutzer.getName());
            tvEinrichtung.setText(nutzer.getUnterkunft().toString());

            updateBedarfe();

            nutzer.addObserver(this);
        }
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Observer ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Update method called, if list of the user's needs changes.
     * Causes label of user's needs to chow current information
     * @param observable unused data model notifying about a change
     * @param data unused data object given by the Observable
     */
    @Override
    public void update(Observable observable, Object data) {
        updateBedarfe();
    }

    /**
     * Private method called to set the needs label for the current user's needs
     */
    private void updateBedarfe() {
        if(nutzer != null) {
            if (!nutzer.hatBedarf()) {
                tvBedarfe.setText(R.string.string_keine_bedarfe);
            } else {

                /* Needs are shown as a list like:
                 * Needs:
                 *  - Jacket
                 *  - Toothbrush
                 */
                tvBedarfe.setText(R.string.string_bedarfe);
                tvBedarfe.append("\n\n");
                tvBedarfe.append(nutzer.getBedarfeAlsListeString());
            }
        }
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * This Method is called, if any of the Buttons  is clicked
     * @param view the View pressed
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Click on the FloatingAction Plus button calls fragment for adding a need
        if(id == R.id.fab_plus) {
            fragmentBedarfNeu.show(getChildFragmentManager(), "Neuer Bedarf");
        }

        // Click on the FloatingAction Delete button calls fragment for removing needs
        if(id == R.id.fab_minus) {
            if(nutzer.hatBedarf()) {
                fragmentBedarfEntfernen.show(getChildFragmentManager(), "Bedarf entfernen");
            } else {

                // Tell user, if there is no need to be deleted
                Toast.makeText(getActivity(), R.string.warnung_loeschen, Toast.LENGTH_SHORT).show();
            }
        }

        // LogOut button calles logout of user in the model
        if(id == R.id.btAbmelden) {
            model.abmelden();
        }
    }
}
