package de.awisus.refugeeaidleipzig.model;

/**
 * Created by Jens Awisus on 11.01.16.
 */
public class Bedarf {

    private String name;
    private Kategorie kategorie;
    private int menge;


    public Bedarf(String name, Kategorie kategorie, int menge) {
        this.name = name;
        this.kategorie = kategorie;
        this.menge = menge;
    }


    public String getName() {
        return name;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public int getMenge() {
        return menge;
    }
}
