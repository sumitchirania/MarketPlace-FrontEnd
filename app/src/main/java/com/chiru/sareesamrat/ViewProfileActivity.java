package com.chiru.sareesamrat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv1, tv2, tv3, tv4, tv5;
    Button editbutton, updatebutton,logoutbutton;
    ProgressDialog pDialog;
    String username,email,contact,is_seller;
    private static String TAG = ViewProfileActivity.class.getSimpleName();
    Boolean updatestatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        SetupSubviews();

        editbutton.setOnClickListener(this);

        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);

        updatebutton.setOnClickListener(this);
        logoutbutton.setOnClickListener(this);

    }
    private void SetupSubviews(){

        tv1 = (TextView) findViewById(R.id.profileview1);
        tv2 = (TextView) findViewById(R.id.profileview2);
        tv3 = (TextView) findViewById(R.id.profileview3);
        tv4 = (TextView) findViewById(R.id.profileview4);
        tv5 = (TextView) findViewById(R.id.profileview5);

        tv1.setText(getIntent().getExtras().getString("name"));
        tv2.setText(getIntent().getExtras().getString("username"));
        tv3.setText(getIntent().getExtras().getString("email"));
        tv4.setText(getIntent().getExtras().getInt("contact")+"");
        String sellerStr = getIntent().getExtras().getBoolean("seller") ? "true" : "false";
        tv5.setText(sellerStr);

        editbutton = (Button) findViewById(R.id.editprofilebutton);
        updatebutton = (Button) findViewById(R.id.updateprofilebutton);
        logoutbutton  = (Button) findViewById(R.id.logoutbutton);

        username = getIntent().getExtras().getString("username");


    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.editprofilebutton: {
                editbutton.setEnabled(false);
                updatebutton.setEnabled(true);
                tv3.setEnabled(true);
                tv4.setEnabled(true);
                tv5.setEnabled(true);
                break;
            }

            case R.id.profileview3: {
                performedit(tv3);
                break;
            }
            case R.id.profileview4: {
                performedit(tv4);
                break;
            }
            case R.id.profileview5: {
                performedit(tv5);
                break;
            }
            case R.id.updateprofilebutton: {

                email = tv3.getText().toString();
                contact = tv4.getText().toString();
                is_seller = tv5.getText().toString();
                new UpdateProfile().execute();
                break;
            }
            case R.id.logoutbutton: {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    private void performedit(TextView tv){

        tv.setCursorVisible(true);
        tv.setFocusableInTouchMode(true);
        tv.setInputType(InputType.TYPE_CLASS_TEXT);
        tv.requestFocus();
    }



    private class UpdateProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewProfileActivity.this);
            pDialog.setMessage("Updating Profile...");
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
                    .appendPath("update")
                    .appendPath(username)
                    .appendQueryParameter("email",email)
                    .appendQueryParameter("contact_no",contact)
                    .appendQueryParameter("is_seller", is_seller);


            String url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    updatestatus= jsonObj.getBoolean("success");

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

            if (updatestatus) {
                Toast.makeText(getApplicationContext(),"Profile Successfully Updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        }


    }


}
