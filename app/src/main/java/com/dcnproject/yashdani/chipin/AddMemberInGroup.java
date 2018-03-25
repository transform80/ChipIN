package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddMemberInGroup extends AppCompatActivity {

    private Button mAddMemberInGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_in_group);
        mAddMemberInGroup =(Button) findViewById(R.id.addMemberInGroup);
        mAddMemberInGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(AddMemberInGroup.this,ViewContactInGroup.class);
                startActivity(addIntent);
            }
        });
    }
}
