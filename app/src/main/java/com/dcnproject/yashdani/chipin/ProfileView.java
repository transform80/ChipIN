package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Uri mImgU = null;
    private TextView mName, mEmail;
    private de.hdodenhof.circleimageview.CircleImageView mProfileImage;

    List<String> Users_group_list = new ArrayList<>();
    List<String> GUID_List = new ArrayList<>();
    List<String> Groups_User_List = new ArrayList<>();
    List<String> Groups_Transactions = new ArrayList<>();
    List<String> Users_Transactions = new ArrayList<>();
    List<String> Users_uid = new ArrayList<>();

    private boolean doubleBackToExitPressedOnce = false;


    private DatabaseReference mDatabaseUsrRef, mDatabaseGrpRef, mDatabaseTransRef;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private Query mQuery;

    private List<TransactionProfileCards> transactionProfileCardsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionProfileCardsAdapter mTransProfileAdapter;


    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorage;

    private String UID;

    public static final int GALLERY_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mDatabaseUsrRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseGrpRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        mDatabaseTransRef = FirebaseDatabase.getInstance().getReference().child("Transactions");
        mAuth = FirebaseAuth.getInstance();
        //mList.hasFixedSize(true);
        mUser = mAuth.getCurrentUser();
        UID = mUser.getUid();
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mStorage  = FirebaseStorage.getInstance().getReference().child("ProfilePictures");
        mProfileImage = (CircleImageView) findViewById(R.id.profileImageView);

        mTransProfileAdapter = new TransactionProfileCardsAdapter(transactionProfileCardsList);

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

        recyclerView.setAdapter(mTransProfileAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                TransactionProfileCards transactionProfileCards = transactionProfileCardsList.get(position);
                if(Float.parseFloat(transactionProfileCards.getAmount()) < 0){
                    Intent pay = new Intent(ProfileView.this,PopPay.class);
                    pay.putExtra("Key",Users_uid.get(position));
                    pay.putExtra("Name",transactionProfileCards.getPayee());
                    pay.putExtra("Amount",transactionProfileCards.getAmount());
                    pay.putExtra("UID",UID);
                    startActivity(pay);
                }
                Toast.makeText(getApplicationContext(), transactionProfileCards.getPayee() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));




        prepareTransProfileData();


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabaseUsers.keepSynced(true);

                mName = (TextView) findViewById(R.id.profileNameTv);
                mEmail = (TextView) findViewById(R.id.profileEmailTv);

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mName.setText(dataSnapshot.child("name").getValue().toString());
                mEmail.setText(dataSnapshot.child("email").getValue().toString());
                if(dataSnapshot.child("image").getValue().toString() != "default") {
                    Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(mProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(ProfileView.this,"Database Error",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

                /*mProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image*//*");
                        startActivityForResult(galleryIntent,GALLERY_REQUEST);


                    }
                });*/








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
    private void prepareTransData() {

        showToast("Preparing users data1");
        mDatabaseGrpRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showToast("Preparing users data2");

                for (DataSnapshot groups : dataSnapshot.getChildren()){
                    showToast("Preparing USER Data 3");
                    if(groups.child("Users").hasChild(mUser.getUid())) {
                        showToast("Adding groups Trans");
                        Users_group_list.add(groups.getKey().toString());
                        mDatabaseTransRef.orderByChild("GUID").equalTo(groups.getKey().toString()).addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                showToast("Adding groups tarns");
                                for (DataSnapshot trans : dataSnapshot.getChildren()) {
                                    try {
                                        Groups_Transactions.add(trans.getKey().toString());
                                        String name = trans.child("Payee").getValue().toString();
                                        String Reason = trans.child("Payment Description").getValue().toString();
                                        String TotalAmount = trans.child("Total Amount").getValue().toString();
                                        transactionProfileCardsList.add(new TransactionProfileCards(name,Reason,TotalAmount));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mTransProfileAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
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
    private void prepareTransProfileData() {
        mDatabaseUsrRef.child(mAuth.getCurrentUser().getUid()).child("Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot users: dataSnapshot.getChildren()){
                    final String name = users.getKey().toString();
                    final String amount = dataSnapshot.child(users.getKey().toString()).getValue().toString();
                    final String trans;
                    if(Double.parseDouble(amount) > 0){
                        trans = "Take";
                    }
                    else{
                        trans = "Give";
                    }
                    mDatabaseUsrRef.child(name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot names) {
                            String user_name = names.child("name").getValue().toString();
                            Users_uid.add(name);
                            String disp_amount = amount;
                            if(disp_amount.startsWith("-"))
                                disp_amount = "₹" + disp_amount.substring(1);
                            else
                                disp_amount = "₹" + amount;
                            transactionProfileCardsList.add(new TransactionProfileCards(user_name,trans,disp_amount));
                            mTransProfileAdapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

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
            Intent addgroupIntent = new Intent(ProfileView.this,AddGroupWithNav.class);
            startActivity(addgroupIntent);
            finish();
        } else if (id == R.id.nav_transaction) {
            Intent addTransIntent = new Intent(ProfileView.this,AddTransactionWithNav.class);
            startActivity(addTransIntent);

        }  else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(ProfileView.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(ProfileView.this,HomePageWithNav.class);
            startActivity(homeIntent);
            finish();
        }
        else if (id == R.id.nav_add_balance) {
            Intent logoutIntent = new Intent(ProfileView.this,AddBalanceActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            mImgU = data.getData();
            mProfileImage.setImageURI(mImgU);
            StorageReference filepath = mStorage.child(mImgU.getLastPathSegment());
            filepath.putFile(mImgU).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri DownloadUrl = taskSnapshot.getDownloadUrl();
                    mDatabase1.child("image").setValue(DownloadUrl.toString().trim());
                }
            });
        }
    }
    public void showToast(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setImage(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);

    }
}