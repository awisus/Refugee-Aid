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
 * Created on 11.01.16.
 *
 * Class defining the Main Activity of the Android App as entry point.
 * Describes behaviour for the Navigation Drawer and takes notice about login and logout behaviour
 * of the user stored in the model.
 * Two windows may be called: One shows general information about the app. The second is a
 * login mask, that pops up, if no user is signed in.
 * @author Jens Awisus
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Observer {

      ////////////////////////////////////////////////////////////////////////////////
     // Attributes //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Layout for the Activity's tool bar and navigation drawer
     */
    private DrawerLayout drawer;

    /**
     * Instance of the About dialogue
     */
    private FragmentInfo fragUeber;

    /**
     * Instance of a login mask
     */
    private FragmentLogin fragAnmelden;

    /**
     * Instance of modelled problem
     */
    private Model model;

      ////////////////////////////////////////////////////////////////////////////////
     // View creation ///////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * /**
     * Called when this activity is created; Android-specific
     * Inflates the layout, initialises the model, tool bar, navigation drawer and the container
     * layout for fragments (Google Map or profile fragment)
     * Prepares instances of the About dialogue and the login mask
     * @param savedInstanceState Bundle of saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set the view by layout xml file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init all components
        initModel();

        Toolbar tb = initToolbar();
        initNavigationDrawer(tb);
        initContainer();

        // prepare instances of the About and Login dialogues
        fragUeber = FragmentInfo.newInstance(
                getResources().getString(R.string.nav_titel_ueber),
                getResources().getString(R.string.info)
        );

        fragAnmelden = FragmentLogin.newInstance(model);
    }

    /**
     * Private method to initialise the model and data to be stored in.
     * (Accommodations and map markers)
     */
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

                // insert into model's HashMap of Markers and accommodations
                model.addUnterkunft(unterkunft);
            }
        } catch (IOException | JSONException e) {
            // Exception: inform user and return
            Toast.makeText(this, R.string.warnung_laden, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * Private method for initialising the tool bar by layout file
     * @return the tool bar
     */
    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    /**
     * Private method for initialising the nav drawer from its layout file
     * @param toolbar tool bar belonging to the nev drawer
     */
    private void initNavigationDrawer(Toolbar toolbar) {

        // find drawer by layout id
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // listen for toogle between open and closed nav drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Listen for item selection in nav drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Private method starting with adding the map fragment to the fragment container
     */
    private void initContainer() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, FragmentKarte.newInstance(model));
        transaction.commit();
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Observer ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Called, if data model reports changes in data set
     * data == Boolean.TRUE, if user logs in and Boolean.FALSE, if user logs out
     * @param observable model to be observed
     * @param data data sent from the model
     */
    @Override
    public void update(Observable observable, Object data) {
        // Check whether model returns log in or log off by user
        Boolean anmeldung = (Boolean) data;
        if(anmeldung.equals(Boolean.TRUE)) {    // Show profile on log in
            wechsleFragment(FragmentProfil.newInstance(model));
        } else {                                // return to map on log off
            wechsleFragment(FragmentKarte.newInstance(model));
        }
    }

      ////////////////////////////////////////////////////////////////////////////////
     // Listeners ///////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Control of the Android back key
     * closes nav drawer, if it is open
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Control of item selection in the nav drawer
     * changes fragment container to profile fragment, if profile is chosen
     *   if user is logged off, the login mask shows up
     * changes fragment container to map fragment, if map is chosen
     * pops up About dialogue, if About is chosen
     * closes drawer
     * @param item pressed nav menu item
     * @return true for success
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = (item.getItemId());

        // Show user profile if logged on
        if(id == R.id.nav_profil) {
            if(model.angemeldet()) {
                wechsleFragment(FragmentProfil.newInstance(model));
            } else {
                // Else, show login dialogue
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

        // Close drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * private method to change fragment of the fragment container
     * @param fragment fragment to be shown in the fragment container
     */
    private void wechsleFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
