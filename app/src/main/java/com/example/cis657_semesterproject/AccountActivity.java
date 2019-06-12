package com.example.cis657_semesterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    TextView acctHeader;
    TextView acctEmail;
    Button viewHistory;
    Button signOut;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        System.out.println("CURRENT USER: " + user);

        acctHeader = (TextView) findViewById(R.id.accountheader);
        acctEmail = (TextView) findViewById(R.id.accountEmail);
        viewHistory = (Button) findViewById(R.id.viewSavedSearches);
        signOut = (Button) findViewById(R.id.signOut);
        acctEmail.setText(user.getEmail());

        viewHistory.setOnClickListener(v-> {
            Intent intent = new Intent(AccountActivity.this, ViewHistoryActivity.class);
            startActivity(intent);
        });

        signOut.setOnClickListener(v-> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            startActivity(intent);
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
                Intent intent = new Intent(AccountActivity.this,
                        AccountActivity.class);
                startActivity(intent);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(AccountActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
        return false;
    }
}
