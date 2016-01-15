package de.awisus.refugeeaidleipzig.model;

import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by Jens Awisus on 12.01.16.
 */
public class Nutzer extends Observable {

    private String name;
    private Unterkunft unterkunft;

    private LinkedList<String> bedarfe;


    public Nutzer(String name, Unterkunft unterkunft) {
        this.name = name.trim();
        this.unterkunft = unterkunft;

        bedarfe = new LinkedList<>();

        unterkunft.anmelden(this);
    }

    public void addBedarf(String bedarf) {
        bedarfe.add(bedarf);
        setChanged();
        notifyObservers();
    }

    public void loescheBedarf(String bedarf) {
        bedarfe.remove(bedarf);
        setChanged();
        notifyObservers();
    }


    public String getName() {
        return name;
    }

    public Unterkunft getUnterkunft() {
        return unterkunft;
    }

    public String getBedarfeAlsString() {
        if(!hatBedarf()) {
            return null;
        } else {
            String str = "";
            for(int i = 0; i < bedarfe.size(); i++) {
                str += bedarfe.get(i);
                if(i < bedarfe.size() - 1) {
                    str += ", ";
                }
            }

            return str;
        }
    }

    public boolean hatBedarf() {
        return bedarfe.size() > 0;
    }
}
