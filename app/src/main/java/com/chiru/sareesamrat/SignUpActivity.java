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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private String TAG = SignUpActivity.class.getSimpleName();
    EditText FullName, UserName, Password, RePassword, Email, ContactNo;
    Button userSignUp;
    RadioGroup seller;
    RadioButton isSeller;
    private ProgressDialog pDialog;
    String name, username, password, rePassword, email, contact, seller_is, message = "Network Problem. Try again later.";
    Boolean success = false;

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

        seller = (RadioGroup) findViewById(R.id.radiogroup);
        userSignUp = (Button) findViewById(R.id.usersignupbutton);

        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = FullName.getText().toString();
                username = UserName.getText().toString();
                password = Password.getText().toString();
                rePassword = RePassword.getText().toString();
                email = Email.getText().toString();
                contact = ContactNo.getText().toString();

                int selectedId = seller.getCheckedRadioButtonId();
                isSeller = (RadioButton) findViewById(selectedId);
                seller_is = isSeller.getText().toString();
                if (seller_is.equals("Yes")) {
                    seller_is = "True";
                } else {
                    seller_is = "False";
                }


                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Enter a Name", Toast.LENGTH_LONG).show();

                } else if (username.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Username required", Toast.LENGTH_LONG).show();

                } else if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Email required", Toast.LENGTH_LONG).show();

                }else if (!isValidEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter a valid Email", Toast.LENGTH_LONG).show();

                }else if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Choose a Password", Toast.LENGTH_LONG).show();

                } else if (rePassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please Enter the same Password again", Toast.LENGTH_LONG).show();

                } else if (!isPasswordEqual(password, rePassword)) {
                    Toast.makeText(SignUpActivity.this, "Password doesn't match.", Toast.LENGTH_LONG).show();

                } else if (seller_is.equals("True") && contact.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Contact required if you are a Seller", Toast.LENGTH_LONG).show();

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
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Registering...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpRequestHandler httpRequestHandler = new HttpRequestHandler();

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("54.214.190.100")
                    .appendPath("users")
                    .appendPath("create")
                    .appendPath("")
                    .appendQueryParameter("name", name)
                    .appendQueryParameter("user_name", username)
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("contact_no", contact)
                    .appendQueryParameter("password", password)
                    .appendQueryParameter("is_seller", seller_is);
            String url = builder.build().toString();


            String jsonStr = httpRequestHandler.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (success) {
                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
            }


        }

    }

    private boolean isPasswordEqual(String Password1, String Password2) {
        return Password1.equals(Password2);

    }

    private boolean isValidEmail( String email){

        final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}