package de.awisus.refugeeaidleipzig.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Kategorie;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public class KategorieAdapter extends ArrayAdapter<Kategorie> {

    private Vector<Kategorie> liste;

    public KategorieAdapter(Context context, int resource, Vector<Kategorie> objects) {
        super(context, resource, objects);
        this.liste = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context ctx = parent.getContext();
        LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.entry_kategorie, parent, false);

        TextView tvName;
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(liste.get(position).getName());

        return view;
    }
}
