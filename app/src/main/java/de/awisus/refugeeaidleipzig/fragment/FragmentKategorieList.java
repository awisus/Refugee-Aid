package de.awisus.refugeeaidleipzig.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Vector;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Bedarf;
import de.awisus.refugeeaidleipzig.model.Kategorie;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public class FragmentKategorieList extends DialogFragment implements AdapterView.OnItemClickListener {

    private ListView listView;

    private KategorieAdapter adapter;

    private Nutzer nutzer;

    private Vector<Kategorie> liste;

    private Bedarf bedarf;


    public static FragmentKategorieList newInstance(Nutzer nutzer, Vector<Kategorie> liste, Bedarf bedarf) {
        FragmentKategorieList frag = new FragmentKategorieList();
        frag.nutzer = nutzer;
        frag.liste = liste;
        frag.bedarf = bedarf;
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_list_kategorie, null);

        adapter = new KategorieAdapter(getActivity(), android.R.layout.simple_list_item_1, liste);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return builder.setTitle(R.string.titel_bedarf_neu).setView(view).create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Kategorie kategorie = liste.get(position);
        bedarf.put(kategorie.getId(), kategorie.getName());

        Vector<Kategorie> subkategorien = kategorie.getSubkategorien();

        if(subkategorien.size() > 0) {
            FragmentKategorieList fragmentBedarfNeu;
            fragmentBedarfNeu = FragmentKategorieList.newInstance(nutzer, subkategorien, bedarf);

            fragmentBedarfNeu.show(getFragmentManager(), "Kategorie wählen");
        } else {
            new BedarfPost(getActivity(), R.string.meldung_hinzufuegen).execute(
                    "user_id",      "" + nutzer.getId(),
                    "category_id",  "" + bedarf.getId());
        }

        dismiss();
    }

    private class BedarfPost extends BackgroundTask<String, Integer, Integer> {

        private BedarfPost(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                return WebFlirt.getInstance().postBedarf(params);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void doPostExecute(Integer result) {

            if(result == null) {
                Toast.makeText(context, R.string.warnung_fehler, Toast.LENGTH_SHORT).show();
            } else {
                nutzer.addBedarf(bedarf);
            }
        }
    }
}
