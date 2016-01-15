package de.awisus.refugeeaidleipzig.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Nutzer;

/**
 * Created by Jens Awisus on 12.01.16.
 */
public class FragmentProfil extends Fragment implements Observer, View.OnClickListener {

    private FragmentBedarfNeu fragmentBedarfNeu;
    private View view;

    private TextView tvName;
    private TextView tvEinrichtung;

    private Nutzer nutzer;

    public static FragmentProfil newInstance(Nutzer nutzer) {
        FragmentProfil frag = new FragmentProfil();
        frag.nutzer = nutzer;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        this.view = view;

        initUI();
        initNutzerInfo();

        return view;
    }

    private void initUI() {
        fragmentBedarfNeu = FragmentBedarfNeu.newInstance(nutzer);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEinrichtung = (TextView) view.findViewById(R.id.tvEinrichtung);

        FloatingActionButton btPlus = (FloatingActionButton) view.findViewById(R.id.fab_plus);
        btPlus.setOnClickListener(this);
    }

    private void initNutzerInfo() {
        if(nutzer != null) {
            tvName.setText(nutzer.getName());
            tvEinrichtung.setText(nutzer.getUnterkunft().getName());

            updateBedarfe();

            nutzer.addObserver(this);
        }
    }


    @Override
    public void update(Observable observable, Object data) {
        updateBedarfe();
    }

    private void updateBedarfe() {
        if(nutzer != null) {
            TextView tvBedarfe = (TextView) view.findViewById(R.id.tvBedarfe);
            String bedarfe;
            if ((bedarfe = nutzer.getBedarfeAlsString()) == null) {
                tvBedarfe.setText(R.string.string_keine_bedarfe);
            } else {
                tvBedarfe.setText(R.string.string_bedarfe);
                tvBedarfe.append(" ");
                tvBedarfe.append(bedarfe);
            }
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.fab_plus) {
            fragmentBedarfNeu.show(getChildFragmentManager(), "Neuer Bedarf");
        }
    }
}
