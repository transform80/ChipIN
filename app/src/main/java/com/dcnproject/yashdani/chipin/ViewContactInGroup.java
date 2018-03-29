package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewContactInGroup extends AppCompatActivity {

    private List<ContactCards> contactCardsList = new ArrayList<>();
    List<String> Users_list = new ArrayList<>();
    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransRef;
    private String GUID;
    private RecyclerView recyclerView;
    private ContactCardsAdapter mContactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_in_group);
        GUID = getIntent().getStringExtra("GUID");
        /*Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(mToolbar);
*/
        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabaseTransRef = FirebaseDatabase.getInstance().getReference().child("Transaction");




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mContactAdapter = new ContactCardsAdapter(contactCardsList);

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

        recyclerView.setAdapter(mContactAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ContactCards contactCards = contactCardsList.get(position);
                Toast.makeText(getApplicationContext(), contactCards.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareContactData();


    }


    private void prepareContactData() {
        mDatabaseGrpRef.child(GUID).child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren())
                    Users_list.add(users.getKey());
                for (int i = 0; i < Users_list.size(); i++) {
                    mDatabaseUsrRef.child(Users_list.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("name").getValue().toString();

                            String email = dataSnapshot.child("email").getValue().toString();
                            String ImageUrl = dataSnapshot.child("image").getValue().toString();
                            contactCardsList.add(new ContactCards(name, email, ImageUrl));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                mContactAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });/*
        ContactCards contactCards = new ContactCards("Yash Dani", "yash.dani.98@gmail.com", 9890);
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Aman Framewala", "amanframewala@gmail.com", 989);
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Saumya Gangwar", "saumya.gangwar@gmail.com", 768);
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Saloni Agarwal", "saloni98@gmail.com", 546);
        contactCardsList.add(contactCards);

        *//*contactCards = new ContactCards("The Martian", "Science Fiction & Fantasy", "2015");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Mission: Impossible Rogue Nation", "Action", "2015");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Up", "Animation", "2009");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Star Trek", "Science Fiction", "2009");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("The LEGO ContactCards", "Animation", "2014");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Iron Man", "Action & Adventure", "2008");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Aliens", "Science Fiction", "1986");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Chicken Run", "Animation", "2000");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Back to the Future", "Science Fiction", "1985");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Raiders of the Lost Ark", "Action & Adventure", "1981");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Goldfinger", "Action & Adventure", "1965");
        contactCardsList.add(contactCards);

        contactCards = new ContactCards("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        contactCardsList.add(contactCards);*//*

        // notify adapter about data set changes
        // so that it will render the list with new data
        mContactAdapter.notifyDataSetChanged();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addcontactingroup) {
            Intent addcontactIntent = new Intent(ViewContactInGroup.this,AddMemberInGroup.class);
            startActivity(addcontactIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
