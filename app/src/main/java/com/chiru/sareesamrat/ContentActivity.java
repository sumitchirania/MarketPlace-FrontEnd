package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textview;
    Button deleteProfile, viewProfile, addItems, editItems, deleteItems, viewItems;
    String username, url,jsonResponse,name,email, viewUrl, message = "Network Problem. Try again later";
    long contact;
    ProgressDialog pDialog;
    String TAG = ContentActivity.class.getSimpleName();
    Boolean seller, deleteStatus =false, readStatus =false;



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

        deleteProfile = (Button) findViewById(R.id.buttontodeleteprofile);
        viewProfile = (Button) findViewById(R.id.buttontoviewprofile);
        addItems = (Button) findViewById(R.id.buttontoaddcontent);
        editItems = (Button) findViewById(R.id.buttontoeditcontent);
        deleteItems = (Button) findViewById(R.id.buttontodeletecontent);
        viewItems = (Button) findViewById(R.id.buttontoviewcontent);


        deleteProfile.setOnClickListener(this);

        viewProfile.setOnClickListener(this);

        addItems.setOnClickListener(this);

        editItems.setOnClickListener(this);

        deleteItems.setOnClickListener(this);

        viewItems.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttontodeleteprofile: {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ContentActivity.this);
                alertDialog.setTitle("Confirm To Delete");

                alertDialog.setMessage("Confirm Deteting your Account??");

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        new DeleteProfile().execute();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                break;
            }
            case R.id.buttontoviewprofile: {
                new ViewProfile().execute();
                break;
            }
            case R.id.buttontoaddcontent: {
                Intent intent = new Intent(getApplicationContext(),AddItems.class);
                intent.putExtra("Username", username);
                startActivity(intent);
                break;
            }
            case  R.id.buttontodeletecontent: {
                Intent intent = new Intent(getApplicationContext(),DeleteItems.class);
                intent.putExtra("Username", username);
                startActivity(intent);
                break;
            }
            case R.id.buttontoeditcontent: {
                Intent intent = new Intent(getApplicationContext(),EditContent.class);
                intent.putExtra("Username", username);
                startActivity(intent);
                break;
            }
            case R.id.buttontoviewcontent: {
                Intent intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
                break;
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
                    .authority("54.214.190.100")
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
                    deleteStatus = jsonObj.getBoolean("success");
                    message = jsonObj.getString("msg");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (deleteStatus) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(ContentActivity.this, message, Toast.LENGTH_SHORT).show();
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

            viewUrl = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(viewUrl);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray detail = jsonObj.getJSONArray("data");
                    JSONObject c = detail.getJSONObject(0);
                    readStatus = jsonObj.getBoolean("success");
                    name = c.getString("name");
                    email = c.getString("email");
                    contact = c.getLong("contact_no");
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

            if (readStatus) {
                Intent intent = new Intent(getApplicationContext(),ViewProfileActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("usernameString", username);
                intent.putExtra("email", email);
                intent.putExtra("contact", contact);
                intent.putExtra("seller", seller);
                startActivity(intent);
            }else {
                Toast.makeText(ContentActivity.this, "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }
    }
}






