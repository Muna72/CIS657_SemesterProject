package com.example.cis657_semesterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.example.cis657_semesterproject.ResultsActivity.ACCOUNT_SELECTION;

public class ViewHistoryActivity extends AppCompatActivity {

    TextView header;
    JSONArray savedSearches;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static final int MAIN_SELECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        header = (TextView) findViewById(R.id.historyHeader);
        header.setText("Search History for " + user.getEmail());

        getCurrentUserHistory();
        displaySearchHistory();
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
                Intent intent = new Intent(ViewHistoryActivity.this,
                        AccountActivity.class);
                startActivityForResult(intent, ACCOUNT_SELECTION);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(ViewHistoryActivity.this,
                    MainActivity.class);
            startActivityForResult(intent, MAIN_SELECTION);
        }
        return false;
    }

    public void getCurrentUserHistory() {

        DatabaseReference usersRef = database.getReference("users");
        savedSearches = null;

        // Read from the database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String currentUserEmail = (String) snap.child("email").getValue();
                    try {
                        if (user.getEmail().equals(currentUserEmail)) {
                            savedSearches = (JSONArray) snap.child("savedSearches").getValue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void displaySearchHistory() {

        for(int i  = 0; i < savedSearches.length(); ++i) {
            //todo for each object, display it's properties
        }
    }
}
