package com.maxbets.makaveli;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.maxbets.makaveli.fragments.ContactUs;
import com.maxbets.makaveli.fragments.Disclaimer;
import com.maxbets.makaveli.fragments.Home;
import com.maxbets.makaveli.fragments.Rateus;
import com.maxbets.makaveli.fragments.Share;
import com.maxbets.makaveli.fragments.VipOdds;
import com.maxbets.makaveli.fragments.details;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                    startActivity(new Intent(getApplicationContext(), filechooser.class));
                }
                else{
                    dialogBoxGuestLogin();


                }
                //Toast.makeText(MainActivity.this, "only admin can upload test", Toast.LENGTH_SHORT).show();

            }
        });
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mActionBarDrawerToggle.syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mActionBarDrawerToggle.syncState();

            }
        };

        mActionBarDrawerToggle.syncState();
        drawer.addDrawerListener(mActionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_home:
                        Home home = new Home();
                        goToFragment(home);
                        drawer.closeDrawers();
                        break;

                    case R.id.nav_vip:
                        VipOdds vipOdds = new VipOdds();
                        goToFragment(vipOdds);
                        drawer.closeDrawers();
                        break;


                    case R.id.nav_invite:
                        Disclaimer disclaimer=new Disclaimer();
                        goToFragment(disclaimer);
                        drawer.closeDrawers();
                        break;

                  

                    case R.id.nav_contact:
                        ContactUs contactUs=new ContactUs();
                        goToFragment(contactUs);
                        drawer.closeDrawers();
                        break;

                    case R.id.nav_share:
                        Share share = new Share();
                        goToFragment(share);
                        drawer.closeDrawers();
                        break;

                    case R.id.nav_rate:
                        Rateus rateus = new Rateus();
                        goToFragment(rateus);
                        drawer.closeDrawers();
                        break;

                    case R.id.nav_details:
                        details details    = new details();
                        goToFragment(details);
                        drawer.closeDrawers();
                        break;





                    default:

                        break;
                }

                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Home home = new Home();
        goToFragment(home);
    }

    private void goToFragment(Fragment selectedFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, selectedFragment)
                .commit();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:




                break;

            case R.id.action_login:

                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void dialogBoxGuestLogin() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Access Denied")
                .setMessage("You are logged in as a guest user. Access is only allowed to Admin")

                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setNeutralButton("I am admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                startActivity(new Intent(MainActivity.this, login.class));
                finish();
            }
        })
                .setCancelable(false)
                .show();

    }

}
