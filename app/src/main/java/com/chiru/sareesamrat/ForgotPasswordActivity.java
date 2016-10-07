package com.chiru.sareesamrat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {


    EditText checkusername,newpasswordtologin,re_enternewpassword;
    Button applyfornewpassword;
    String provideusername,providenewpassword,reprovidenewpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        checkusername = (EditText) findViewById(R.id.usernamefornewpassword);
        newpasswordtologin = (EditText) findViewById(R.id.newpassword);
        re_enternewpassword = (EditText) findViewById(R.id.re_enternewpassword);

        applyfornewpassword = (Button) findViewById(R.id.buttonfornewpassword);

        provideusername = checkusername.getText().toString();
        providenewpassword = newpasswordtologin.getText().toString();
        reprovidenewpassword = re_enternewpassword.getText().toString();


        applyfornewpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(provideusername.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter your username",Toast.LENGTH_LONG).show();
                }
                else if(!providenewpassword.equals(reprovidenewpassword)){
                    Toast.makeText(ForgotPasswordActivity.this,"Password doesn't match",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this,"Password Updated, Login to Continue",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
            }
        });



    }
}
