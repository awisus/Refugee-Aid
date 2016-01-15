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
 * Created by Jens Awisus on 11.01.16.
 */
public class FragmentKarte extends Fragment implements OnMapReadyCallback {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public static FragmentKarte newInstance(Model model) {
        FragmentKarte frag = new FragmentKarte();
        frag.model = model;
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_karte, container, false);
    }

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

    @Override
    public void onMapReady(GoogleMap karte) {

        // get accommodations loaded into the model
        final LinkedList<Unterkunft> unterkuenfte = model.getUnterkuenfte();

        // Bring marker for each accommodation to the map
        for (int i = 0; i < unterkuenfte.size(); i++) {
            // Marke anzeigen und speichern
            Marker marke = karte.addMarker(model.getMarkerOption(i));
            model.addMarke(marke, i);
        }

        // Zoom on users accommodation, if logged on
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

                Unterkunft unterkunft = model.getUnterkunft(marke);
                String detail = getResources().getString(R.string.string_plaetze) + " ";
                detail += unterkunft.getGroesse();
                detail += "\n\n";
                detail += unterkunft.hatBedarf()
                        ? getResources().getString(R.string.string_bedarfe) + " "
                        + unterkunft.getBedarfeAlsString()
                        : getResources().getString(R.string.string_keine_bedarfe);

                FragmentInfo.newInstance(
                        unterkunft.getName(),
                        detail
                ).show(getActivity().getSupportFragmentManager(), "Info");
            }
        });
    }
}
