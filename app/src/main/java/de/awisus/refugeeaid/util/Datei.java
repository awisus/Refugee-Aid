package de.awisus.refugeeaid.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created on 09.10.15.
 *
 * @author Jens Awisus
 */
public class Datei {

	public static Datei INSTANCE = new Datei();

    private Datei() {}
    public static Datei getInstance() {
        return INSTANCE;
    }


    public String lesen(Context context, String dateiname) throws IOException {

        if(context == null) return null;

        File datei = new File(context.getFilesDir(), dateiname);

        if(!datei.exists()) return null;

        BufferedReader br;
        br = new BufferedReader(new FileReader(datei));

        String zeile, gelesen = "";

        while((zeile = br.readLine()) != null) {
            gelesen += zeile;
            gelesen += "\n";
        }

        br.close();

        return gelesen.trim();
    }

    public void schreiben(Context context, String dateiname, String inhalt) throws IOException {

        if(context == null) return;

        File datei = new File(context.getFilesDir(), dateiname);
        if(!datei.exists()) {
            datei.getParentFile().mkdirs();
            datei.createNewFile();
        }

        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(datei));
        bw.write(inhalt.trim());

        bw.close();
    }

    public void loeschen(Context context, String dateiname) throws IOException {

        if(context == null) return;

        File datei = new File(context.getFilesDir(), dateiname);
        if(datei.exists()) {
            datei.delete();
        }
    }
}
