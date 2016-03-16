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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Bedarf;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.util.Datei;

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
public class FragmentProfil extends Fragment implements Observer {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Label showing the user name
     */
    private TextView tvName;

    /**
     * Label showing the accommodation the user stays in
     */
    private TextView tvEinrichtung;

    private AdapterBedarf adapter;

    private Vector<Bedarf> liste;

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
        frag.liste = frag.nutzer.getBedarf().asVector();
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
        tvName = (TextView) view.findViewById(R.id.tvUnterkunft);
        tvEinrichtung = (TextView) view.findViewById(R.id.tvEinrichtung);

        adapter = new AdapterBedarf(getActivity(), android.R.layout.simple_list_item_1, liste, nutzer);

        ListView listView;
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    /**
     * This shows user information (name, accommodation, needs) in the UI labels
     */
    private void initNutzerInfo() {
        if(nutzer != null) {
            tvName.setText(nutzer.getName());
            tvEinrichtung.setText(nutzer.getUnterkunft().toString());

            nutzer.addObserver(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_profile_menu, menu);
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
        updateBedarf();
    }

    /**
     * Private method called to set the needs label for the current user's needs
     */
    private void updateBedarf() {
        adapter.notifyDataSetChanged();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itNeu:
                FragmentKategorieList.newInstance(nutzer, model.getKategorien().asVector())
                .show(getFragmentManager(), "Kategorien");
                return true;
            case R.id.itBearbeiten:
                // Not implemented here
                return true;
            case R.id.itAbmelden:
                model.abmelden();

                try {
                    Datei.getInstance().loeschen(getActivity(), "login.json");
                } catch (IOException e) {
                    Log.e("Abmelden", "Fehler beim Löschen der Nutzerdaten");
                }
                return true;
            default:
                break;
        }

        return false;
    }
}
