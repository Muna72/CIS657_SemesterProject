package com.example.cis657_semesterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {


    Spinner sizeSpinner;
    Spinner hairSpinner;
    Spinner priceSpinner;
    Spinner spaceSpinner;
    Spinner timeSpinner;
    CheckBox hypoBox;
    Button submitSearch;

    String sizeSelection;
    String priceSelection;
    String timeSelection;
    boolean hypoSelection;
    String spaceSelection;
    String hairSelection;

    public static final int RESULTS_SELECTION = 1;

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
            hypoSelection = hypoBox.hasSelection();

            Intent intent = new Intent(SearchActivity.this,ResultsActivity.class);
            intent.putExtra("sizeSelection", sizeSelection);
            intent.putExtra("hairSelection", hairSelection);
            intent.putExtra("priceSelection", priceSelection);
            intent.putExtra("spaceSelection", spaceSelection);
            intent.putExtra("timeSelection", timeSelection);
            intent.putExtra("hypoSelection", hypoSelection);
            startActivityForResult(intent,RESULTS_SELECTION);
        });
    }

}
