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

package de.awisus.refugeeaid.views.map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import de.awisus.refugeeaid.Loader;
import de.awisus.refugeeaid.MainActivity;
import de.awisus.refugeeaid.R;
import de.awisus.refugeeaid.ViewModel;
import de.awisus.refugeeaid.models.Angebot;
import de.awisus.refugeeaid.models.ILocationDataObject;
import de.awisus.refugeeaid.models.Nutzer;
import de.awisus.refugeeaid.models.Unterkunft;
import de.awisus.refugeeaid.util.BackgroundTask;
import de.awisus.refugeeaid.util.LocationUtility;

import static de.awisus.refugeeaid.R.id.map;

/**
 * Created on 11.01.16.
 *
 * Fragment that is responsible for showing the model's information about accommodations and their
 * needs (collection of their resident's needs).
 * Main point is to show a Google Map with markers on the accommodation's locations and additional
 * information about sizes, number of residents and a list of needs for each accommodation
 * @author Jens Awisus
 */
public class FragmentKarte extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private MainActivity context;

    private GoogleMap karte;

    private FloatingActionButton fabUpdate;

    private Animation fabHide, fabShow;

    private boolean hidden = false;

    /**
     * ViewModel to access information about the accommodations and their respective marker info
     */
    private ViewModel model;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Static factory Method initialising with reference to the model
     * @param model
     * @return
     */
    public static FragmentKarte newInstance(MainActivity context, ViewModel model) {
        FragmentKarte frag = new FragmentKarte();
        frag.model = model;
        frag.context = context;
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

        fabHide = AnimationUtils.loadAnimation(context, R.anim.bt_hide);
        fabShow = AnimationUtils.loadAnimation(context, R.anim.bt_show);

        getActivity().setTitle(R.string.app_name);
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
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        if(LocationUtility.isGPSOn(getActivity())) {
            if (!LocationUtility.haveGPSPermission(getActivity())) {
                LocationUtility.requestGPSPermission(getActivity());
            }
        }
    }

//    @TargetApi(23)
//    private void zoomToUser() {
//        karte.setMyLocationEnabled(true);
//        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//
//        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
//        if (location != null) {
//            LatLng coords = new LatLng(location.getLatitude(), location.getLongitude());
//            karte.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 11f));
//        }
//    }

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

        // tap on marker info box shows accommodation detail
        karte.setOnInfoWindowClickListener(this);
        karte.setOnMarkerClickListener(this);
        karte.setOnMapClickListener(this);
        setMarkers();

        // Show Germany as default
        defaultZoom();

        // Zoom to user location, if possible
        if(LocationUtility.isGPSOn(getActivity())) {
            setupLocationListener();

            // if not, then visit a refugee's accommocation
            if(LocationUtility.haveGPSPermission(getActivity())) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng coords = new LatLng(location.getLatitude(), location.getLongitude());
                karte.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 13f));
            } else {
                // Zoom on users accommodation, if logged in
                zoomToAccommocation();
            }
        } else {
            zoomToAccommocation();
        }
    }

    private void zoomToAccommocation() {
        // Zoom on users accommodation, if logged in
        if (model.angemeldet()) {
            Nutzer nutzer = model.getNutzer();
            if (nutzer.isRefugee()) {
                karte.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        nutzer.getUnterkunft().getLatLng(), 13f)
                );
            }
        }
    }

    private void setupLocationListener() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                LatLng coords = new LatLng(location.getLatitude(), location.getLongitude());
                karte.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 13f));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // ask user for permission to read gps location on start
        if(LocationUtility.haveGPSPermission(getActivity())) {
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    private void defaultZoom() {
        // Zoom to Germany
        karte.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(51.5, 10.5), 5.8f)
        );
    }

    private void setMarkers() {
        for(MarkerOptions markerOption : model.getMarkerOptionen()) {
            Marker marke = karte.addMarker(markerOption);
            model.addMarke(marke, markerOption);
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
        ILocationDataObject data = model.getData(marke);

        if(data instanceof Unterkunft) {
            showAccommodationInfo((Unterkunft) data);
            return;
        }
        if(data instanceof Angebot) {
            FragmentOfferInfo.newInstance((Angebot) data)
                    .show(getActivity().getSupportFragmentManager(), data.toString());
        }
    }

    private void showAccommodationInfo(Unterkunft unterkunft) {
        FragmentAccommodationInfo.newInstance(unterkunft)
                .show(getActivity().getSupportFragmentManager(), unterkunft.toString());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(!hidden) {
            fabUpdate.startAnimation(fabHide);
            fabUpdate.setClickable(false);
            hidden = true;
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(hidden) {
            fabUpdate.startAnimation(fabShow);
            fabUpdate.setClickable(true);
            hidden = false;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fabUpdate) {
            new Updater(context, R.string.meldung_aktualisieren).execute();
        }
    }

    private class Updater extends BackgroundTask<String, Integer, ViewModel> {

        public Updater(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected void doPostExecute(ViewModel result) {

            if(result == null) {
                Toast.makeText(getActivity(), R.string.warnung_laden, Toast.LENGTH_SHORT).show();
            } else {
                karte.clear();
                setMarkers();
            }
        }

        @Override
        protected ViewModel doInBackground(String... params) {
            try {
                // initialise the model

                model.clearLocationData();
                model.setKategorien(Loader.getKategorien());
                model.setAngebote(Loader.getAngebote());
                model.setUnterkuenfte(Loader.getUnterkuenfte());

                return model;
            } catch (IOException | JSONException | InterruptedException | ExecutionException e) {
                return null;
            }
        }
    }
}
