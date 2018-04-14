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
        recyclerView.setLayoutManager(mLayoutManager);
        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // adding custom divider line with padding 16dp
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
                for (DataSnapshot users : dataSnapshot.getChildren()){
                    mDatabaseUsrRef.child(users.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot users) {
                            String name = users.child("name").getValue().toString();
                            String email = users.child("email").getValue().toString();
                            String ImageUrl = users.child("image").getValue().toString();
                            contactCardsList.add(new ContactCards(name, email, ImageUrl));
                            mContactAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    showToast(String.valueOf(contactCardsList.size()));
                    mContactAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
            addcontactIntent.putExtra("GUID",GUID);
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