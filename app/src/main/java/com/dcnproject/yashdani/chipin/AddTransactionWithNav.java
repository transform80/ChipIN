package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransactions;
    private boolean doubleBackToExitPressedOnce = false;

    final List<String> payeeList = new ArrayList<>();
    final List<String> payeeUIDList = new ArrayList<>();
    List<String> Users_group_list = new ArrayList<>();
    String GUID;
    private boolean para_passed = false;

    private int payeeIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("GUID")){
            GUID = getIntent().getStringExtra("GUID");
            para_passed = true;

        }
        setContentView(R.layout.activity_add_transaction_with_nav);
        mDatabaseTransactions = FirebaseDatabase.getInstance().getReference().child("Transactions");
        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        mAmount = (EditText) findViewById(R.id.amountEt);
        mDesc = (EditText) findViewById(R.id.descEt);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


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
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProfileView.class));
            }
        });
        final TextView mNameNavHeader = (TextView) headerView.findViewById(R.id.nameNavHeaderTv);
        TextView mEmailNavHeader = (TextView) headerView.findViewById(R.id.emailNavHeaderTv);
        final ImageView mProfileImage = (ImageView) headerView.findViewById(R.id.imageView);
        if(mUser != null){
            mEmailNavHeader.setText(mUser.getEmail());
            mDatabaseUsrRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mNameNavHeader.setText(dataSnapshot.child("name").getValue().toString());
                    if(dataSnapshot.child("image").getValue().toString() != "default") {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(mProfileImage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                startActivity(new Intent(getApplicationContext(),HomePageWithNav.class));
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
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

        } else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(AddTransactionWithNav.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(AddTransactionWithNav.this,HomePageWithNav.class);
            startActivity(homeIntent);
            finish();
        }
        else if (id == R.id.nav_add_balance) {
            Intent logoutIntent = new Intent(getApplicationContext(),AddBalanceActivity.class);
            startActivity(logoutIntent);
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

        if(!para_passed){
        mDatabaseGrpRef.orderByChild("Name").equalTo(groupNameValue).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                showToast(dataSnapshot.child("Name").getValue().toString());
                showToast(dataSnapshot.getKey());
                if(dataSnapshot.getKey() != null && dataSnapshot.child("Name").getValue().toString() == groupNameValue){
                    String group_ID = dataSnapshot.getKey();

                    startSplitting(group_ID,amount);

                    newTrans.child("GUID").setValue(group_ID);
                    newTrans.child("Total Amount").setValue(amount);
                    newTrans.child("Payee").setValue(payeeNameValue);
                    newTrans.child("Payment Description").setValue(reason);
                    SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
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
        else {
            startSplitting(GUID,amount);

            newTrans.child("GUID").setValue(GUID);
            newTrans.child("Total Amount").setValue(amount);
            newTrans.child("Payee").setValue(payeeNameValue);
            newTrans.child("Payment Description").setValue(reason);
            SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy");
            String format = sdate.format(new Date());
            newTrans.child("Time").setValue(format);
            showToast("Transaction Succesfull");

            Intent subtransIntent = new Intent(AddTransactionWithNav.this, GroupView.class);
            subtransIntent.putExtra("GUID",GUID);
            startActivity(subtransIntent);
            finish();

        }
    }
    public  void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void populatePayeeDropDown(){
        memberDropDown = (Spinner) findViewById(R.id.memberSpinner);
        if(!para_passed) {
            if (groupNameValue != null) {
                mDatabaseGrpRef.orderByChild("Name").equalTo(groupNameValue).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String GUID = dataSnapshot.getKey();
                        mDatabaseGrpRef.child(GUID).child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                payeeList.clear();
                                payeeUIDList.clear();
                                for (DataSnapshot payeeSnapshot : dataSnapshot.getChildren()) {
                                    payeeList.add(payeeSnapshot.getValue().toString());
                                    payeeUIDList.add(payeeSnapshot.getKey());
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddTransactionWithNav.this, android.R.layout.simple_spinner_item, payeeList);
                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    memberDropDown.setAdapter(adapter2);
                                    memberDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                            payeeNameValue = adapterView.getItemAtPosition(i).toString();
                                            payeeIndex = i;
                                            Toast.makeText(AddTransactionWithNav.this, "Selected: " + payeeNameValue, Toast.LENGTH_SHORT).show();
                                            mSubmit = (Button) findViewById(R.id.submit_trans);
                                            mSubmit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    NewTransaction();
                                                }
                                            });


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
        else{

            mDatabaseGrpRef.child(GUID).child("Users").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                payeeList.clear();
                payeeUIDList.clear();
                for (DataSnapshot payeeSnapshot : dataSnapshot.getChildren()) {
                    payeeList.add(payeeSnapshot.getValue().toString());
                    payeeUIDList.add(payeeSnapshot.getKey());
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddTransactionWithNav.this, android.R.layout.simple_spinner_item, payeeList);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    memberDropDown.setAdapter(adapter2);
                    memberDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            payeeNameValue = adapterView.getItemAtPosition(i).toString();
                            payeeIndex = i;
                            Toast.makeText(AddTransactionWithNav.this, "Selected: " + payeeNameValue, Toast.LENGTH_SHORT).show();
                            mSubmit = (Button) findViewById(R.id.submit_trans);
                            mSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    NewTransaction();
                                }
                            });


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

    }

    public void populateDropDown() {
        final List<String> groupNames = new ArrayList<>();

        groupDropDown = (Spinner) findViewById(R.id.groupSpinner);
        if(!para_passed) {
            mDatabaseGrpRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot groups : dataSnapshot.getChildren()) {
                        if (groups.child("Users").hasChild(mUser.getUid())) {
                            Users_group_list.add(groups.getKey().toString());
                            groupNames.add(groups.child("Name").getValue().toString());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTransactionWithNav.this, android.R.layout.simple_spinner_item, groupNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            groupDropDown.setAdapter(adapter);
                            groupDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    groupNameValue = adapterView.getItemAtPosition(i).toString();


                                    Toast.makeText(AddTransactionWithNav.this, "Selected: " + groupNameValue, Toast.LENGTH_SHORT).show();
                                    populatePayeeDropDown();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            mDatabaseGrpRef.child(GUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    groupNames.add(dataSnapshot.child("Name").getValue().toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddTransactionWithNav.this, android.R.layout.simple_spinner_item, groupNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    groupDropDown.setAdapter(adapter);
                    groupDropDown.setEnabled(false);
                    populatePayeeDropDown();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
    public void startSplitting(String guid, String amount){
        DatabaseReference group = mDatabaseGrpRef.child(guid);
        int num = payeeList.size();
        double total_amount = Double.parseDouble(amount);
        total_amount *= 100;
        int temp_amount = (int) total_amount;
        total_amount = temp_amount/100;
        final double payable_share = (total_amount/num);
        for(int i = 0; i <num; i++ ){
            if(i != payeeIndex ) {
                final int finalI = i;
                mDatabaseUsrRef.child(payeeUIDList.get(i)).child("Balance").child(payeeUIDList.get(payeeIndex)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double balance_amount = 0;
                        if(dataSnapshot.getValue() != null)
                            balance_amount = Double.parseDouble(dataSnapshot.getValue().toString());
                        balance_amount = balance_amount - payable_share;
                        mDatabaseUsrRef.child(payeeUIDList.get(finalI)).child("Balance").child(payeeUIDList.get(payeeIndex)).setValue(balance_amount);
                        balance_amount = -balance_amount;
                        if(balance_amount != 0){
                            mDatabaseUsrRef.child(payeeUIDList.get(payeeIndex)).child("Balance").child(payeeUIDList.get(finalI)).setValue(balance_amount);
                            mDatabaseUsrRef.child(payeeUIDList.get(finalI)).child("Balance").child(payeeUIDList.get(payeeIndex)).setValue(balance_amount);

                        }
                        else{
                            mDatabaseUsrRef.child(payeeUIDList.get(payeeIndex)).child("Balance").child(payeeUIDList.get(finalI)).removeValue();
                            mDatabaseUsrRef.child(payeeUIDList.get(finalI)).child("Balance").child(payeeUIDList.get(payeeIndex)).removeValue();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }




        }




    }

}
