package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ProfileView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton mProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProfileImage =(ImageButton) findViewById(R.id.profilePicButton);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*Add some thing to do over here*/


            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_group) {
            Intent addgroupIntent = new Intent(ProfileView.this,AddGroupWithNav.class);
            startActivity(addgroupIntent);
            finish();
        } else if (id == R.id.nav_transaction) {
            Intent addTransIntent = new Intent(ProfileView.this,AddTransactionWithNav.class);
            startActivity(addTransIntent);
            
        } else if (id == R.id.nav_settings) {
            Intent settingIntent = new Intent(ProfileView.this,SettingsActivity.class);
            startActivity(settingIntent);

        } else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(ProfileView.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(ProfileView.this,HomePageWithNav.class);
            startActivity(homeIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
