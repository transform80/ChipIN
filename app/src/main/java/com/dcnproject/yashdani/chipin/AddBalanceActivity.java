package com.dcnproject.yashdani.chipin;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class AddBalanceActivity extends AppCompatActivity {
    private Button mAddBalance;
    private EditText mAmount;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);
        mAddBalance = (Button) findViewById(R.id.add_balance_button);
        mAmount = (EditText) findViewById(R.id.add_amoutn_Et);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        mAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float amount = Float.parseFloat((mAmount.getText().toString()));
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("user_balance")) {
                            float user_balance = Float.parseFloat(dataSnapshot.child("user_balance").getValue().toString());
                            user_balance += amount;
                            mDatabase.child("user_balance").setValue(user_balance);
                            startActivity(new Intent(AddBalanceActivity.this,ProfileView.class));
                            finish();
                        }
                        else{
                            mDatabase.child("user_balance").setValue(amount);
                            startActivity(new Intent(getApplicationContext(),ProfileView.class));
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}