package de.awisus.refugeeaidleipzig.model;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by Jens Awisus on 12.01.16.
 */
public class Model extends Observable {

    private LinkedList<MarkerOptions> markerOptionen;
    private LinkedList<Unterkunft> unterkuenfte;
    private HashMap<Marker, Unterkunft> mapUnterkuenfte;

    private Nutzer nutzerAktuell;


    public Model() {
        markerOptionen = new LinkedList<>();
        unterkuenfte = new LinkedList<>();
        mapUnterkuenfte = new HashMap<>();
    }


    public void anmelden(String name, Unterkunft unterkunft) {
        nutzerAktuell = new Nutzer(name, unterkunft);
        setChanged();
        notifyObservers();
    }

    public void anmelden(Nutzer nutzer) {
        nutzerAktuell = nutzer;
        setChanged();
        notifyObservers();
    }

    public void abmelden() {
        nutzerAktuell = null;
        setChanged();
        notifyObservers();
    }

    public boolean angemeldet() {
        return nutzerAktuell != null;
    }


    public void addBedarf(String bedarf) {
        if(nutzerAktuell != null) {
            nutzerAktuell.addBedarf(bedarf);
            setChanged();
            notifyObservers();
        }
    }

    public void addUnterkunft(Unterkunft unterkunft) {
        MarkerOptions marke = new MarkerOptions();
        marke.title(unterkunft.getName());
        marke.position(unterkunft.getLatLng());

        markerOptionen.add(marke);
        unterkuenfte.add(unterkunft);
    }

    public void addMarke(Marker marke, int position) {
        Unterkunft unterkunft = unterkuenfte.get(position);
        if(unterkuenfte.size() == mapUnterkuenfte.size()) {
            mapUnterkuenfte.clear();
        }
        mapUnterkuenfte.put(marke, unterkunft);
    }

    public MarkerOptions getMarkerOption(int position) {
        return markerOptionen.get(position);
    }

    public Unterkunft getUnterkunft(Marker marke) {
        return mapUnterkuenfte.get(marke);
    }

    public LinkedList<Unterkunft> getUnterkuenfte() {
        return unterkuenfte;
    }

    public Unterkunft getUnterkunft(int position) {
        return unterkuenfte.get(position);
    }

    public Nutzer getNutzerAktuell() {
        return nutzerAktuell;
    }
}
