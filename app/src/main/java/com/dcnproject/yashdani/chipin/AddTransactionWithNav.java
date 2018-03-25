package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddTransactionWithNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Spinner groupDropDown,memberDropDown;
    private Button mSubmit;
    private EditText mAmount,mDesc;
    private String groupNameValue, payeeNameValue;
    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransactions;
    final List<String> payeeList = new ArrayList<>();
    final List<String> payeeUIDList = new ArrayList<>();
    private int payeeIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction_with_nav);
        mDatabaseTransactions = FirebaseDatabase.getInstance().getReference().child("Transactions");
        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        mAmount = (EditText) findViewById(R.id.amountEt);
        mDesc = (EditText) findViewById(R.id.descEt);



        mSubmit=(Button) findViewById(R.id.submit_trans);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTransaction();
            }
        });

        // Populating Drop Downs

        populateDropDown();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            Intent addgroupIntent = new Intent(AddTransactionWithNav.this,AddGroupWithNav.class);
            startActivity(addgroupIntent);
            finish();
        } else if (id == R.id.nav_transaction) {

        } else if (id == R.id.nav_settings) {
            Intent settingIntent = new Intent(AddTransactionWithNav.this,SettingsActivity.class);
            startActivity(settingIntent);

        } else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(AddTransactionWithNav.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(AddTransactionWithNav.this,HomePageWithNav.class);
            startActivity(homeIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void NewTransaction()
    {
        final String amount = mAmount.getText().toString();
        final String reason = mDesc.getText().toString();

        final DatabaseReference newTrans = mDatabaseTransactions.push();
        mDatabaseGrpRef.orderByChild("Name").equalTo(groupNameValue).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey() != null){
                    String group_ID = dataSnapshot.getKey();
                    startSplitting(group_ID,amount);

                    newTrans.child("GUID").setValue(group_ID);
                    newTrans.child("Total Amount").setValue(amount);
                    newTrans.child("Payee").setValue(payeeNameValue);
                    newTrans.child("Payment Description").setValue(reason);
                    SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
                    String format = sdate.format(new Date());
                    newTrans.child("Time").setValue(format);
                    showToast("Transaction Succesfull");

                    Intent subtransIntent = new Intent(AddTransactionWithNav.this, HomePageWithNav.class);
                    startActivity(subtransIntent);
                    finish();

                }
                else {
                    showToast("Transaction entry failed");
                }
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
    }
    public  void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void populatePayeeDropDown(){
        memberDropDown = (Spinner) findViewById(R.id.memberSpinner);
        if (groupNameValue != null){
            mDatabaseGrpRef.orderByChild("Name").equalTo(groupNameValue).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String GUID = dataSnapshot.getKey();
                    mDatabaseGrpRef.child(GUID).child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot payeeSnapshot : dataSnapshot.getChildren()){
                                payeeList.add(payeeSnapshot.getValue().toString());
                                payeeUIDList.add(payeeSnapshot.getKey());
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddTransactionWithNav.this,android.R.layout.simple_spinner_item, payeeList);
                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                memberDropDown.setAdapter(adapter2);
                                memberDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                        payeeNameValue = adapterView.getItemAtPosition(i).toString();
                                        payeeIndex = i;


                                        Toast.makeText(AddTransactionWithNav.this, "Selected: "+payeeNameValue,Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });



                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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


        }



    }

    public void populateDropDown(){
        final List<String> groupNames = new ArrayList<>();

        groupDropDown = (Spinner) findViewById(R.id.groupSpinner);
        mDatabaseGrpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()){
                    groupNames.add(groupSnapshot.child("Name").getValue().toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTransactionWithNav.this,android.R.layout.simple_spinner_item, groupNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    groupDropDown.setAdapter(adapter);
                    groupDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            groupNameValue = adapterView.getItemAtPosition(i).toString();

                            Toast.makeText(AddTransactionWithNav.this, "Selected: "+groupNameValue,Toast.LENGTH_SHORT).show();
                            populatePayeeDropDown();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void startSplitting(String guid, String amount){
        DatabaseReference group = mDatabaseGrpRef.child(guid);
        int num = Integer.parseInt(group.child("Number of Members").toString());
        float total_amount = Float.parseFloat(amount);
        total_amount *= 100;
        int temp_amount = Integer.parseInt(String.valueOf(total_amount));
        total_amount = temp_amount/100;
        final float payable_share = (total_amount/num);
        for(int i = 0; i <num; i++ ){
            if(i != payeeIndex ) {
                final int finalI = i;
                mDatabaseUsrRef.child(payeeUIDList.get(i)).child("Balance").child(payeeUIDList.get(payeeIndex)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        float balance_amount = (float) dataSnapshot.getValue();
                        balance_amount = balance_amount - payable_share;
                        mDatabaseUsrRef.child(payeeUIDList.get(finalI)).child("Balance").child(payeeUIDList.get(payeeIndex)).setValue(balance_amount);
                        balance_amount = -balance_amount;
                        mDatabaseUsrRef.child(payeeUIDList.get(payeeIndex)).child("Balance").child(payeeUIDList.get(finalI)).setValue(balance_amount);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }




        }




    }

}
