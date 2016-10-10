package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textview;
    Button editprofile,deleteprofile,viewprofile,additems,edititems,deleteitems,viewitems;
    String username, url,jsonResponse,name,email,viewurl;
    int contact;Boolean seller;
    ProgressDialog pDialog;
    String TAG = ContentActivity.class.getSimpleName();
    Boolean deletestatus,readstatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        setupSubviews();
    }

    private void setupSubviews() {
        textview = (TextView) findViewById(R.id.textviewcontent);
        textview.setText("Hello" + " " + getIntent().getExtras().getString("Username"));
        username = getIntent().getExtras().getString("Username");

        editprofile = (Button) findViewById(R.id.buttontoeditprofile);
        deleteprofile = (Button) findViewById(R.id.buttontodeleteprofile);
        viewprofile = (Button) findViewById(R.id.buttontoviewprofile);
        additems = (Button) findViewById(R.id.buttontoaddcontent);
        edititems = (Button) findViewById(R.id.buttontoeditcontent);
        deleteitems = (Button) findViewById(R.id.buttontodeletecontent);
        viewitems = (Button) findViewById(R.id.buttontoviewcontent);


        editprofile.setOnClickListener(this);

        deleteprofile.setOnClickListener(this);

        viewprofile.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttontoeditprofile: {
                Intent intent = new Intent(getApplicationContext(),CrudOperations.class);
                intent.putExtra("Username", username);
                startActivity(intent);
                break;
            }
            case R.id.buttontodeleteprofile:{
                new DeleteProfile().execute();
                break;
            }
            case R.id.buttontoviewprofile:{
                new ViewProfile().execute();
                break;
            }
            case R.id.buttontoaddcontent: {
                Intent intent = new Intent(getApplicationContext(),AddItems.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }
    }
    private class DeleteProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContentActivity.this);
            pDialog.setMessage("Deleting Profile...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler pwordhandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100:8000")
                    .appendPath("users")
                    .appendPath("delete")
                    .appendPath(username);

            url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);
            jsonResponse = jsonStr;

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    deletestatus= jsonObj.getBoolean("success");

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (deletestatus) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        }


    }


    private class ViewProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContentActivity.this);
            pDialog.setMessage("Fetching Profile...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler pwordhandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("users")
                    .appendPath("read")
                    .appendPath(username);

            viewurl = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(viewurl);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray detail = jsonObj.getJSONArray("data");
                    JSONObject c = detail.getJSONObject(0);
                    readstatus= jsonObj.getBoolean("success");
                    name = c.getString("name");
                    email = c.getString("email");
                    contact = c.getInt("contact_no");
                    seller = c.getBoolean("is_seller");


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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (readstatus) {
                Intent intent = new Intent(getApplicationContext(),ViewProfileActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("contact", contact);
                intent.putExtra("seller", seller);
                startActivity(intent);
            }
        }


    }




}






