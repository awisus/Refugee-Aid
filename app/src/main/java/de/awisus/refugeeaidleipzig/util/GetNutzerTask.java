package de.awisus.refugeeaidleipzig.util;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import de.awisus.refugeeaidleipzig.model.LoginData;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;

/**
 * Created on 15.03.16.
 *
 * @author jens
 */
public abstract class GetNutzerTask extends BackgroundTask<String, Integer, Nutzer> {

    /**
     * Reference to the model to log in the new user (or to be found in the chosen accommodation)
     */
    protected Model model;

    protected int warnungID;

    protected LoginData login;

    protected GetNutzerTask(Activity context, int textID,  LoginData login) {
        super(context, textID);
        this.login = login;
    }

    @Override
    protected void doPostExecute(Nutzer result) {

        if(result == null) {
            Toast.makeText(context, warnungID, Toast.LENGTH_SHORT).show();
        } else {
            model.anmelden(result);

            // save login data
            try {
                Datei.getInstance().schreiben(context, "login.json", new Gson().toJson(login));
            } catch (IOException e) {}
        }
        dismiss();
    }
}
