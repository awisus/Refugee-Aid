package de.awisus.refugeeaid.views.profile;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Vector;

import de.awisus.refugeeaid.R;
import de.awisus.refugeeaid.models.Bedarf;
import de.awisus.refugeeaid.models.Kategorie;
import de.awisus.refugeeaid.models.Nutzer;
import de.awisus.refugeeaid.net.WebFlirt;
import de.awisus.refugeeaid.util.BackgroundTask;
import de.awisus.refugeeaid.views.SuperAdapter;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public class FragmentKategorieList extends DialogFragment implements AdapterView.OnItemClickListener {

    private ListView listView;

    private SuperAdapter<Kategorie> adapter;

    private Nutzer nutzer;

    private Vector<Kategorie> liste;


    public static FragmentKategorieList newInstance(Nutzer nutzer, Vector<Kategorie> liste) {
        FragmentKategorieList frag = new FragmentKategorieList();
        frag.nutzer = nutzer;
        frag.liste = liste;
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogue_list_kategorie, null);

        adapter = new AdapterCategories(getActivity(), android.R.layout.simple_list_item_1, liste, R.layout.entry_kategorie);

        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setDivider(null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return builder.setTitle(R.string.titel_bedarf_neu).setView(view).create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Kategorie kategorie = liste.get(position);

        Vector<Kategorie> subkategorien = kategorie.getSubkategorien();

        if(subkategorien.size() > 0) {
            FragmentKategorieList fragmentBedarfNeu;
            fragmentBedarfNeu = FragmentKategorieList.newInstance(nutzer, subkategorien);

            fragmentBedarfNeu.show(getFragmentManager(), "Kategorie wählen");
        } else {
            new BedarfPost(getActivity(), R.string.meldung_hinzufuegen).execute(
                    "user_id",      "" + nutzer.getId(),
                    "category_id",  "" + kategorie.getId()
            );
        }

        dismiss();
    }

    private class BedarfPost extends BackgroundTask<String, Integer, Bedarf> {

        private BedarfPost(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected Bedarf doInBackground(String... params) {
            try {
                String antwort = WebFlirt.post("needs_remote", params);
                return Bedarf.fromJSON(new JSONObject(antwort));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void doPostExecute(Bedarf result) {

            if(result == null) {
                Toast.makeText(context, R.string.warnung_bedarf_vorhanden, Toast.LENGTH_SHORT).show();
            } else {
                nutzer.addData(result);
            }
        }
    }
}
