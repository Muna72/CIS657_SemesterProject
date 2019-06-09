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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.cis657_semesterproject.ResultsActivity.ACCOUNT_SELECTION;

public class AccountActivity extends AppCompatActivity {

    TextView acctHeader;
    Button viewHistory;
    Button signOut;
    public static final int HISTORY_SELECTION = 1;
    public static final int MAIN_SELECTION = 1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        System.out.println("CURRENT USER: " + user);

        acctHeader = (TextView) findViewById(R.id.accountheader);
        viewHistory = (Button) findViewById(R.id.viewSavedSearches);
        signOut = (Button) findViewById(R.id.signOut);
        acctHeader.setText("Account Page\n" +  user.getEmail());

        viewHistory.setOnClickListener(v-> {
            Intent intent = new Intent(AccountActivity.this, ViewHistoryActivity.class);
            startActivityForResult(intent,HISTORY_SELECTION);
        });

        signOut.setOnClickListener(v-> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            startActivityForResult(intent,MAIN_SELECTION);
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
                startActivityForResult(intent, ACCOUNT_SELECTION);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(AccountActivity.this,
                    MainActivity.class);
            startActivityForResult(intent, MAIN_SELECTION);
        }
        return false;
    }
}
