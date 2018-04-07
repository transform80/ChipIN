package com.dcnproject.yashdani.chipin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomePageWithNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private SharedPreferences pref;
    private Context _context;
    private SharedPreferences.Editor editor;
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private boolean doubleBackToExitPressedOnce = false;


    private TextView mWelcome;
    List<String> Users_group_list = new ArrayList<>();
    List<String> GUID_List = new ArrayList<>();

    private List<com.dcnproject.yashdani.chipin.Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;


    /*private RecyclerView mList;*/
    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransRef;
    List<String> groupList = new ArrayList<>();
    int iterator = 0;
    private String current_user;


    // Shared pref mode
    int PRIVATE_MODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_with_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                showToast("Chechking if user exists");
                Log.d("Login Error Report", "onAuthStateChanged: Checking if user exists");
                if(mAuth.getCurrentUser() == null){
                    startActivity(new Intent(HomePageWithNav.this, LoginActivity.class));
                    Log.d("Login Error Report", "onAuthStateChanged: User Does Not Exist");
                    showToast("User does not exist");
                }
            }
        });



        mUser = mAuth.getCurrentUser();
        if(mUser != null)
            current_user = mUser.getEmail();

        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabaseTransRef = FirebaseDatabase.getInstance().getReference().child("Transaction");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.HORIZONTAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent group = new Intent(HomePageWithNav.this,GroupView.class);

                group.putExtra("GUID",Users_group_list.get(position));
                startActivity(group);

                com.dcnproject.yashdani.chipin.Movie movie = movieList.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

       /* mList = (RecyclerView) findViewById(R.id.groupList);
        mList.setLayoutManager(new LinearLayoutManager(this));*/

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
                startActivity(new Intent(HomePageWithNav.this,ProfileView.class));
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
        prepareMovieData();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
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
            Intent addgroupIntent = new Intent(HomePageWithNav.this,AddGroupWithNav.class);
            startActivity(addgroupIntent);
            finish();
        } else if (id == R.id.nav_transaction) {
            Intent addtransIntent = new Intent(HomePageWithNav.this, AddTransactionWithNav.class);
            startActivity(addtransIntent);
            finish();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent logoutIntent = new Intent(HomePageWithNav.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {

        }
        else if (id == R.id.nav_add_balance) {
            Intent logoutIntent = new Intent(getApplicationContext(),AddBalanceActivity.class);
            startActivity(logoutIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void createLoginSession(String name, String email) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {

            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn() {

        return pref.getBoolean(IS_LOGIN, false);
    }
    private void checkUserExist() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(user_id)) {

                    Intent mainIntent = new Intent(HomePageWithNav.this, LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                    //Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
    public void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }






    private void prepareMovieData() {
        showToast("Preparing users data1");
        mDatabaseGrpRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showToast("Preparing users data2");

                for (DataSnapshot groups : dataSnapshot.getChildren()){
                    if(groups.child("Users").hasChild(mUser.getUid())) {
                        Users_group_list.add(groups.getKey().toString());
                        movieList.add(new com.dcnproject.yashdani.chipin.Movie(groups.child("Name").getValue().toString()));

                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        // notify adapter about data set changes
        // so that it will render the list with new data
    }

}

/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CardsViewGroups , cardViewHolder> firebaserecycleradapter = new FirebaseRecyclerAdapter<CardsViewGroups, cardViewHolder>(
                CardsViewGroups.class,
                R.layout.list_row_view_group,
                cardViewHolder.class,
                mDatabaseGrpRef
        ) {

            @Override
            protected void populateViewHolder(final cardViewHolder viewHolder, final CardsViewGroups model, int position) {

                getGroups();
                viewHolder.setName(model.getName());

                mDatabaseGrpRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showToast("Getting datasnapshot " + groupList.size());
                        if(iterator < groupList.size()) {
                            viewHolder.setName(dataSnapshot.child(groupList.get(iterator++)).child("Name").getValue().toString());


                        }

                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });







            }
        };
        mList.setAdapter(firebaserecycleradapter);



    }

    public static class cardViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mGroup;


        public cardViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mGroup = (TextView) mView.findViewById(R.id.groupNameEt);
        }

        public void setName(String name) {
            TextView   mMember = (TextView) mView.findViewById(R.id.groupNameEt);
            mMember.setText(name);

        }


    }



    public void getGroups(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot groups: dataSnapshot.getChildren()){
                    for(DataSnapshot users: groups.getChildren()){
                        for(DataSnapshot emailid: users.getChildren()) {
                            if (emailid.getValue() == current_user) {
                                showToast("Group Found");
                                groupList.add(dataSnapshot.getKey());
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

