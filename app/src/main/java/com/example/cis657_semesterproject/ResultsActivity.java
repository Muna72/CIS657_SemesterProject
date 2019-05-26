package com.example.cis657_semesterproject;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

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

public class ResultsActivity extends AppCompatActivity {

//android:autoLink="web"

    public static final int RESULTS_SELECTION = 1;
    public static final int WEBPAGE_SELECTION = 1;
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

        Button petfinder = (Button) findViewById(R.id.petfinder);

        petfinder.setOnClickListener(v-> {
            Intent intent = new Intent(ResultsActivity.this, WebDisplayActivity.class);
            startActivityForResult(intent,WEBPAGE_SELECTION);
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        System.out.println("Hit activity func before if conditional");

        if(resultCode == RESULTS_SELECTION) {
             sizeSelection = (data.getStringExtra("sizeSelection"));
             priceSelection = (data.getStringExtra("priceSelection"));
             hairSelection = (data.getStringExtra("hairSelection"));
             spaceSelection = (data.getStringExtra("spaceSelection"));
             timeSelection = (data.getStringExtra("timeSelection"));
             //hypoSelection = (data.getBooleanExtra("hypoSelection"));
            System.out.println("hit inside activity result function");
            generateResults();
        }
    }

    //TODO will return top ten breeds that best fit form data
    private void generateResults() {

        // Write a message to the database
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Got into firebase function");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                ArrayList<Object> breedsReturned = new ArrayList<Object>();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String num = datas.child("1").getValue().toString();
                    System.out.println("NUM: " + num);


                }
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

