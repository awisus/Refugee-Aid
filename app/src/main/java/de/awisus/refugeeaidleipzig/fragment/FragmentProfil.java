package de.awisus.refugeeaidleipzig.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;

/**
 * Created by Jens Awisus on 12.01.16.
 */
public class FragmentProfil extends Fragment implements Observer, View.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private FragmentBedarfNeu fragmentBedarfNeu;
    private View view;

    private TextView tvName;
    private TextView tvEinrichtung;

    private Button btAbmelden;

    private Model model;
    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    public static FragmentProfil newInstance(Model model) {
        FragmentProfil frag = new FragmentProfil();
        frag.model = model;
        frag.nutzer = model.getNutzerAktuell();
        return frag;
    }

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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

        btAbmelden = (Button) view.findViewById(R.id.btAbmelden);
        btAbmelden.setOnClickListener(this);

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

      ////////////////////////////////////////////////////////////////////////////////
     // Observer ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

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

      ////////////////////////////////////////////////////////////////////////////////
     // Listener ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.fab_plus) {
            fragmentBedarfNeu.show(getChildFragmentManager(), "Neuer Bedarf");
        }

        if(id == R.id.btAbmelden) {
            model.abmelden();
        }
    }
}
