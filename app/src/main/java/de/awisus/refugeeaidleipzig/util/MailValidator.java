package de.awisus.refugeeaidleipzig.util;

import android.util.Patterns;

/**
 * Created on 12.03.16.
 *
 * @author jens
 */
public class MailValidator {

    public static final MailValidator INSTANCE = new MailValidator();

    private MailValidator() {}
    public static MailValidator getInstance() {
        return INSTANCE;
    }

    public boolean isValid(String mail) {
        if(mail.isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(mail).matches();
    }
}
