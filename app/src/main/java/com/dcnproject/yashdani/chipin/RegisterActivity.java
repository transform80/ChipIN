package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.text.TextUtils;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;

    private Button mRegisterBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase mDatabase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress= new ProgressDialog(this);

        mNameField = (EditText) findViewById(R.id.nameField);
        mEmailField=(EditText) findViewById(R.id.emailField);
        mPasswordField=(EditText) findViewById(R.id.passwordField);
        mRegisterBtn=(Button)findViewById(R.id.registerBtn);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister();
            }
        });
    }


    private void startRegister() {

        final String name= mNameField.getText().toString().trim();
        final String email= mEmailField.getText().toString().trim();
        final String password=mPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Signing Up..");
            mProgress.show();



            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                @Override
                public void onComplete(Task<com.google.firebase.auth.AuthResult> task) {

                    if (task.isSuccessful()) {

                        String user_id= mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabaseRef.child(user_id);
                        current_user_db.child("name").setValue(name);
                        current_user_db.child("email").setValue(email);
                        current_user_db.child("image").setValue("default");

                        mProgress.dismiss();

                        Intent mainIntent= new Intent(RegisterActivity.this, LoginActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();
                    }

                    else {
                        Toast.makeText(RegisterActivity.this, "Error registration", Toast.LENGTH_LONG).show();
                    }

                }
            });


        }

    }


}