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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.cis657_semesterproject.ResultsActivity.ACCOUNT_SELECTION;
import static com.example.cis657_semesterproject.SignupActivity.validated;

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
                Intent intent = new Intent(SearchActivity.this,
                        AccountActivity.class);
                startActivityForResult(intent, ACCOUNT_SELECTION);
                return true;
            } else {
                Intent intent = new Intent(SearchActivity.this,
                        SignupActivity.class);
                startActivityForResult(intent, ACCOUNT_SELECTION);
                return true;
            }
        }
        return false;
    }

}
