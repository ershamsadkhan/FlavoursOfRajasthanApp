package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.Alert.Alert;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.TextStorage;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    FragmentManager fragmentManager;
    public FloatingActionButton fab;

    TextStorage txtStorage;
    Alert alert;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        alert = new Alert(this);
        txtStorage = new TextStorage(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fragmentManager = getSupportFragmentManager();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempOrderDto = txtStorage.getCartData();
                if (tempOrderDto == "") {
                    alert.alertMessage("No Items present in the cart");
                }
                else {
                    fragment = new CartFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, fragment)
                            .addToBackStack("tag")
                            .commit();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new HomeFragment());
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            if (fab != null) {
                fab.show();
            }
            fragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .commit();
        } else if (id == R.id.nav_profile) {
            fragment = new LoginFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        } else if (id == R.id.nav_pastorder) {
            if (fab != null) {
                fab.show();
            }
            fragment = new PastOrderFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        } else if (id == R.id.nav_trackorder) {
            userId=txtStorage.getUserId();
            if(userId==""){
                alert.alertMessage(userId);
                return true ;
            }
            if (fab != null) {
                fab.show();
            }
            fragment = new TrackOrderFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        }else if (id == R.id.nav_notifications) {
            if (fab != null) {
                fab.show();
            }
            fragment = new TrackOrderFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        }
        else if (id == R.id.nav_rateus) {
            RateUs();
        }
        else if (id == R.id.nav_share) {
            Share();
        }
        else if (id == R.id.nav_terms) {
            int count = fragmentManager.getBackStackEntryCount();
            for(int i = 0; i < count-1; ++i) {
                fragmentManager.popBackStack();
            }

            fragment = new TermsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        }
        else if (id == R.id.nav_help) {
            int count = fragmentManager.getBackStackEntryCount();
            for(int i = 0; i < count; ++i) {
                fragmentManager.popBackStack();
            }
            fragment = new SupportFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void RateUs(){
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    public void Share(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Flavours Of Rajasthan");
            String sAux = "\nHey Please Download This Cool Food App\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id="+  this.getPackageName()+"\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            e.toString();
        }
    }
}
