package de.awisus.refugeeaidleipzig.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;

/**
 * Created on 12.01.16.
 *
 *
 * @author Jens Awisus
 */
public class FragmentProfil extends Fragment implements Observer, View.OnClickListener {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Fragment for adding new needs by the user
     */
    private FragmentBedarfNeu fragmentBedarfNeu;

    /**
     * Fragment for removing new needs by the user
     */
    private FragmentBedarfEntfernen fragmentBedarfEntfernen;

    /**
     * Label showing the user name
     */
    private TextView tvName;

    /**
     * Label showing the accommodation the user stays in
     */
    private TextView tvEinrichtung;

    /**
     * Label listung the user's needs
     */
    private TextView tvBedarfe;

    /**
     * Log-Out button
     */
    private Button btAbmelden;

    /**
     * Reference to the model for logging the user out
     */
    private Model model;

    /**
     * The user reference
     */
    private Nutzer nutzer;

      ////////////////////////////////////////////////////////////////////////////////
     // Constructor /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Public factory method setting the model and the user reference in it
     * @param model Model to be set
     * @return new Profile Fragment
     */
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

        initUI(view);
        initNutzerInfo();

        getActivity().setTitle(R.string.titel_profil);

        return view;
    }

    private void initUI(View view) {
        fragmentBedarfNeu = FragmentBedarfNeu.newInstance(nutzer);
        fragmentBedarfEntfernen = FragmentBedarfEntfernen.newInstance(nutzer);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvEinrichtung = (TextView) view.findViewById(R.id.tvEinrichtung);
        tvBedarfe = (TextView) view.findViewById(R.id.tvBedarfe);

        btAbmelden = (Button) view.findViewById(R.id.btAbmelden);
        btAbmelden.setOnClickListener(this);

        FloatingActionButton btPlus = (FloatingActionButton) view.findViewById(R.id.fab_plus);
        FloatingActionButton btMinus = (FloatingActionButton) view.findViewById(R.id.fab_minus);
        btPlus.setOnClickListener(this);
        btMinus.setOnClickListener(this);
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
            String bedarfe;
            if ((bedarfe = nutzer.getBedarfeAlsListeString()) == null) {
                tvBedarfe.setText(R.string.string_keine_bedarfe);
            } else {
                tvBedarfe.setText(R.string.string_bedarfe);
                tvBedarfe.append("\n\n");
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

        if(id == R.id.fab_minus) {
            if(nutzer.hatBedarf()) {
                fragmentBedarfEntfernen.show(getChildFragmentManager(), "Bedarf entfernen");
            } else {
                Toast.makeText(getActivity(), R.string.warnung_loeschen, Toast.LENGTH_SHORT).show();
            }
        }

        if(id == R.id.btAbmelden) {
            model.abmelden();
        }
    }
}
