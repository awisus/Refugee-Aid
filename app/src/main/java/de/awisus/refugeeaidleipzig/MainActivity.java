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

import de.awisus.refugeeaidleipzig.fragment.FragmentInfo;
import de.awisus.refugeeaidleipzig.fragment.FragmentKarte;
import de.awisus.refugeeaidleipzig.fragment.FragmentProfil;
import de.awisus.refugeeaidleipzig.model.Model;
import de.awisus.refugeeaidleipzig.model.Nutzer;
import de.awisus.refugeeaidleipzig.model.Unterkunft;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FragmentInfo fragUeber;

    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // Oberflaeche erzeugen ////////////////////////////////////////////////////////
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
    }

    private void initModel() {
        model = new Model();

        try {
            RessourcenLader lader = new RessourcenLader(this);

            Unterkunft[] unterkuenfte = lader.getUnterkuenfte();
            for(int i = 0; i < unterkuenfte.length; i++) {
                Unterkunft unterkunft = unterkuenfte[i];

                // Eintrag in HashMap des Models
                model.addUnterkunft(unterkunft);
            }
        } catch (IOException | JSONException e) {
            Toast.makeText(this, R.string.warnung_laden, Toast.LENGTH_SHORT).show();
            return;
        }

        Nutzer dummy = new Nutzer("Jens Awisus", model.getUnterkunft(2));
        dummy.addBedarf("Kuchen");
        dummy.addBedarf("Bettdecke");

        model.anmelden(dummy);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initNavigationDrawer(Toolbar toolbar) {

        // Navigation Drawer
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

        // Fragment fuer Kartenansicht
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, FragmentKarte.newInstance(model));
        transaction.commit();
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
        if(id == R.id.nav_profil) {
            wechsleFragment(R.id.fragment_container, FragmentProfil.newInstance(model.getNutzerAktuell()));
        }
        if(id == R.id.nav_karte) {
            wechsleFragment(R.id.fragment_container, FragmentKarte.newInstance(model));
        }
        if(id == R.id.nav_ueber) {
            fragUeber.show(getSupportFragmentManager(), "Info");
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void wechsleFragment(int container, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(container, fragment);
        transaction.commit();
    }
}
