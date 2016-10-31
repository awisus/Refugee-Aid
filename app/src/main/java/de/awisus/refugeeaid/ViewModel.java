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

package de.awisus.refugeeaid;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Observable;
import java.util.Set;

import de.awisus.refugeeaid.models.Angebot;
import de.awisus.refugeeaid.models.DataMap;
import de.awisus.refugeeaid.models.ILocationDataObject;
import de.awisus.refugeeaid.models.Kategorie;
import de.awisus.refugeeaid.models.Nutzer;
import de.awisus.refugeeaid.models.Unterkunft;

/**
 * Created on 12.01.16.
 *
 * Class modelling the problem.
 * Simply is made up by a mapping of pre-given accommodations and GoogleMaps-specific Markers
 * In the second place, this class stores an active user logged on or is not.
 * This class extends Observable to provide Observers with information about user login und logout
 * @author Jens Awisus
 */
public class ViewModel extends Observable {

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
     * Mapping of Map Markers and accommodations or offers for easy information
     * retrieval
     */
    private HashMap<Marker, ILocationDataObject> mapLocationData;
    private HashMap<MarkerOptions, ILocationDataObject> mapLocationDataOptions;

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
    public ViewModel() {
        mapLocationData = new HashMap<>();
        mapLocationDataOptions = new HashMap<>();
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

        for(Unterkunft unterkunft : unterkuenfte) {
            addMarkerOption(unterkunft);
        }
    }

    public void setAngebote(DataMap<Angebot> angebote) {
        this.angebote = angebote;

        for(Angebot angebot : angebote) {
            addMarkerOption(angebot);
        }
    }

    public void clearLocationData() {
        mapLocationDataOptions.clear();
        mapLocationData.clear();
    }

    /**
     * Private Method helping to add MarkerOptins to their list based upon an accommodation's data
     * @param unterkunft accommodation data with data to be got
     */
    private void addMarkerOption(Unterkunft unterkunft) {
        MarkerOptions markerOption;
        markerOption = new MarkerOptions()
            .title(unterkunft.toString())
            .position(unterkunft.getLatLng())
            .icon(
                    BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_ORANGE));

        mapLocationDataOptions.put(markerOption, unterkunft);
    }

    private void addMarkerOption(Angebot angebote) {
        MarkerOptions markerOption;
        markerOption = new MarkerOptions()
            .title(angebote.toString())
            .position(angebote.getLatLng())
            .icon(
                    BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_VIOLET));

        mapLocationDataOptions.put(markerOption, angebote);
    }

    /**
     * Method for adding MapMarkers to the HashMap for easy information
     * retrieval.
     * @param marke Marker for Map
     * @param markerOption DataSet of a marker
     */
    public void addMarke(Marker marke, MarkerOptions markerOption) {
        ILocationDataObject data = mapLocationDataOptions.get(markerOption);
        mapLocationData.put(marke, data);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public Set<MarkerOptions> getMarkerOptionen() {
        return mapLocationDataOptions.keySet();
    }

    /**
     * Method for getting an accommodation or offer for a specific Map Marker
     * @param marke Map Marker of an accommodation/offer
     * @return desired accommodation/offer
     */
    public ILocationDataObject getData(Marker marke) {
        return mapLocationData.get(marke);
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
