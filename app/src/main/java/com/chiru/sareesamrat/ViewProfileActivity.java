package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewProfileActivity extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        SetupSubviews();


    }
    private void SetupSubviews(){

        tv1 = (TextView) findViewById(R.id.profileview1);
        tv2 = (TextView) findViewById(R.id.profileview2);
        tv3 = (TextView) findViewById(R.id.profileview3);
        tv4 = (TextView) findViewById(R.id.profileview4);
        tv5 = (TextView) findViewById(R.id.profileview5);

        tv1.setText("Name :" +  " " + getIntent().getExtras().getString("name"));
        tv2.setText("Username :" + " " + getIntent().getExtras().getString("username"));
        tv3.setText("Email :" + " " + getIntent().getExtras().getString("email"));
        tv4.setText("Contact: " + " " + getIntent().getExtras().getInt("contact"));
        tv5.setText("Seller :" + " " + getIntent().getExtras().getBoolean("seller"));


    }

}
