package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

public class ProfileView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton mProfileImage;
    private Uri mImgU = null;
    private TextView mName, mEmail;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private Query mQuery;

    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorage;

    public static final int GALLERY_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProfileImage =(ImageButton) findViewById(R.id.profilePicButton);


                mAuth = FirebaseAuth.getInstance();
                //mList.hasFixedSize(true);
                mUser = mAuth.getCurrentUser();
                mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                mStorage  = FirebaseStorage.getInstance().getReference().child("ProfilePictures");

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

                mProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent,GALLERY_REQUEST);


                    }
                });








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
            Intent addgroupIntent = new Intent(ProfileView.this,AddGroupWithNav.class);
            startActivity(addgroupIntent);
            finish();
        } else if (id == R.id.nav_transaction) {
            Intent addTransIntent = new Intent(ProfileView.this,AddTransactionWithNav.class);
            startActivity(addTransIntent);

        } else if (id == R.id.nav_settings) {
            Intent settingIntent = new Intent(ProfileView.this,SettingsActivity.class);
            startActivity(settingIntent);

        } else if (id == R.id.nav_logout) {
            Intent logoutIntent = new Intent(ProfileView.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(ProfileView.this,HomePageWithNav.class);
            startActivity(homeIntent);
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

}
