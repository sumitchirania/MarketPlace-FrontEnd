package com.chiru.sareesamrat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FullPageActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_page);


        tv1 = (TextView) findViewById(R.id.fullpagetextview1);
        tv2 = (TextView) findViewById(R.id.fullpagetextview2);
        tv3 = (TextView) findViewById(R.id.fullpagetextview3);

        iv = (ImageView) findViewById(R.id.fullpageimageview);

        tv1.setText(getIntent().getExtras().getString("title"));
        tv2.setText(getIntent().getExtras().getString("description"));
        tv3.setText(getIntent().getExtras().getString("price"));

    }
}
