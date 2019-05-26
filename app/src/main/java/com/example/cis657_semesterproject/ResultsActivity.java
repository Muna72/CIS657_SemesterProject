package com.example.cis657_semesterproject;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class ResultsActivity extends AppCompatActivity {

//android:autoLink="web"

    public static final int RESULTS_SELECTION = 1;
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

        //Button submitSearch = (Button) findViewById(R.id.submit);
        String imageUri = "https://dog.ceo/api/breed/akita/images/random";
        SendfeedbackJob job = new SendfeedbackJob();
        job.execute(imageUri);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

      /*  submitSearch.setOnClickListener(v-> {


        }); */
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULTS_SELECTION) {
             sizeSelection = (data.getStringExtra("sizeSelection"));
             priceSelection = (data.getStringExtra("priceSelection"));
             hairSelection = (data.getStringExtra("hairSelection"));
             spaceSelection = (data.getStringExtra("spaceSelection"));
             timeSelection = (data.getStringExtra("timeSelection"));
             //hypoSelection = (data.getBooleanExtra("hypoSelection"));
            generateResults();
        }
    }

    private void generateResults() {

        // Write a message to the database
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here

            try {
                String urlToRead = params[0];
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
                System.out.println("GOTHERE");
                String url = imageObject.getString("message");
                System.out.println("Image object: " + imageObject);
                System.out.println("Image URL extracted: " + url);
                ImageView testImage = (ImageView) findViewById(R.id.testImage);
                //Picasso.with(ResultsActivity.this).load(url).fit().centerCrop().into(testImage);
                Picasso.with(ResultsActivity.this).load(url).into(testImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

