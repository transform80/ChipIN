package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddGroupWithNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText groupName,numOfMembers;
    private Button confirm;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef;
    private FirebaseDatabase mDatabase;
    private static String finalUID;
    private String tempUID = null;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_with_nav);

        groupName = (EditText) findViewById(R.id.gnameEt);
        numOfMembers = (EditText) findViewById (R.id.numEt);
        confirm = (Button) findViewById(R.id.confirm_btn);

        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup();
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
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProfileView.class));
            }
        });
        final TextView mNameNavHeader = (TextView) headerView.findViewById(R.id.nameNavHeaderTv);
        TextView mEmailNavHeader = (TextView) headerView.findViewById(R.id.emailNavHeaderTv);
        final ImageView mProfileImage = (ImageView) headerView.findViewById(R.id.imageView);
        if(mUser != null){
            mEmailNavHeader.setText(mUser.getEmail());
            mDatabaseUsrRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mNameNavHeader.setText(dataSnapshot.child("name").getValue().toString());
                    if(dataSnapshot.child("image").getValue().toString() != "default") {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(mProfileImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                startActivity(new Intent(getApplicationContext(),HomePageWithNav.class));
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
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

        } else if (id == R.id.nav_transaction) {
            Intent addtransIntent = new Intent(AddGroupWithNav.this,AddTransactionWithNav.class);
            startActivity(addtransIntent);
            finish();
        }  else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent logoutIntent = new Intent(AddGroupWithNav.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(AddGroupWithNav.this,HomePageWithNav.class);
            startActivity(homeIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void addGroup(){
        confirm.setEnabled(false);

        final int num = Integer.parseInt(numOfMembers.getText().toString());
        // Dynamically creates the various text fields for the entry of the participants name
        final LinearLayout ll = (LinearLayout) findViewById(R.id.groupLL);
        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth() / 3;
        final LinearLayout l;

        l = new LinearLayout(AddGroupWithNav.this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.removeAllViewsInLayout();
        EditText ed;
        final List<EditText> allEds = new ArrayList<EditText>();

        for (int i = 0; i < num; i++) { // Generation of the various edit text bars
            ed = new EditText(AddGroupWithNav.this);
            ed.setHint("Username of participant "+ (i+1));
            allEds.add(ed);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(width, WindowManager.LayoutParams.WRAP_CONTENT);
            l.addView(ed,lp);
        }
        Button bt = new Button(AddGroupWithNav.this);
        bt.setText("Confirm Participants");
        final String[] strings = new String[(allEds.size())];

        l.addView(bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i < allEds.size(); i++){
                    strings[i] = allEds.get(i).getText().toString();
                }


                showToast("Button Clicked");
                String gName = groupName.getText().toString(); // Gets the group name from the text field
                String numP = numOfMembers.getText().toString(); // Gets the number of members in the group
                int numM = Integer.parseInt(numP); // Returns int value of the number of participants
                final DatabaseReference newGroup = mDatabaseGrpRef.push(); // Pushes the group into the database with a unique id
                newGroup.child("Name").setValue(gName);
                newGroup.child("Number of Members").setValue(num);
                for(int i = 0; i < numM; i++){
                    final String uName = strings[i];

                    // Receiving the UID of the user from the given name
                    showToast("Getting UID");
                    mDatabaseUsrRef.orderByChild("email").equalTo(uName).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot.getKey() != null) {
                                newGroup.child("Users").child(dataSnapshot.getKey()).setValue(uName);
                                Intent intent = new Intent(AddGroupWithNav.this, HomePageWithNav.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                                showToast("No user entry since UID = " + finalUID);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            finalUID = dataSnapshot.getKey();




                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            finalUID = dataSnapshot.getKey();



                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            finalUID = dataSnapshot.getKey();



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }

            }
        });

        ll.addView(l);


    }

}
