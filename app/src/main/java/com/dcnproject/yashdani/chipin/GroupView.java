package com.dcnproject.yashdani.chipin;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

public class GroupView extends AppCompatActivity {

    private RecyclerView mList;
    private String GUID ;
    List<String> Groups_Transactions = new ArrayList<>();
    int iterator = 0;
    private List<TransactionGroup> transactionGroupList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionGroupAdapter mAdapter;
    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransRef;
    private FloatingActionButton mAddTrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        GUID = getIntent().getStringExtra("GUID").trim();
        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabaseTransRef = FirebaseDatabase.getInstance().getReference().child("Transactions");
        mAddTrans = (FloatingActionButton) findViewById(R.id.fab);
        mAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupTrans = new Intent(GroupView.this,AddTransactionWithNav.class);
                groupTrans.putExtra("GUID",GUID);
                startActivity(groupTrans);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new TransactionGroupAdapter(transactionGroupList);
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

        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TransactionGroup transactionGroup = transactionGroupList.get(position);
                Toast.makeText(getApplicationContext(), transactionGroup.getPayee() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareTransactionData();


    }



    private void prepareTransactionData() {

        mDatabaseTransRef.orderByChild("GUID").equalTo(GUID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot trans : dataSnapshot.getChildren()) {
                    try {
                        Groups_Transactions.add(trans.getKey().toString());
                        String name = trans.child("Payee").getValue().toString();
                        String Reason = trans.child("Payment Description").getValue().toString();
                        String TotalAmount = trans.child("Total Amount").getValue().toString();
                        TotalAmount = "₹" + TotalAmount;
                        transactionGroupList.add(new TransactionGroup(name,Reason,TotalAmount));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.seeContacts) {
            Intent addcontactIntent = new Intent(GroupView.this,ViewContactInGroup.class);
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
