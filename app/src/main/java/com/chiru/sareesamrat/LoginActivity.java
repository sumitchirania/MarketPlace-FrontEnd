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

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginButton, signUpButton;
    TextView textViewForgotPassword;
    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    String match_request,username,password,message = "Network Problem. Try again later.";
    Boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        usernameField = (EditText) findViewById(R.id.edittextUname);
        passwordField = (EditText) findViewById(R.id.edittextPword);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        signUpButton = (Button) findViewById(R.id.buttonSignup);
        textViewForgotPassword = (TextView) findViewById(R.id.textviewforgotpassword);


        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Fill the required Fields", Toast.LENGTH_LONG).show();

                }
                else {
                    new getPassword().execute();
                }


            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });


    }

    private class getPassword extends AsyncTask<Void, Void, Void> {

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
            HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("login")
                    .appendPath(username)
                    .appendPath(password);
            String url = builder.build().toString();

            String jsonStr = httpRequestHandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    match_request = jsonObj.getString("match");
                    success = jsonObj.getBoolean("success");
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

            if (success && match_request.equals("True")) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);

            } else if (success && match_request.equals("False")) {
                Toast.makeText(LoginActivity.this, "Username and Password doesn't match", Toast.LENGTH_SHORT).show();

            }else if(!success){
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }


    }
}
