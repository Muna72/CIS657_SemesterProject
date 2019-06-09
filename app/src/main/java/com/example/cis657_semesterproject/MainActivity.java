package com.example.cis657_semesterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.cis657_semesterproject.ResultsActivity.ACCOUNT_SELECTION;
import static com.example.cis657_semesterproject.SignupActivity.validated;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static final int SEARCH_SELECTION = 1;
    public static final int SIGNUP_SELECTION = 1;
    Button startNewSearch;
    Button signIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startNewSearch = (Button) findViewById(R.id.startNewSearch);
        signIn = (Button) findViewById(R.id.signIn);
        signUp = (Button) findViewById(R.id.signUp);

        startNewSearch.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivity.this,SearchActivity.class);
            startActivityForResult(intent,SEARCH_SELECTION);
        });

        signIn.setOnClickListener(v-> {
            if(user == null) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                intent.putExtra("isSignUp", false);
                startActivityForResult(intent, SIGNUP_SELECTION);
            } else {
                Intent intent = new Intent(MainActivity.this,
                        AccountActivity.class);
                startActivityForResult(intent, ACCOUNT_SELECTION);
            }
        });

        signUp.setOnClickListener(v-> {
            if(user == null) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                intent.putExtra("isSignUp", true);
                startActivityForResult(intent, SIGNUP_SELECTION);
            } else {
                Intent intent = new Intent(MainActivity.this,
                        AccountActivity.class);
                startActivityForResult(intent, ACCOUNT_SELECTION);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_account) {
            if(user != null) {
                System.out.println("USER IS: " + user);
                Intent intent = new Intent(MainActivity.this,
                        AccountActivity.class);
                startActivityForResult(intent, ACCOUNT_SELECTION);
                return true;
            } else {
                Intent intent = new Intent(MainActivity.this,
                        SignupActivity.class);
                intent.putExtra("isSignUp", false);
                startActivityForResult(intent, ACCOUNT_SELECTION);
                return true;
            }
        }
        return false;
    }

}

/*public void sendFeedback(View button) {
    // Do click handling here
} */