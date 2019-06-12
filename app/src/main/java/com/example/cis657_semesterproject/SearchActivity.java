package com.example.cis657_semesterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.joda.time.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements Serializable {


    Spinner sizeSpinner;
    Spinner hairSpinner;
    Spinner priceSpinner;
    Spinner spaceSpinner;
    Spinner timeSpinner;
    CheckBox hypoBox;
    Button submitSearch;

    public String sizeSelection;
    public String priceSelection;
    public String timeSelection;
    public boolean hypoSelection;
    public String spaceSelection;
    public String hairSelection;

    public JSONObject item;
    public Map<String, Object> currentHistory;
    public static final int RESULTS_SELECTION = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search);

        submitSearch = (Button) findViewById(R.id.submit);
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        hairSpinner = (Spinner) findViewById(R.id.hairSpinner);
        priceSpinner = (Spinner) findViewById(R.id.priceSpinner);
        spaceSpinner = (Spinner) findViewById(R.id.houseSpinner);
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        hypoBox = (CheckBox) findViewById(R.id.hypoCheck);

        submitSearch.setOnClickListener(v-> {

            sizeSelection = sizeSpinner.getSelectedItem().toString();
            hairSelection = hairSpinner.getSelectedItem().toString();
            priceSelection = priceSpinner.getSelectedItem().toString();
            spaceSelection = spaceSpinner.getSelectedItem().toString();
            timeSelection = timeSpinner.getSelectedItem().toString();
            hypoSelection = hypoBox.isChecked();

            updateUserSearchHistory();

            Intent intent = new Intent(SearchActivity.this,ResultsActivity.class);
            intent.putExtra("sizeSelection", sizeSelection);
            intent.putExtra("hairSelection", hairSelection);
            intent.putExtra("priceSelection", priceSelection);
            intent.putExtra("spaceSelection", spaceSelection);
            intent.putExtra("timeSelection", timeSelection);
            intent.putExtra("hypoSelection", hypoSelection);
            startActivity(intent);
        });
    }

    public void updateUserSearchHistory() {

        if(user != null) {
System.out.println("USER SIGN IN ON SEARCH PAGE: " + user);
            DatabaseReference usersRef = database.getReference("users");

            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //String value = dataSnapshot.getValue(String.class);

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {

                        String currentEmail = (String) snap.child("email").getValue();
                        ArrayList<Object> currentItems;
                        //JSONArray currentHistory = (JSONArray) snap.child("savedSearches").getValue();
                        if("".equals(snap.child("savedSearches").getValue())) {
                            currentItems = new ArrayList<>();
                        } else {
                            currentItems = (ArrayList<Object>) snap.child("savedSearches").getValue();
                        }
                        currentHistory = new HashMap<>();
                        System.out.println("CURRENT ITEMS: " + currentItems);
                        try {
                            if (currentEmail.equals(user.getEmail())) {

                                for (int i = 0; i < currentItems.size(); ++i) {
                                    currentHistory.put(String.valueOf(i), currentItems.get(i));
                                }

                                DatabaseReference currentUserRef = snap.getRef();
                                item = new JSONObject();
                                item.put("price", priceSelection);
                                item.put("size", sizeSelection);
                                item.put("hairType", hairSelection);
                                item.put("dailyTimeRequirement", timeSelection);
                                System.out.println("HYPO SELECTION: " + hypoSelection);
                                item.put("hypoallergenic", hypoSelection);
                                item.put("idealSpace", spaceSelection);
                                item.put("date", LocalDate.now());
                                String jsonString = item.toString();
                                Map<String, Object> jsonMap = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {}.getType());
                                currentHistory.put(String.valueOf(currentItems.size()), jsonMap);
                                System.out.println("CURRENT HISTORY: " + currentHistory);
                                System.out.println("CURRENT REF: " +  currentUserRef.child("savedSearches") );
                                currentUserRef.child("savedSearches").setValue(currentHistory);
                                //String key = currentUserRef.child("savedSearches").push().getKey();
                                //currentUserRef.child("savedSearches").child(key).setValue(currentHistory);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(item.getItemId() == R.id.action_account) {
            if(user != null) {
                System.out.println("USER IS: " + user);
                Intent intent = new Intent(SearchActivity.this,
                        AccountActivity.class);
                startActivity(intent);
                return true;
            } else {
                Intent intent = new Intent(SearchActivity.this,
                        SignupActivity.class);
                intent.putExtra("isSignUp", false);
                startActivity(intent);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(SearchActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

}
