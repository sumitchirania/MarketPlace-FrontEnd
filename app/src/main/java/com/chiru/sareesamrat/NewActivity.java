package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class NewActivity extends AppCompatActivity {


    TextView textView1, textView2, textView3, textView4, textView5, textView6;
    ImageView imageView;
    String seller_id, imageUri;
    ProgressDialog pDialog;
    public static String TAG = NewActivity.class.getSimpleName();
    String sellerName, sellerContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        setUpSubViews();

        seller_id = getIntent().getExtras().getString("seller_id");
        imageUri = getIntent().getExtras().getString("imageURL");
        imageUri = "http://54.214.190.100" + imageUri;
        new getSellerDetail().execute();


    }

    private void setUpSubViews() {


        textView1 = (TextView) findViewById(R.id.fullView1);
        textView2 = (TextView) findViewById(R.id.fullView2);
        textView3 = (TextView) findViewById(R.id.fullView3);
        textView4 = (TextView) findViewById(R.id.fullView4);
        textView5 = (TextView) findViewById(R.id.fullView5);
        textView6 = (TextView) findViewById(R.id.fullView6);

        imageView = (ImageView) findViewById(R.id.fullViewImage);


    }

    private class getSellerDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler pwordhandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("users")
                    .appendPath("get")
                    .appendPath(seller_id);
            ;

            String url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    sellerName = jsonObj.getString("name");
                    sellerContact = jsonObj.getString("contact");


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            textView1.setText(getIntent().getExtras().getString("title"));
            textView2.setText("Specs : " + getIntent().getExtras().getString("description"));
            textView3.setText("Rs. " + getIntent().getExtras().getString("price"));
            textView4.setText(getIntent().getExtras().getString("quantity") + " pieces available");
            textView5.setText("Listed by : " + sellerName);
            textView6.setText("Seller Contact : " + sellerContact);


            Picasso.with(NewActivity.this).load(imageUri).placeholder(R.drawable.addimage).fit().into(imageView);
        }


    }
}
