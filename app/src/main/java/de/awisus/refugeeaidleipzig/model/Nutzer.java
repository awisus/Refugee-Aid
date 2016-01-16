package de.awisus.refugeeaidleipzig.model;

import java.util.LinkedList;
import java.util.Observable;

/**
 * Created on 12.01.16.
 *
 * Class providing an implementation of a user being resident of an accommodation
 * This class extends Observable to provide Observers with information about needs being added or
 * removed
 * @author Jens Awisus
 */
public class Nutzer extends Observable {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * User name
     */
    private String name;

    /**
     * Accommodation this user stays in
     */
    private Unterkunft unterkunft;

    /**
     * List of needs being uttered to the public
     */
    private LinkedList<String> bedarfe;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public constructor to instantiate with name and accommodation
     * instantiates list of needs, processes login into given accommodation
     * @param name user name
     * @param unterkunft accommodation
     */
    public Nutzer(String name, Unterkunft unterkunft) {
        this.name = name.trim();
        this.unterkunft = unterkunft;

        bedarfe = new LinkedList<>();

        unterkunft.anmelden(this);
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Methods /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for adding new need to this resident
     * @param bedarf new need to be added
     */
    public void addBedarf(String bedarf) {
        bedarfe.add(bedarf);
        setChanged();
        notifyObservers();
    }

    /**
     * Method for removing a need from the list of needs
     * @param bedarf need to be removed
     */
    public void loescheBedarf(String bedarf) {
        bedarfe.remove(bedarf);
        setChanged();
        notifyObservers();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Getters /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Get user name
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Get accommodation this user stays in
     * @return accommodation this user stays in
     */
    public Unterkunft getUnterkunft() {
        return unterkunft;
    }

    /**
     * Getter for personal needs
     * @return list of this user's needs
     */
    public LinkedList<String> getBedarfe() {
        return bedarfe;
    }

    /**
     * Turns list of needs this into a comma-separated string to be shown on map and profile page
     * @return comma-separated string of personal needs
     */
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

    /**
     * Information whether this user has needs
     * @return true, if list of needs > 0; false else
     */
    public boolean hatBedarf() {
        return bedarfe.size() > 0;
    }
}
