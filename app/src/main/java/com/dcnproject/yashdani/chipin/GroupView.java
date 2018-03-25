package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}
