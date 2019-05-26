package com.example.cis657_semesterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SEARCH_SELECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startNewSearch = (Button) findViewById(R.id.startNewSearch);

        startNewSearch.setOnClickListener(v-> {
            //lat1 = Double.valueOf(String.valueOf(latitudeOne.getText()));
            Intent intent = new Intent(MainActivity.this,SearchActivity.class);
            startActivityForResult(intent,SEARCH_SELECTION);
            //distanceDisplay.setText(String.valueOf(calculateDistance(0.0,0.0)));
        });
    }

}

/*public void sendFeedback(View button) {
    // Do click handling here
} */