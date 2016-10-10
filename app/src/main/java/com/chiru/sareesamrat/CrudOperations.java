package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CrudOperations extends AppCompatActivity {

    EditText updateemail,updatecontact,updateisseller;
    Button finalupdate;
    ProgressDialog pDialog;
    String url,username,email,contact,isseller;
    Boolean statusUpdate, statusDelete;
    String TAG = CrudOperations.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_operations);

        updateemail = (EditText) findViewById(R.id.updateemail);
        updatecontact = (EditText) findViewById(R.id.updatecontactno);
        updateisseller = (EditText) findViewById(R.id.updateasseller);

        finalupdate = (Button) findViewById(R.id.finalupdatebutton);

        finalupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = updateemail.getText().toString();
                contact = updatecontact.getText().toString();
                isseller = updateisseller.getText().toString();
                if(isseller == "yes")
                    isseller="True";
                else
                    isseller="False";


                new UpdateProfile().execute();
                username = getIntent().getExtras().getString("Username");

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("54.214.190.100")
                        .appendPath("users")
                        .appendPath("update")
                        .appendPath(username)
                        .appendPath("")
                        .appendQueryParameter("email", email)
                        .appendQueryParameter("contact_no", contact)
                        .appendQueryParameter("is_seller", isseller);


                url = builder.build().toString();


            }
        });



    }
    private class UpdateProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CrudOperations.this);
            pDialog.setMessage("Updating Profile...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler pwordhandler = new HttpRequestHandler();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    statusUpdate= jsonObj.getBoolean("success");

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

            if (statusUpdate) {
                Toast.makeText(CrudOperations.this, "Profile Successfully Updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }else{
                Toast.makeText(CrudOperations.this, "Check the fields Entered", Toast.LENGTH_LONG).show();
            }
        }


    }
}



