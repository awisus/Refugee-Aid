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

package de.awisus.refugeeaidleipzig.model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Observable;
import java.util.Set;

/**
 * Created on 12.01.16.
 *
 * Class modelling the problem.
 * Simply is made up by a mapping of pre-given accommodations and GoogleMaps-specific Markers
 * In the second place, this class stores an active user logged on or is not.
 * This class extends Observable to provide Observers with information about user login und logout
 * @author Jens Awisus
 */
public class Model extends Observable {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * List of all pre-given accommodations
     */
    private DataMap<Unterkunft> unterkuenfte;

    /**
     * List of all offers created by users
     */
    private DataMap<Angebot> angebote;

    /**
     * List of all pre-given categories
     */
    private DataMap<Kategorie> kategorien;

    /**
     * Mapping of Map Markers and accommodations for easy information retrieval
     */
    private HashMap<Marker, Unterkunft> mapUnterkuenfte;
    private HashMap<MarkerOptions, Unterkunft> mapMarkerOptionenUnterkuenfte;

    /**
     * Mapping of Map Markers and offers for easy information retrieval
     */
    private HashMap<Marker, Angebot> mapAngebote;
    private HashMap<MarkerOptions, Angebot> mapMarkerOptionenAngebote;

    /**
     * Current user (may be null, if not logged in)
     */
    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * public constructor initialising lists and the HashMap
     */
    public Model() {
        mapUnterkuenfte = new HashMap<>();
        mapMarkerOptionenUnterkuenfte = new HashMap<>();
        mapAngebote = new HashMap<>();
        mapMarkerOptionenAngebote = new HashMap<>();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Log-In method to log in a user by creation with name and its accommodation
     * Notifies with TRUE, to inform Observer about login
     * @param nutzer user
     */
    public void anmelden(Nutzer nutzer) {
        this.nutzer = nutzer;

        // report change to Observer
        setChanged();
        notifyObservers(Boolean.TRUE);
    }

    /**
     * Log-out method setting user to null
     * Notifies with FALSE, to inform Observer about logout
     */
    public void abmelden() {
        nutzer = null;
        setChanged();
        notifyObservers(Boolean.FALSE);
    }

    /**
     * Checking, whether user is logged on (not equal to NULL)
     * @return true, if user is logged on; false else
     */
    public boolean angemeldet() {
        return nutzer != null;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Setters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public void setKategorien(DataMap<Kategorie> kategorien) {
        this.kategorien = kategorien;
    }

    /**
     * Setter for the accommodation list
     * @param unterkuenfte accommodation list to be set
     */
    public void setUnterkuenfte(DataMap<Unterkunft> unterkuenfte) {
        this.unterkuenfte = unterkuenfte;

        mapMarkerOptionenUnterkuenfte.clear();
        for(Unterkunft unterkunft : unterkuenfte) {
            addMarkerOption(unterkunft);
        }
    }

    public void setAngebote(DataMap<Angebot> angebote) {
        this.angebote = angebote;

        mapMarkerOptionenAngebote.clear();
        for(Angebot angebot : angebote) {
            addMarkerOption(angebot);
        }
    }

    /**
     * Private Method helping to add MarkerOptins to their list based upon an accommodation's data
     * @param unterkunft accommodation data with data to be got
     */
    private void addMarkerOption(Unterkunft unterkunft) {
        MarkerOptions markerOption;
        markerOption = new MarkerOptions();
        markerOption.title(unterkunft.toString());
        markerOption.position(unterkunft.getLatLng());

        mapMarkerOptionenUnterkuenfte.put(markerOption, unterkunft);
    }

    private void addMarkerOption(Angebot angebote) {
        MarkerOptions markerOption;
        markerOption = new MarkerOptions()
            .title(angebote.toString())
            .position(angebote.getLatLng())
            .icon(
                    BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        mapMarkerOptionenAngebote.put(markerOption, angebote);
    }

    /**
     * Method for adding MapMarkers to the HashMap for easy information retrieval.
     * Clears HashMap, if a very new set of Markers is to be stores (happens, if MapFragment is
     * newly created in Android view code)
     * @param marke Marker for Map
     * @param markerOption
     */
    public void addUnterkunftMarke(Marker marke, MarkerOptions markerOption) {
        if(unterkuenfte.size() == mapUnterkuenfte.size()) {
            mapUnterkuenfte.clear();
        }

        Unterkunft unterkunft;
        unterkunft = mapMarkerOptionenUnterkuenfte.get(markerOption);

        mapUnterkuenfte.put(marke, unterkunft);
    }

    public void addAngebotMarke(Marker marke, MarkerOptions markerOption) {
        if(angebote.size() == mapAngebote.size()) {
            mapAngebote.clear();
        }

        Angebot angebot;
        angebot = mapMarkerOptionenAngebote.get(markerOption);

        mapAngebote.put(marke, angebot);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public Set<MarkerOptions> getMarkerOptionenUnterkuenfte() {
        return mapMarkerOptionenUnterkuenfte.keySet();
    }

    public Set<MarkerOptions> getMarkerOptionenAngebote() {
        return mapMarkerOptionenAngebote.keySet();
    }

    /**
     * Method for getting an accommodation for a specific Map Marker
     * @param marke Map Marker of an accommodation
     * @return desired accommodation
     */
    public Unterkunft getUnterkunft(Marker marke) {
        return mapUnterkuenfte.get(marke);
    }

    public Angebot getAngebot(Marker marke) {
        return mapAngebote.get(marke);
    }

    /**
     * Get accommodation list
     * @return accommodation list
     */
    public DataMap<Unterkunft> getUnterkuenfte() {
        return unterkuenfte;
    }

    public DataMap<Angebot> getAngebote() {
        return angebote;
    }

    /**
     * Get accommodation list
     * @return accommodation list
     */
    public DataMap<Kategorie> getKategorien() {
        return kategorien;
    }

    /**
     * Get user logged on
     * @return user logged on
     */
    public Nutzer getNutzer() {
        return nutzer;
    }
}
