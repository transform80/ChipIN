package com.dcnproject.yashdani.chipin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by chintandani on 08/11/17.
 */

public class PopPay extends Activity {
    private String key,name,amount,UID;
    private DatabaseReference mDatabase;
    private Button yes_delete;
    private Button cancel_delete;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_pay);
        key = getIntent().getStringExtra("Key");
        name = getIntent().getStringExtra("Name");
        amount = getIntent().getStringExtra("Amount");
        UID = getIntent().getStringExtra("UID");
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(UID);

        final float amount_float = Float.parseFloat(amount) * -1;
        TextView mPayeeDetails = (TextView) findViewById(R.id.payee_details);
        mPayeeDetails.setText("Pay " + amount_float + " to " + name);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double width = dm.widthPixels;
        double height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.23));

        cancel_delete = (Button) findViewById(R.id.cancel_button);
        cancel_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopPay.this, ProfileView.class));
                finish();
            }
        });

        yes_delete = (Button) findViewById(R.id.pay_button);
        yes_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double current_balance = Double.parseDouble(dataSnapshot.child("user_balance").getValue().toString());
                        if(current_balance > amount_float){
                            current_balance -= amount_float;
                            mDatabase.child("user_balance").setValue(current_balance);
                            mDatabase.child("Balance").child(key).removeValue();
                        }
                        else{
                            Toast toast = Toast.makeText(PopPay.this,"Insufficient Balance. Add money",Toast.LENGTH_SHORT);
                            toast.show();
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
