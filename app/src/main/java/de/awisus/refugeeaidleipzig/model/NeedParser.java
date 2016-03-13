package de.awisus.refugeeaidleipzig.model;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created on 12.03.16.
 *
 * @author jens
 */
public class NeedParser {

    public static NeedParser INSTANCE = new NeedParser();

    private NeedParser() {}
    public static NeedParser getInstance() {
        return INSTANCE;
    }

    public DataMap<Bedarf> parse(DataMap<Kategorie> kategorien, JSONArray json) throws JSONException {
        DataMap<Bedarf> bedarf = new DataMap<>();

        for(int i = 0; i < json.length(); i++) {
            int[] ids = stringToInts(json.getString(i));

            Kategorie kategorie;
            kategorie = kategorien.getFromID(ids[0]);

            bedarf.add(i, kategorie.toBedarf(ids));
        }

        return bedarf;
    }

    private int[] stringToInts(String input) {
        String[] strings = input.split(",");

        int[] ints = new int[strings.length];
        for(int i = 0; i < ints.length; i++) {
            ints[i] = Integer.parseInt(strings[i]);
        }

        return ints;
    }
}
