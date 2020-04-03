package com.example.javaprojone;


import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;

import steelkiwi.com.library.DotsLoaderView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DotsLoaderView dotsLoaderView;
    public static boolean course = false;
    public static boolean start = true;
    public static DrawerLayout drawer;
    public static NavigationView navigationView;
    public static BottomNavigationView bottomNavigationView;
    public static LinearLayout l;
    public static String heads;
    public static String type;
    public static String posted_by;
    public static String department;
    public static String club;
    public static List_Item li;
    public static boolean inSWrite=false;
    public static Calendar Day;
    public static SQLite_populator s;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dotsLoaderView = findViewById(R.id.dotsLoader);
        downloadDemo();


        Toolbar toolbar = findViewById(R.id.toolbar);//Need to use the version 7 Toolbar type
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        //FirebaseApp.initializeApp(this);

        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(botNavListener);

        navigationView.setNavigationItemSelectedListener(this);//Set a listener that will be notified when a menu item is selected. Because of this we need to implement NavigationView.OnNavigationItemSelectedListener(Select Navigation view one)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);//The top left button (hamburger) to open drawer and rotate itself
        drawer.addDrawerListener(toggle);
        toggle.syncState();//To rotate hamburger item while opening


        if (start) {
            Log.i("1","Start");
            l = findViewById(R.id.Main);
            l.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new course_add_fragment()).addToBackStack("Tag").commit();


        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener botNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_announcements:
                            //navigationView.setCheckedItem(R.id.nav_home);

                            selectedFragment = new announcements_fragment();
                            break;
                        case R.id.nav_club:
                            //  navigationView.setCheckedItem(R.id.nav_fav);
                            selectedFragment = new Club_fragment();
                            break;
                        case R.id.nav_course:
                            //   navigationView.setCheckedItem(R.id.nav_search);
                            selectedFragment = new course_fragment();
                            break;
                        case R.id.nav_event:
                            //   navigationView.setCheckedItem(R.id.nav_search);
                            selectedFragment = new Event_fragment();
                            break;

                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).addToBackStack("Tag").commit();

                    return true;
                }
            };


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.nav_announcements:
                bottomNavigationView.setSelectedItemId(R.id.nav_announcements);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new announcements_fragment()).addToBackStack("Tag").commit();
                break;
            case R.id.nav_login:
                Log.i("sf","assa");
                if(Login_fragment.logged_in==true) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SuperWriteFragment()).addToBackStack("Tag").commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Login_fragment()).addToBackStack("Login").commit();
                }
                break;
            case R.id.writeondb:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SuperWriteFragment()).addToBackStack("Tag").commit();
            case R.id.nav_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SuperAddFragment()).addToBackStack("Tag").commit();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;//false means no action selected. so make it true
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()==3 ) // If there are minimal number of fragments. [3 because of circling through initially to add stuff to nav drawer]
        {
            // Do nothing
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Are you sure you want to exit the app?");
//            builder.setCancelable(true);
//            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    start = true;
//                    course = false;
////                    while(getSupportFragmentManager().getBackStackEntryCount()>=1)
////                        getSupportFragmentManager().popBackStackImmediate();
//                    finish();
//
//                }
//            });
//            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog al = builder.create();
//            al.show();


        }
        else if(MainActivity.inSWrite)
        {


            Login_fragment.logged_in = false;
            Log.i("Before",""+R.id.nav_login);
            MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setTitle("Login");
            Log.i("After",""+R.id.nav_login);
            MainActivity.navigationView.getMenu().findItem(R.id.writeondb).setVisible(false);
            MainActivity.navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            Toast.makeText(this,"Logged Out!",Toast.LENGTH_SHORT).show();
            MainActivity.inSWrite = false;
            while(true)
            {
                if(!getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName().equals("Login"))
                    break;
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().executePendingTransactions();// Executes last fragment on stack


        }

        else {
            super.onBackPressed();
        }
    }


    private void downloadDemo() {
        AsyncTask<String, String, String> demoAsync = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {
                dotsLoaderView.show();
            }

            @Override
            protected String doInBackground(String... params) {

                   while(MainActivity.start);

                return "done";
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("done"))
                    dotsLoaderView.hide();
            }
        };
        demoAsync.execute();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.share:

            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "Hey! This is an Announcements application for RVCE! Download it now using this link! https://drive.google.com/uc?export=download&id=1llIW12D5NrOEGkB-zWeVNb1EEmkO8Sr6";
            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(myIntent,"Share Using"));
            getSupportFragmentManager().executePendingTransactions();
            return(true);


    }

        return(super.onOptionsItemSelected(item));
    }

}