package com.chiru.sareesamrat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WaitForAuthentication extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_authentication);

        tv = (TextView) findViewById(R.id.authenticationtextview);
        tv.setText("Hello"+" "+getIntent().getExtras().getString("Name"));

    }
}
