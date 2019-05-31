package com.example.cis657_semesterproject;

import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cis657_semesterproject.dummy.ResultsContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements ResultsFragment.OnListFragmentInteractionListener{

//android:autoLink="web"

    public static final int ACCOUNT_SELECTION = 1;
    public static final int WEBPAGE_SELECTION = 1;
    public static HashMap<String, ImageView> breedEntries = new HashMap<String, ImageView>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String sizeSelection;
    String priceSelection;
    String hairSelection;
    String spaceSelection;
    String timeSelection;
    boolean hypoSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intentFromSearchPage = getIntent();
        sizeSelection = (intentFromSearchPage.getStringExtra("sizeSelection"));
        priceSelection = (intentFromSearchPage.getStringExtra("priceSelection"));
        hairSelection = (intentFromSearchPage.getStringExtra("hairSelection"));
        spaceSelection = (intentFromSearchPage.getStringExtra("spaceSelection"));
        timeSelection = (intentFromSearchPage.getStringExtra("timeSelection"));
        //hypoSelection = (intentFromSearchPage.getBooleanExtra();
        generateResults();

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

        Button petfinder = (Button) findViewById(R.id.petfinder);

        petfinder.setOnClickListener(v-> {
            Intent intent = new Intent(ResultsActivity.this, WebDisplayActivity.class);
            startActivityForResult(intent,WEBPAGE_SELECTION);
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_account) {
            Intent intent = new Intent(ResultsActivity.this,
                    AccountActivity.class);
            startActivityForResult(intent, ACCOUNT_SELECTION);
            return true;
        }
        return false;
    }

    public void onListFragmentInteraction(ResultsContent.ResultsItem item) {
        Intent intent = new Intent();
        intent.putExtra("item", breedEntries);
        //setResult(MainActivity.HISTORY_RESULT,intent);
        finish();
    }


    private void generateResults() {

        // Write a message to the database
        DatabaseReference breedsRef = database.getReference("breeds");
        //DatabaseReference breedsRef = rootRef.child("breeds");
        //breedsRef.setValue("Hello, World!");

        // Read from the database
        breedsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                ArrayList<String> matchingBreedNames = new ArrayList<String>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String currentBreedName = (String) snap.child("name").getValue();
                    String currentBreedSize = (String) snap.child("size").getValue();
                    String currentBreedHair = (String) snap.child("hairType").getValue();
                    int currentBreedPrice = (int) (long) snap.child("price").getValue();
                    String currentBreedSpace = (String) snap.child("idealSpace").getValue();
                    int currentBreedTime = (int) (long) snap.child("dailyTimeRequirement").getValue();
                    //Boolean currentBreedHypo =
                    System.out.println("IIIIIDDDDDDKKKKKK" + snap.child("size"));
                    int matchingFeatures = 0;
                    try {
                        if (currentBreedSize.equalsIgnoreCase(sizeSelection)) {
                            ++matchingFeatures;
                        }
                        if (currentBreedHair.equalsIgnoreCase(hairSelection)) {
                            ++matchingFeatures;
                        }
                        if (currentBreedSpace.equalsIgnoreCase(spaceSelection)) {
                            ++matchingFeatures;
                        }
                        if(currentBreedPrice <= Integer.valueOf(priceSelection.substring(1))) {
                            ++matchingFeatures;
                         }
                        if(currentBreedTime <= Integer.valueOf(timeSelection.substring(1))) {
                            ++matchingFeatures;
                        }
                        if( == hypoSelection) {
                            ++matchingFeatures;
                        }
                        if(matchingFeatures == 6 && matchingBreedNames.size() < 10) {
                            matchingBreedNames.add(currentBreedName);
                        } else if (matchingFeatures == 5 && matchingBreedNames.size() < 10) {
                            matchingBreedNames.add(currentBreedName);
                        } else if (matchingFeatures == 4 && matchingBreedNames.size() < 10) {
                            matchingBreedNames.add(currentBreedName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                displayBreedData(matchingBreedNames);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void displayBreedData(ArrayList<String> breeds) {

        for(int i = 0; i < breeds.size(); ++i) {
            String imageUri = "https://dog.ceo/api/" + breeds.get(i) + "/images/random";
            SendfeedbackJob job = new SendfeedbackJob();
            job.execute(imageUri, breeds.get(i));
        }
        //TODO call fragment with hashmap data somehow?
    }

    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        private String breedName;

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here

            try {
                String urlToRead = params[0];
                this.breedName = params[1];
                StringBuilder result = new StringBuilder();
                URL url = new URL(urlToRead);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {

            try {

                String jsonText = message;
                System.out.println("Retrieved JSON text: " + jsonText);
                JSONObject imageObject = new JSONObject(jsonText);
                String url = imageObject.getString("message");
                System.out.println("Image object: " + imageObject);
                System.out.println("Image URL extracted: " + url);
                ImageView imageView = new ImageView(getApplicationContext());
                //Picasso.with(ResultsActivity.this).load(url).fit().centerCrop().into(testImage);
                Picasso.with(ResultsActivity.this).load(url).into(imageView);

                TextView textView = new TextView(getApplicationContext());
                textView.setClickable(true);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                String text = "<a href='https://www.google.com/search?q=" + this.breedName + "'> " + this.breedName + " </a>";
                textView.setText(Html.fromHtml(text));

                breedEntries.put(this.breedName, imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

