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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText UnameField, PwordField;
    Button LoginButton, SignupButton;
    TextView textViewforgotpassword;
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    String match_request,username, password;
    Boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        UnameField = (EditText) findViewById(R.id.edittextUname);
        PwordField = (EditText) findViewById(R.id.edittextPword);
        LoginButton = (Button) findViewById(R.id.buttonLogin);
        SignupButton = (Button) findViewById(R.id.buttonSignup);
        textViewforgotpassword = (TextView) findViewById(R.id.textviewforgotpassword);


        textViewforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentpaswrd = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intentpaswrd);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = UnameField.getText().toString();
                password = PwordField.getText().toString();

                new GetPasswd().execute();


            }
        });
        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSignup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentSignup);
            }
        });


    }

    private class GetPasswd extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler pw = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("login")
                    .appendPath(username)
                    .appendPath(password);
            String url = builder.build().toString();

            String jsonStr = pw.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    match_request = jsonObj.getString("match");
                    success = jsonObj.getBoolean("success");

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
                                "Couldn't get json from server.",
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

            if (success) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            } else if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please Fillin the required Fields", Toast.LENGTH_LONG).show();
            } else if (!success && match_request == "False") {
                Toast.makeText(LoginActivity.this, "Username and Password doesn't match", Toast.LENGTH_LONG).show();
            }else if(!success){
                Toast.makeText(LoginActivity.this, "No Response From Server. Try Again in some Time", Toast.LENGTH_LONG).show();
            }
        }


    }
}
