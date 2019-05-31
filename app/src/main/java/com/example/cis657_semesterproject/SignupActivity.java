package com.example.cis657_semesterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    TextView header = (TextView) findViewById(R.id.header);
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button accountAction = (Button) findViewById(R.id.accountAction);
    boolean signUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        header.setText(intent.getStringExtra("sizeSelection"));
        accountAction.setText(intent.getStringExtra("sizeSelection"));
        //priceSelection = (intentFromSearchPage.getStringExtra("priceSelection"));
        //showConfirm = (intentFromSearchPage.getBooleanExtra();

        if(signUp) {
            header.setText("Create Account");
            confirmPassword.setVisibility(View.VISIBLE);
            accountAction.setText("Sign Up");

        } else {
            header.setText("Login");
            accountAction.setText("Sign In");
            confirmPassword.setVisibility(View.INVISIBLE);
        }
    }
}
