package com.chiru.sareesamrat;

import android.app.Dialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private String TAG = SignupActivity.class.getSimpleName();
    EditText FullName, UserName, Password, RePassword, Email, ContactNo;
    Button UserSignup;
    RadioGroup Seller;
    RadioButton IsSeller;
    private ProgressDialog pDialog;
    String fullname, username, password, repassword, email, contact, seller_is;
    Boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FullName = (EditText) findViewById(R.id.edittextFullname);
        UserName = (EditText) findViewById(R.id.edittextusername);
        Password = (EditText) findViewById(R.id.edittextpassword);
        RePassword = (EditText) findViewById(R.id.edittextrepassword);
        Email = (EditText) findViewById(R.id.edittextemail);
        ContactNo = (EditText) findViewById(R.id.edittextcontact);

        Seller = (RadioGroup) findViewById(R.id.radiogroup);
        UserSignup = (Button) findViewById(R.id.usersignupbutton);

        UserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fullname = FullName.getText().toString();
                username = UserName.getText().toString();
                password = Password.getText().toString();
                repassword = RePassword.getText().toString();
                email = Email.getText().toString();
                contact = ContactNo.getText().toString();

                int selectedId = Seller.getCheckedRadioButtonId();
                IsSeller = (RadioButton) findViewById(selectedId);
                seller_is = (String) IsSeller.getText().toString();


                if (fullname.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please Enter a Name", Toast.LENGTH_LONG).show();
                } else if (username.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please Choose a Username", Toast.LENGTH_LONG).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please Choose a Password", Toast.LENGTH_LONG).show();
                } else if (repassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please Enter the same Password again", Toast.LENGTH_LONG).show();
                } else if (isPasswordEqual(password, repassword) == false) {
                    Toast.makeText(SignupActivity.this, "Password doesn't match.", Toast.LENGTH_LONG).show();
                } else {

                    new RegisterNewUser().execute();

                }


            }
        });
    }


    private class RegisterNewUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Registering...");
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
                    .appendPath("create")
                    .appendPath("")
                    .appendQueryParameter("name", fullname)
                    .appendQueryParameter("user_name", username)
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("contact_no", contact)
                    .appendQueryParameter("password", password)
                    .appendQueryParameter("is_seller", seller_is);
            String url = builder.build().toString();


            String jsonStr = pwordhandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getBoolean("success");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();



            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }


            //if(confirmation==null)Toast.makeText(SignupActivity.this, "Kuch GadBad hai", Toast.LENGTH_LONG).show();
            if (success) {
                Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(SignupActivity.this, "Kuch GadBad hai", Toast.LENGTH_LONG).show();
            }


        }

    }
    private boolean isPasswordEqual(String Password1, String Password2) {
        if (Password1.equals(Password2))
            return true;
        else
            return false;
    }
}
