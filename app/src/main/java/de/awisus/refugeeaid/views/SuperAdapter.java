package de.awisus.refugeeaid.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

import de.awisus.refugeeaid.R;
import de.awisus.refugeeaid.models.IDObject;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public class SuperAdapter<T extends IDObject> extends ArrayAdapter<T> {

    protected Vector<T> liste;
    protected int layoutID;

    public SuperAdapter(Context context, int resource, Vector<T> objects, int layoutID) {
        super(context, resource, objects);
        this.liste = objects;
        this.layoutID = layoutID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context ctx = parent.getContext();
        LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(layoutID, parent, false);

        TextView tvName;
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvName.setText(liste.get(position).toString());

        doExtraBits(position, view);

        return view;
    }

    protected void doExtraBits(int position, View view) {}
}
