package com.chiru.sareesamrat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    EditText FullName,UserName,Password,RePassword;
    Button UserSignup;
    RadioGroup Merchant;
    RadioButton IsMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FullName = (EditText) findViewById(R.id.edittextFullname);
        UserName = (EditText) findViewById(R.id.edittextusername);
        Password = (EditText) findViewById(R.id.edittextpassword);
        RePassword = (EditText) findViewById(R.id.edittextrepassword);

        Merchant = (RadioGroup) findViewById(R.id.radiogroup);
        UserSignup = (Button) findViewById(R.id.usersignupbutton);

        UserSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullname = FullName.getText().toString();
                String username = UserName.getText().toString();
                String password = Password.getText().toString();
                String repassword = RePassword.getText().toString();

                int selectedId = Merchant.getCheckedRadioButtonId();
                IsMerchant = (RadioButton) findViewById(selectedId);

                if(fullname.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Please Enter a Name",Toast.LENGTH_LONG).show();
                }
                else if(isOnlyAlphabet(fullname)==false){
                    Toast.makeText(SignupActivity.this,"Please Enter a Valid Name",Toast.LENGTH_LONG).show();
                }
                else if(username.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Please Choose a Username",Toast.LENGTH_LONG).show();
                }
                else if(password.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Please Choose a Password",Toast.LENGTH_LONG).show();
                }
                else if(repassword.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Please Enter the same Password again",Toast.LENGTH_LONG).show();
                }
                else if(isPasswordEqual(password,repassword)==false){
                    Toast.makeText(SignupActivity.this,"Password doesn't match.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }


            }
        });

    }
    public boolean isOnlyAlphabet(String name){


          return Pattern.compile("[A-Za-z]*[A-Za-z]*").matcher(name).matches();
    }
    private boolean isPasswordEqual(String Password1,String Password2){
        if(Password1.equals(Password2))
            return true;
        else
            return false;
    }



}
