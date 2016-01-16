package de.awisus.refugeeaidleipzig.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.LinkedList;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created on 11.01.16.
 *
 * Fragment that is responsible for showing the model's information about accommodations and their
 * needs (collection of their resident's needs).
 * Main point is to show a Google Map with markers on the accommodation's locations and additional
 * information about sizes, number of residents and a list of needs for each accommodation
 * @author Jens Awisus
 */
public class FragmentKarte extends Fragment implements OnMapReadyCallback {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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
    public static FragmentKarte newInstance(Model model) {
        FragmentKarte frag = new FragmentKarte();
        frag.model = model;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Automatically called method inflating the xml-layout
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View inflated with the layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_karte, container, false);
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

        // get accommodations loaded into the model
        final LinkedList<Unterkunft> unterkuenfte = model.getUnterkuenfte();

        // Bring marker for each accommodation to the map
        for (int i = 0; i < unterkuenfte.size(); i++) {
            Marker marke = karte.addMarker(model.getMarkerOption(i));

            // Store this marker in the model
            model.addMarke(marke, i);
        }

        // Zoom on users accommodation, if logged in
        if(model.angemeldet()) {
            karte.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    model.getNutzerAktuell().getUnterkunft().getLatLng(), 11f)
            );
        } else {
            // Zoom to arbitrary accommodation
            karte.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    unterkuenfte.get(0).getLatLng(), 11f)
            );
        }

        // tap on marker info box shows accommodation detail
        karte.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marke) {

                // Get the accommodation for the marker being tabbed
                Unterkunft unterkunft = model.getUnterkunft(marke);
                String detail;
                /*
                 * Build up the details string to be shown in a separate window
                 * example format:
                 *
                 *   Spaces: 30
                 *   Residents: 28
                 *
                 *   Needs: Football, Jacket, Toothbrush
                 */

                detail  = getResources().getString(R.string.string_plaetze) + " ";
                detail += unterkunft.getGroesse();
                detail += "\n";
                detail += getResources().getString(R.string.string_bewohner) + " ";
                detail += unterkunft.findeBewohner();

                detail += "\n\n";

                detail += unterkunft.hatBedarf()
                        ? getResources().getString(R.string.string_bedarfe) + " "
                        + unterkunft.getBedarfeAlsString()
                        : getResources().getString(R.string.string_keine_bedarfe);

                // Call a new info fragment with name and details about the accommodation
                FragmentInfo.newInstance(
                        unterkunft.getName(),
                        detail
                ).show(getActivity().getSupportFragmentManager(), "Info");
            }
        });
    }
}
