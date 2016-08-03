package de.awisus.refugeeaidleipzig;

/**
 * Created on 15.03.16.
 *
 * @author jens
 */
public class LoginData {

    private String name;
    private String mail;
    private String passwort;

    public LoginData(String name, String passwort) {
        this.name = name;
        this.mail = null;
        this.passwort = passwort;
    }

    public LoginData(String name, String mail, String passwort) {
        this(name, passwort);
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPasswort() {
        return passwort;
    }
}
