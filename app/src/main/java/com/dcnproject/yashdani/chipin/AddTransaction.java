package com.dcnproject.yashdani.chipin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddTransaction extends AppCompatActivity {
    private Button mSubmitTransInGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        mSubmitTransInGroup = (Button) findViewById(R.id.submit_trans_in_group);
        mSubmitTransInGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtransingroupIntent = new Intent(AddTransaction.this,GroupView.class);
                startActivity(addtransingroupIntent);
            }
        });
    }
}
