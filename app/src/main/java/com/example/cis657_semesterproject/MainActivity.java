package com.example.cis657_semesterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            startActivity(intent);
        });

        signIn.setOnClickListener(v-> {
            if(user == null) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                intent.putExtra("isSignUp", false);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this,
                        AccountActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(v-> {
            if(user == null) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                intent.putExtra("isSignUp", true);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this,
                        AccountActivity.class);
                startActivity(intent);
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
                startActivity(intent);
                return true;
            } else {
                Intent intent = new Intent(MainActivity.this,
                        SignupActivity.class);
                intent.putExtra("isSignUp", false);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

}

/*public void sendFeedback(View button) {
    // Do click handling here
} */