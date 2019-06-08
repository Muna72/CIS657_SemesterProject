package com.example.cis657_semesterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewHistoryActivity extends AppCompatActivity {

    ArrayList<Object> savedSearches;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        getCurrentUserHistory();
        displaySearchHistory();
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
                            savedSearches = (ArrayList<Object>) snap.child("savedSearches").getValue();
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

        for(int i  = 0; i < savedSearches.size(); ++i) {
            //todo for each object, display it's properties
        }
    }
}
