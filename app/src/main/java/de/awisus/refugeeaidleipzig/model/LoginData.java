package de.awisus.refugeeaidleipzig.model;

/**
 * Created on 15.03.16.
 *
 * @author jens
 */
public class LoginData {

    private String name;
    private String passwort;

    public LoginData(String name, String passwort) {
        this.name = name;
        this.passwort = passwort;
    }

    public String getName() {
        return name;
    }

    public String getPasswort() {
        return passwort;
    }
}
