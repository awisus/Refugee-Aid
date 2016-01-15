package de.awisus.refugeeaidleipzig;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import de.awisus.refugeeaidleipzig.fragment.FragmentInfo;
import de.awisus.refugeeaidleipzig.fragment.FragmentKarte;
import de.awisus.refugeeaidleipzig.fragment.FragmentLogin;
import de.awisus.refugeeaidleipzig.fragment.FragmentProfil;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

/**
 * Created by Jens Awisus on 11.01.16.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Observer {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private DrawerLayout drawer;
    private FragmentInfo fragUeber;
    private FragmentLogin fragAnmelden;

    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initModel();

        Toolbar tb = initToolbar();
        initNavigationDrawer(tb);
        initContainer();

        fragUeber = FragmentInfo.newInstance(
                getResources().getString(R.string.nav_titel_ueber),
                getResources().getString(R.string.info)
        );

        fragAnmelden = FragmentLogin.newInstance(model);
    }

    private void initModel() {

        // initialise the model
        model = new Model();

        // this Activity listens to model changes (login and logout)
        model.addObserver(this);

        // try to retrieve read accommodations from json file
        try {
            RessourcenLader lader = new RessourcenLader(this);

            // run through all retrieved accommodations
            Unterkunft[] unterkuenfte = lader.getUnterkuenfte();
            for(int i = 0; i < unterkuenfte.length; i++) {
                Unterkunft unterkunft = unterkuenfte[i];

                // Insert into model's HashMap of Markers and accommodations
                model.addUnterkunft(unterkunft);
            }
        } catch (IOException | JSONException e) {
            // Exception: inform user and return
            Toast.makeText(this, R.string.warnung_laden, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initContainer() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, FragmentKarte.newInstance(model));
        transaction.commit();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Observer ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update(Observable observable, Object data) {
        wechsleFragment(FragmentProfil.newInstance(model.getNutzerAktuell()));
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listeners ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = (item.getItemId());

        // Show user profile if logged on
        if(id == R.id.nav_profil) {
            if(model.angemeldet()) {
                wechsleFragment(FragmentProfil.newInstance(model.getNutzerAktuell()));
            } else { // Else, show log on dialogue
                fragAnmelden.show(getSupportFragmentManager(), "Anmelden");
            }
        }

        // Switch to Google Map
        if(id == R.id.nav_karte) {
            wechsleFragment(FragmentKarte.newInstance(model));
        }

        // Show About Dialogue
        if(id == R.id.nav_ueber) {
            fragUeber.show(getSupportFragmentManager(), "Info");
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void wechsleFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
