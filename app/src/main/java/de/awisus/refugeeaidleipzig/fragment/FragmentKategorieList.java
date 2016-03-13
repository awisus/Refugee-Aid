package de.awisus.refugeeaidleipzig.fragment;

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
import de.awisus.refugeeaidleipzig.model.Kategorie;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public class FragmentKategorieList extends DialogFragment implements AdapterView.OnItemClickListener {

    private ListView listView;

    private KategorieAdapter adapter;

    private Vector<Kategorie> liste;


    public static FragmentKategorieList newInstance(Vector<Kategorie> liste) {
        FragmentKategorieList frag = new FragmentKategorieList();
        frag.liste = liste;
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_list_kategorie, null);

        listView = (ListView) view.findViewById(android.R.id.list);

        adapter = new KategorieAdapter(getActivity(), android.R.layout.simple_list_item_1, liste);
        listView.setAdapter(adapter);

        return builder.setTitle(R.string.titel_bedarf_neu).setView(view).create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        Toast.makeText(getActivity(), liste.get(position).toString(), Toast.LENGTH_SHORT).show();
    }
}
