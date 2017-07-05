package com.flavoursofrajasthan.sam.flavoursofrajasthan;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import static android.content.ContentValues.TAG;

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
        //tx.addToBackStack("tag");
        tx.commit();

        if(isStoragePermissionGranted()){

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //alert.alertMessage("Back buuton pressed");
    }



    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        int count = fragmentManager.getBackStackEntryCount();


        if (id == R.id.nav_home) {

            if (fab != null) {
                fab.show();
            }
            if (count > 0) {
                fragmentManager.popBackStack();
            }
            fragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack(HomeFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_profile) {
            fragment = new LoginFragment();
            if (count > 0) {
                fragmentManager.popBackStack();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack(LoginFragment.class.getName())
                    .commit();
        } else if (id == R.id.nav_pastorder) {
            userId=txtStorage.getUserName();
            if(userId==""){
                fragment = new LoginFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .addToBackStack(LoginFragment.class.getName())
                        .commit();
            }else {
                if (fab != null) {
                    fab.show();
                }
                fragment = new PastOrderFragment();
                if (count > 0) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .addToBackStack(PastOrderFragment.class.getName())
                        .commit();
            }
        } else if (id == R.id.nav_trackorder) {
            userId=txtStorage.getUserName();
            if(userId==""){
                fragment = new LoginFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .addToBackStack(LoginFragment.class.getName())
                        .commit();
            }
            else {
                if (fab != null) {
                    fab.show();
                }
                fragment = new TrackOrderFragment();
                if (count > 0) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, fragment)
                        .addToBackStack(TrackOrderFragment.class.getName())
                        .commit();
            }
        }else if (id == R.id.nav_notifications) {
            if (fab != null) {
                fab.show();
            }
            fragment = new OffersFragment();
            if (count > 0) {
                fragmentManager.popBackStack();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack(OffersFragment.class.getName())
                    .commit();
        }
        else if (id == R.id.nav_rateus) {
            RateUs();
        }
        else if (id == R.id.nav_share) {
            Share();
        }
        else if (id == R.id.nav_terms) {

            fragment = new TermsFragment();
            //fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        }
        else if (id == R.id.nav_help) {
            fragment = new SupportFragment();
            //fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .addToBackStack("tag")
                    .commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("Flavours of Rajasthan","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
