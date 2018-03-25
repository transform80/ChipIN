package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionWithNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Spinner dropdownmenu;
    Spinner dropdownmenu2;
    private Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction_with_nav);
        mSubmit=(Button) findViewById(R.id.submit_trans);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent subtransIntent = new Intent(AddTransactionWithNav.this, HomePageWithNav.class);
                startActivity(subtransIntent);
                finish();
            }
        });
        dropdownmenu2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list2 = new ArrayList<>();
        list2.add("Yash");
        list2.add("Aman");
        list2.add("Jainam");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownmenu2.setAdapter(adapter2);
        dropdownmenu2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String itemvalue2 = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(AddTransactionWithNav.this, "Selected: "+itemvalue2,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dropdownmenu = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("Travelling");
        list.add("College");
        list.add("Technovanza");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownmenu.setAdapter(adapter);
        dropdownmenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String itemvalue = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(AddTransactionWithNav.this, "Selected: "+itemvalue,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
            Intent addgroupIntent = new Intent(AddTransactionWithNav.this,AddGroupWithNav.class);
            startActivity(addgroupIntent);
            finish();
        } else if (id == R.id.nav_transaction) {

        } else if (id == R.id.nav_settings) {
            Intent settingIntent = new Intent(AddTransactionWithNav.this,SettingsActivity.class);
            startActivity(settingIntent);

        } else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(AddTransactionWithNav.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(AddTransactionWithNav.this,HomePageWithNav.class);
            startActivity(homeIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
