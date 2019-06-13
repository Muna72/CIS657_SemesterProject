package com.mobileapp.cis657_semesterproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity implements ResultsFragment.OnListFragmentInteractionListener{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static List<ResultsItem> breedEntries = new ArrayList<ResultsItem>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    LocationManager locationManager;
    LocationListener locationListener;

    Double latitude;
    Double longitude;
    String state;
    String zip;
    String sizeSelection;
    String priceSelection;
    String hairSelection;
    String spaceSelection;
    String timeSelection;
    Button petfinder;
    boolean hypoSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!breedEntries.isEmpty()) {
            breedEntries.clear();
        }

        Intent intentFromSearchPage = getIntent();
        sizeSelection = (intentFromSearchPage.getStringExtra("sizeSelection"));
        priceSelection = (intentFromSearchPage.getStringExtra("priceSelection"));
        hairSelection = (intentFromSearchPage.getStringExtra("hairSelection"));
        spaceSelection = (intentFromSearchPage.getStringExtra("spaceSelection"));
        timeSelection = (intentFromSearchPage.getStringExtra("timeSelection"));
        hypoSelection = (intentFromSearchPage.getExtras().getBoolean("hypoSelection"));
        generateResults();

        boolean flag = displayGpsStatus();
        if (flag) {

            locationListener = new MyLocationListener();

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ResultsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                locationManager.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, 5000, 10, locationListener);
            }

        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }

        petfinder = (Button) findViewById(R.id.petfinder);

        petfinder.setOnClickListener(v-> {
            gotCoordinates();
            Intent intent = new Intent(ResultsActivity.this, WebDisplayActivity.class);
            intent.putExtra("state", state);
            intent.putExtra("zip", zip);
            startActivity(intent);
        });
    }

 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!breedEntries.isEmpty()) {
            breedEntries.clear();
        }
    } */

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        locationManager.requestLocationUpdates(LocationManager
                                .GPS_PROVIDER, 5000, 10, locationListener);
                    }

                } else {
                    petfinder.setActivated(false); //disable petfinder button
                }
                return;
            }

        }
    }

    public void gotCoordinates(){

        latitude = ((MyLocationListener) locationListener).getLatitude();
        longitude = ((MyLocationListener) locationListener).getLongitude();

        if(latitude == null || longitude == null) {
            latitude = 42.964273;
            longitude = -85.680237;
        }

        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(this.latitude, this.longitude, 1);

            String add = "";
            if (addresses.size() > 0)
            {
                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
                    add += addresses.get(0).getAddressLine(i) + "\n";
            }

            if (addresses.size() > 0) {
                //city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                zip = addresses.get(0).getPostalCode();
                //country = addresses.get(0).getCountryName();
            }
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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
                Intent intent = new Intent(ResultsActivity.this,
                        AccountActivity.class);
                startActivity(intent);
                return true;
            } else {
                Intent intent = new Intent(ResultsActivity.this,
                        SignupActivity.class);
                intent.putExtra("isSignUp", false);
                startActivity(intent);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(ResultsActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

    public void onListFragmentInteraction(ResultsItem item) { //TODO what happens if a dog image/name is touched

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
                    boolean currentBreedHypo = (boolean) snap.child("hypoallergenic").getValue();
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
                        if(currentBreedTime <= Integer.valueOf(timeSelection.substring(0,1))) {
                            ++matchingFeatures;
                        }
                        if(currentBreedHypo == hypoSelection) {
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

        String name = "";

        for(int i = 0; i < breeds.size(); ++i) {
            if(breeds.get(i).contains(", ")) {
                name = breeds.get(i).replaceAll(", ", "/");
            } else if(breeds.get(i).contains(" ")) {
                name = name.replaceAll(" ", "");
            }
            else {
                name = breeds.get(i);
            }
            name = name.toLowerCase();
            String imageUri = "https://dog.ceo/api/breed/" + name + "/images/random";
            SendfeedbackJob job = new SendfeedbackJob();
            job.execute(imageUri, breeds.get(i));
        }
    }

    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        private String breedName;

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here

            try {
                String urlToRead = params[0];
                breedName = params[1];
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
                JSONObject imageObject = new JSONObject(jsonText);
                String url = imageObject.getString("message");
                ImageView imageView = new ImageView(getApplicationContext());
                //Picasso.with(ResultsActivity.this).load(url).fit().centerCrop().into(testImage);
                Picasso.with(ResultsActivity.this).load(url).into(imageView);
                System.out.println("URL: " + url);
                //textView.setText(Html.fromHtml(text));
                //textView.setMovementMethod(LinkMovementMethod.getInstance());

                ResultsItem breedItem = new ResultsItem(this.breedName, imageView);
                breedEntries.add(breedItem);

                ResultsFragment.myRecycler.setAdapter(null);
                ResultsFragment.myRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ResultsFragment.myRecycler.setAdapter(ResultsFragment.myAdapter);
                ResultsFragment.myAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ResultsItem {
        public final String breedName;
        public final ImageView breedImage;

        public ResultsItem(String breedName, ImageView breedImage) {
            this.breedImage = breedImage;
            this.breedName = breedName;
        }
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        Double lat;
        Double lng;

        @Override
        public void onLocationChanged(Location loc) {
            lng = loc.getLongitude();
            lat = loc.getLatitude();
        }

        public Double getLatitude() {
            return lat;
        }

        public Double getLongitude() {
            return lng;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public void processGoogleSearch(String text) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(text));
        startActivity(browserIntent);
    }

}

