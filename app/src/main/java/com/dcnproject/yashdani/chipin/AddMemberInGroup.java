package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMemberInGroup extends AppCompatActivity {

    private Button mAddMemberInGroup;
    private String mGroupID;
    private DatabaseReference mDatabaseGrpRef,mDatabaseUsrRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_in_group);
        mGroupID = getIntent().getStringExtra("GUID");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(mGroupID);
        mDatabaseUsrRef = FirebaseDatabase.getInstance().getReference().child("Users");


        mAddMemberInGroup =(Button) findViewById(R.id.addMemberInGroup);
        mAddMemberInGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();


            }
        });
    }

    private void addMember(){
        EditText mEmail = (EditText) findViewById(R.id.addEmailField);
        final String email = mEmail.getText().toString().trim();

        mDatabaseUsrRef.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                showToast(dataSnapshot.child("email").getValue().toString());
                showToast(dataSnapshot.getKey());
                    String user_ID = dataSnapshot.getKey();
                    if(email != null){

                        mDatabaseGrpRef.child("Users").child(user_ID).setValue(email);
                        mDatabaseGrpRef.child("Number of Members").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot_num) {
                                int num = Integer.parseInt(dataSnapshot_num.getValue().toString());
                                num += 1;
                                mDatabaseGrpRef.child("Number of Members").setValue(num);
                                                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    Intent subtransIntent = new Intent(AddMemberInGroup.this, ViewContactInGroup.class);
                    subtransIntent.putExtra("GUID",mGroupID);
                    startActivity(subtransIntent);
                    finish();

                }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        showToast("Error : User may not exist");

        }

    public void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
