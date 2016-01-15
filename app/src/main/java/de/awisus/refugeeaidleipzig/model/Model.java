package de.awisus.refugeeaidleipzig.model;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by Jens Awisus on 12.01.16.
 *
 * Class modelling the problem.
 * Simply is made up by a mapping of pre-given accommodations and GoogleMaps-specific Markers for
 * the map.
 * In the second place, this class stores an active user logged on or is not.
 */
public class Model extends Observable {

    /**
     * List of MarkerOptions storing information for each Marker to each accommodations
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
     * Current user (can be null, if not logged in)
     */
    private Nutzer nutzerAktuell;

    /**
     * public constructor initialising lists and the HashMap
     */
    public Model() {
        markerOptionen = new LinkedList<>();
        unterkuenfte = new LinkedList<>();
        mapUnterkuenfte = new HashMap<>();
    }

    /**
     * Log-In method to log in a user by creation with name and its accommodation
     * @param name user name
     * @param unterkunft user's accomodation
     */
    public void anmelden(String name, Unterkunft unterkunft) {
        nutzerAktuell = new Nutzer(name, unterkunft);
        setChanged();
        notifyObservers();
    }

    /**
     * Log-In method for an already created user
     * @param nutzer user, that is already is memory
     */
    public void anmelden(Nutzer nutzer) {
        nutzerAktuell = nutzer;
        setChanged();
        notifyObservers();
    }

    /**
     * Log-out method setting user to null
     */
    public void abmelden() {
        nutzerAktuell = null;
        setChanged();
        notifyObservers();
    }

    /**
     * Checking, whether is user is logged on (not equal to NULL)
     * @return true, if user is logged on; false else
     */
    public boolean angemeldet() {
        return nutzerAktuell != null;
    }

    /**
     * Method for adding pre-known accommodations dynamically to the model
     * @param unterkunft accommodations to be added
     */
    public void addUnterkunft(Unterkunft unterkunft) {
        MarkerOptions marke = new MarkerOptions();
        marke.title(unterkunft.getName());
        marke.position(unterkunft.getLatLng());

        markerOptionen.add(marke);
        unterkuenfte.add(unterkunft);
    }

    /**
     * Method for adding MapMarkers to the HashMap fpr easy information retrieval.
     * Clears HashMap, if a very new set of Markers is to be stores (happens, if MapFragment is
     * newly created in Android view code)
     * @param marke Marker for Ma
     * @param position Iterator indicating position in accommodation list to check for "full" Map
     */
    public void addMarke(Marker marke, int position) {
        Unterkunft unterkunft = unterkuenfte.get(position);
        if(unterkuenfte.size() == mapUnterkuenfte.size()) {
            mapUnterkuenfte.clear();
        }
        mapUnterkuenfte.put(marke, unterkunft);
    }

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
     * Getting accommodation list
     * @return accommodation list
     */
    public LinkedList<Unterkunft> getUnterkuenfte() {
        return unterkuenfte;
    }

    /**
     * Getting accommodation by a position
     * @param position position of accommodation to be got
     * @return desired accommodation
     */
    public Unterkunft getUnterkunft(int position) {
        return unterkuenfte.get(position);
    }

    /**
     * Get user logged on
     * @return user logged on
     */
    public Nutzer getNutzerAktuell() {
        return nutzerAktuell;
    }
}
