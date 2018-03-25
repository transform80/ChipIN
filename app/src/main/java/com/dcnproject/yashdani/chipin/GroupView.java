package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class GroupView extends AppCompatActivity {

    private FloatingActionButton mAddTrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
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
}
