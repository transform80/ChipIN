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
       /* mList = (RecyclerView) findViewById(R.id.groupList);
        mList.setLayoutManager(new LinearLayoutManager(this));
*/
        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabaseTransRef = FirebaseDatabase.getInstance().getReference().child("Transactions");
/*

        mAddTrans = (FloatingActionButton) findViewById(R.id.fab);
        mAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtransIntent = new Intent(GroupView.this,AddTransaction.class);
                startActivity(addtransIntent);
            }
        });

*/



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new TransactionGroupAdapter(transactionGroupList);

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
        //showToast("Preparing users data1");

        mDatabaseTransRef.orderByChild("GUID").equalTo(GUID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot trans : dataSnapshot.getChildren()) {
                    //showToast("Preparing users data3");
                    //showToast(trans.child("GUID").getValue().toString());
                    Groups_Transactions.add(trans.getKey().toString());
                    String name = trans.child("Payee").getValue().toString();
                    String Reason = trans.child("Payment Description").getValue().toString();
                    String TotalAmount = trans.child("Total Amount").getValue().toString();
                    transactionGroupList.add(new TransactionGroup(name,Reason,TotalAmount));
                    //showToast(String.valueOf(transactionGroupList.size()));




                }
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*TransactionGroup transactionGroup = new TransactionGroup("Jainam", "Action & Adventure", "2000");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Ansh", "Animation, Kids & Family", "2015");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Aman", "Action", "5000");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Yash", "Animation", "4000");
        transactionGroupList.add(transactionGroup);

        *//*transactionGroup = new TransactionGroup("The Martian", "Science Fiction & Fantasy", "2015");
        transactionGroupList.add(transactionGroup);*//*

        *//*transactionGroup = new TransactionGroup("Mission: Impossible Rogue Nation", "Action", "2015");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Up", "Animation", "2009");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Star Trek", "Science Fiction", "2009");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("The LEGO TransactionGroup", "Animation", "2014");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Iron Man", "Action & Adventure", "2008");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Aliens", "Science Fiction", "1986");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Chicken Run", "Animation", "2000");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Back to the Future", "Science Fiction", "1985");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Raiders of the Lost Ark", "Action & Adventure", "1981");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Goldfinger", "Action & Adventure", "1965");
        transactionGroupList.add(transactionGroup);

        transactionGroup = new TransactionGroup("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        transactionGroupList.add(transactionGroup);*//*

        // notify adapter about data set changes
        // so that it will render the list with new data
        mAdapter.notifyDataSetChanged();*/
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
/*

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Cards_GroupView , cardViewHolder> firebaserecycleradapter = new FirebaseRecyclerAdapter<Cards_GroupView, cardViewHolder>(
                Cards_GroupView.class,
                R.layout.list_row_group_view,
                cardViewHolder.class,
                mDatabaseUsrRef
        ) {

            @Override
            protected void populateViewHolder(final cardViewHolder viewHolder, final Cards_GroupView model, int position) {

                getGroupMembers();

                mDatabaseUsrRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showToast("Getting datasnapshot");
                        if(iterator < memberList.size()) {
                            viewHolder.setName(dataSnapshot.child(memberList.get(iterator)).child("name").getValue().toString());
                            viewHolder.setImageUrl(getApplicationContext(),dataSnapshot.child(memberList.get(iterator++)).child("image").getValue().toString());

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
        ImageView mProfileImage;
        TextView mMember;


        public cardViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mProfileImage = (ImageView) mView.findViewById(R.id.profilePic);
            mMember = (TextView) mView.findViewById(R.id.memberNameEt);
        }

        public void setName(String name) {
            TextView   mMember = (TextView) mView.findViewById(R.id.memberNameEt);
            mMember.setText(name);

        }

        public void setImageUrl(final Context ctx, final String image){
            final ImageView post_image = (ImageView) mView.findViewById(R.id.profilePic);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(post_image);
                }
            });


        }
    }

*/

    public void showToast(String message)

    {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }
/*    public void getGroupMembers()
    {
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot payeeSnapshot : dataSnapshot.getChildren()) {
                    memberList.add(payeeSnapshot.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/


}
