package com.mobileapp.cis657_semesterproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewHistoryActivity extends AppCompatActivity {

    TextView header;
    LinearLayout layout;
    HashMap<String, String> currentSearch;
    public Map<String, Object> savedSearches;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        header = (TextView) findViewById(R.id.historyHeader);
        layout = (LinearLayout) findViewById(R.id.linearLayout);
        TextView email = (TextView) findViewById(R.id.emailSpace);
        savedSearches = new HashMap<>();
        header.setText("Search History");
        header.setTextSize(35);
        email.setText(user.getEmail());
        email.setTextSize(18);

        getCurrentUserHistory();
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
                startActivity(intent);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(ViewHistoryActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

    public void getCurrentUserHistory() {

        DatabaseReference usersRef = database.getReference("users");

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
                            ArrayList<Object> currentSearches = (ArrayList<Object>) snap.child("savedSearches").getValue();
                            for (int i = 0; i < currentSearches.size(); ++i) {
                                savedSearches.put(String.valueOf(i), currentSearches.get(i));
                        }   }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                displaySearchHistory();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void displaySearchHistory() {

        if(savedSearches != null) {

            for (int i = 0; i < savedSearches.keySet().size(); ++i) {
                 TextView temp = new TextView(this);
                 temp.setClickable(true);
                 temp.setPadding(10,15,10,15);
                 temp.setTextSize(25);
                 temp.setBackgroundResource(R.color.grey);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15,15,15,15);
                temp.setLayoutParams(params);
                 currentSearch = ((HashMap<String, String>) savedSearches.get(String.valueOf(i)));
                 temp.setOnClickListener(v -> {
                    Intent intent = new Intent(ViewHistoryActivity.this, ResultsActivity.class);
                    try {
                        intent.putExtra("sizeSelection", currentSearch.get("size"));
                        intent.putExtra("hairSelection", currentSearch.get("hairType"));
                        intent.putExtra("priceSelection", currentSearch.get("price"));
                        intent.putExtra("spaceSelection", currentSearch.get("idealSpace"));
                        intent.putExtra("timeSelection", currentSearch.get("dailyTimeRequirement"));
                        intent.putExtra("hypoSelection", currentSearch.get("hypoallergenic"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                 });
                try {
                    temp.setText("Search Made On: " + currentSearch.get("date") + " > ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                 layout.addView(temp);
            }
            layout.invalidate();
            layout.requestLayout();
        }
    }
}
