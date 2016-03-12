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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaidleipzig.MainActivity;
import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.DataMap;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Unterkunft;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.Utility;

/**
 * Created on 11.01.16.
 *
 * Fragment that is responsible for showing the model's information about accommodations and their
 * needs (collection of their resident's needs).
 * Main point is to show a Google Map with markers on the accommodation's locations and additional
 * information about sizes, number of residents and a list of needs for each accommodation
 * @author Jens Awisus
 */
public class FragmentKarte extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private MainActivity context;

    private GoogleMap karte;

    private FloatingActionButton fabUpdate;

    /**
     * Model to access information about the accommodations and their respective marker info
     */
    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Static factory Method initialising with reference to the model
     * @param model
     * @return
     */
    public static FragmentKarte newInstance(MainActivity context, Model model) {
        FragmentKarte frag = new FragmentKarte();
        frag.model = model;
        frag.context = context;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Automatically called method inflating the xml-layout
     * Sets actovoty title to Map
     * @param inflater who cares
     * @param container who cares
     * @param savedInstanceState who cares
     * @return View inflated with the layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_karte, container, false);

        fabUpdate = (FloatingActionButton) view.findViewById(R.id.fabUpdate);
        fabUpdate.setOnClickListener(this);

        getActivity().setTitle(R.string.nav_karte);
        return view;
    }

    /**
     * Automatically called when the parent activity has finished creating
     * Responsible for getting the map onto the fragment
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment;
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Google Map //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Automatically called when Google Map is ready
     * In this method, markers are added to the model corresponding to the accommodations.
     * Their will also be zoom on the user's place as well as an info text for each marker window
     * @param karte Google Map that is ready
     */
    @Override
    public void onMapReady(GoogleMap karte) {
        this.karte = karte;
        this.setMarkers();

        // Zoom on users accommodation, if logged in
        if(model.angemeldet()) {
            karte.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            model.getNutzerAktuell().getUnterkunft().getLatLng(), 11f));
        } else {
            // Zoom to arbitrary accommodation
            karte.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.5, 10.5), 5.8f));
        }

        // tap on marker info box shows accommodation detail
        karte.setOnInfoWindowClickListener(this);
    }

    private void setMarkers() {

        // get accommodations loaded into the model
        final DataMap<Unterkunft> unterkuenfte = model.getUnterkuenfte();

        // Bring marker for each accommodation to the map
        for (int i = 0; i < unterkuenfte.size(); i++) {
            Marker marke = karte.addMarker(model.getMarkerOption(i));

            // Store this marker in the model
            model.addMarke(marke, i);
        }
    }

    /**
     * This method is called, if a click on an info windows of a map marker occurs.
     * This is going to pop up a detailed view of the accommodation corresponding to the marker.
     * That window will contain information about space, number of residents and needs of all
     * residents.
     * @param marke Marker if which the window is clicked
     */
    @Override
    public void onInfoWindowClick(Marker marke) {

        // Get the accommodation for the marker being tabbed
        Unterkunft unterkunft = model.getUnterkunft(marke);

        /*
         * Build up the details string to be shown in a separate window.
         * Example format:
         *
         *   Spaces: 30
         *   Residents: 28
         *
         *   Needs: Football, Jacket, Toothbrush
         */

        String detail;
        detail  = getResources().getString(R.string.string_plaetze) + " ";
        detail += unterkunft.getGroesse();
        detail += "\n";
        detail += getResources().getString(R.string.string_bewohner) + " ";
        detail += unterkunft.getBewohner();

        detail += "\n\n";

        detail += unterkunft.hatBedarf()
                ? getResources().getString(R.string.string_bedarfe) + " "
                + unterkunft.getBedarfeAlsString()
                : getResources().getString(R.string.string_keine_bedarfe);

        // Call a new info fragment with name and details about the accommodation
        FragmentInfo.newInstance(
                unterkunft.toString(),
                detail
        ).show(getActivity().getSupportFragmentManager(), "Info");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabUpdate) {
            new Updater().execute();
        }
    }

    private class Updater extends AsyncTask<String, Integer, DataMap<Unterkunft>> {

        private ProgressDialog ladebalken;

        @Override
        protected void onPreExecute() {
            ladebalken = Utility.getInstance().zeigeLadebalken(context, getResources().getString(R.string.meldung_aktualisieren));
        }

        @Override
        protected void onPostExecute(DataMap<Unterkunft> result) {

            if(result == null) {
                Toast.makeText(context, "Download gescheitert", Toast.LENGTH_SHORT).show();
            } else {
                model.setUnterkuenfte(result);
                karte.clear();
                setMarkers();
            }

            ladebalken.dismiss();
        }

        @Override
        protected DataMap<Unterkunft> doInBackground(String... params) {
            try {
                return WebFlirt.getInstance().getUnterkuenfte();
            } catch (IOException | JSONException | InterruptedException | ExecutionException e){
                return null;
            }
        }
    }
}
