package de.awisus.refugeeaidleipzig.model;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

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
     * List of MarkerOptions storing information for each Marker for each accommodation
     */
    private LinkedList<MarkerOptions> markerOptionen;

    /**
     * List of all pre-given accommodations
     */
    private LinkedList<Unterkunft> unterkuenfte;

    /**
     * Mapping of Map Markers and accommodations for easy information retrieval
     */
    private HashMap<Marker, Unterkunft> mapUnterkuenfte;

    /**
     * Current user (may be null, if not logged in)
     */
    private Nutzer nutzerAktuell;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * public constructor initialising lists and the HashMap
     */
    public Model() {
        markerOptionen = new LinkedList<>();
        unterkuenfte = new LinkedList<>();
        mapUnterkuenfte = new HashMap<>();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Log-In method to log in a user by creation with name and its accommodation
     * Notifies with TRUE, to inform Observer about login
     * @param name user name
     * @param unterkunft user's accomodation
     */
    public void anmelden(String name, Unterkunft unterkunft) {
        Nutzer bewohner = unterkunft.findeBewohner(name.trim());
        if(bewohner != null) {
            nutzerAktuell = bewohner;
        } else {
            nutzerAktuell = new Nutzer(name, unterkunft);
        }

        // report change to Observer
        setChanged();
        notifyObservers(Boolean.TRUE);
    }

    /**
     * Log-out method setting user to null
     * Notifies with FALSE, to inform Observer about logout
     */
    public void abmelden() {
        nutzerAktuell = null;
        setChanged();
        notifyObservers(Boolean.FALSE);
    }

    /**
     * Checking, whether user is logged on (not equal to NULL)
     * @return true, if user is logged on; false else
     */
    public boolean angemeldet() {
        return nutzerAktuell != null;
    }

    /**
     * Method for adding MapMarkers to the HashMap for easy information retrieval.
     * Clears HashMap, if a very new set of Markers is to be stores (happens, if MapFragment is
     * newly created in Android view code)
     * @param marke Marker for Map
     * @param position Iterator indicating position in accommodation list to check for "full" Map
     */
    public void addMarke(Marker marke, int position) {
        Unterkunft unterkunft = unterkuenfte.get(position);
        if(unterkuenfte.size() == mapUnterkuenfte.size()) {
            mapUnterkuenfte.clear();
        }
        mapUnterkuenfte.put(marke, unterkunft);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Setters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Setter for the accommodation list
     * @param unterkuenfte accommodation list to be set
     */
    public void setUnterkuenfte(LinkedList<Unterkunft> unterkuenfte) {
        this.unterkuenfte = unterkuenfte;

        for(Unterkunft unterkunft : unterkuenfte) {
            addMarkerOption(unterkunft);
        }
    }

    /**
     * Private Method helping to add MarkerOptins to their list based upon an accommodation's data
     * @param unterkunft accommodation data with data to be got
     */
    private void addMarkerOption(Unterkunft unterkunft) {
        MarkerOptions marke = new MarkerOptions();
        marke.title(unterkunft.getName());
        marke.position(unterkunft.getLatLng());

        markerOptionen.add(marke);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter method for specific MarkerOption in MarkerOption list
     * @param position position of MarkerOption to be got
     * @return desired MarkerOption
     */
    public MarkerOptions getMarkerOption(int position) {
        return markerOptionen.get(position);
    }

    /**
     * Method for getting an accommodation for a specific Map Marker
     * @param marke Map Marker of an accommodation
     * @return desired accommodation
     */
    public Unterkunft getUnterkunft(Marker marke) {
        return mapUnterkuenfte.get(marke);
    }

    /**
     * Get accommodation list
     * @return accommodation list
     */
    public LinkedList<Unterkunft> getUnterkuenfte() {
        return unterkuenfte;
    }

    /**
     * Get user logged on
     * @return user logged on
     */
    public Nutzer getNutzerAktuell() {
        return nutzerAktuell;
    }
}
