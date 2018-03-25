package com.dcnproject.yashdani.chipin;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabase;
    private String GUID = "-L8T8XbApnZSp5ynd9tw";
    final List<String> memberList = new ArrayList<>();
    int iterator = 0;

    private FloatingActionButton mAddTrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        mList = (RecyclerView) findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groups").child(GUID);

        mAddTrans = (FloatingActionButton) findViewById(R.id.fab);
        mAddTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtransIntent = new Intent(GroupView.this,AddTransaction.class);
                startActivity(addtransIntent);
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
            startActivity(addcontactIntent);
        }
        return super.onOptionsItemSelected(item);
    }

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


    public void showToast(String message)

    {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }

    public void getGroupMembers()
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
    }


}
