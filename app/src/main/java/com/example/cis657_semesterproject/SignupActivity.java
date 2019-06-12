package com.example.cis657_semesterproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    TextView header;
    TextView warning;
    TextView confirmLabel;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button accountAction;
    String emailContent;
    String passwordContent;
    boolean signUp;
    public JSONObject item;
    public Map<String, Object> newEntry;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        header = (TextView) findViewById(R.id.header);
        warning = (TextView) findViewById(R.id.warning);
        confirmLabel = (TextView) findViewById(R.id.confirmLabel);
        warning.setVisibility(View.INVISIBLE);
        warning.setTextColor(Color.RED);
        warning.setTextSize(15);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        accountAction = (Button) findViewById(R.id.accountAction);

        Intent intent = getIntent();
        signUp = (intent.getExtras().getBoolean("isSignUp"));

        accountAction.setOnClickListener(v-> {

            warning.setVisibility(View.INVISIBLE);

            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {

                boolean validEntry = validateInputs();

            if(validEntry) {
                    emailContent = (String.valueOf(email.getText()));
                    passwordContent = (String.valueOf(password.getText()));

                    if (signUp) {
                        System.out.println("INSIDE SIGNUP");

                        mAuth.createUserWithEmailAndPassword(emailContent, passwordContent)
                                .addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    try {
                                                        throw task.getException();
                                                    }
                                                    // if user enters wrong email.
                                                    catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                        warning.setText("ERROR: weak_password");

                                                        // TODO: take your actions!
                                                    }
                                                    // if user enters wrong password.
                                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                        warning.setText("ERROR: malformed_email");

                                                        // TODO: Take your action
                                                    } catch (FirebaseAuthUserCollisionException existEmail) {
                                                        warning.setText("ERROR: exist_email");

                                                        // TODO: Take your action
                                                    } catch (Exception e) {
                                                        warning.setText("ERROR: " + e.getMessage());
                                                    }
                                                    warning.setVisibility(View.VISIBLE);
                                                } else {
                                                    //create database entry
                                                    final DatabaseReference usersRef = database.getReference("users");
                                                    // Read from the database
                                                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            // This method is called once with the initial value and again
                                                            // whenever data at this location is updated.
                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                            item = new JSONObject();
                                                            try {
                                                                item.put("email", user.getEmail());
                                                                item.put("savedSearches","");
                                                                newEntry = new HashMap<>();
                                                                String jsonString = item.toString();
                                                                Map<String, Object> jsonMap = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {}.getType());
                                                                DatabaseReference newEntryRef = usersRef.child(String.valueOf(dataSnapshot.getChildrenCount()));
                                                                newEntryRef.setValue(jsonMap);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {
                                                            // Failed to read value
                                                            // Log.w(TAG, "Failed to read value.", error.toException());
                                                        }
                                                    });

                                                    Intent i = new Intent(SignupActivity.this, AccountActivity.class);
                                                    startActivity(i);
                                                }
                                            }
                                        }
                                );
                    } else {
                        System.out.println("INSIDE SIGNIN");
                        mAuth.signInWithEmailAndPassword(emailContent, passwordContent)
                                .addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (!task.isSuccessful()) {
                                                    try {
                                                        throw task.getException();
                                                    }
                                                    // if user enters wrong email.
                                                    catch (FirebaseAuthInvalidUserException invalidEmail) {
                                                        warning.setText("ERROR: invalid_email");

                                                        // TODO: take your actions!
                                                    }
                                                    // if user enters wrong password.
                                                    catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                                        warning.setText("ERROR: wrong_password");

                                                        // TODO: Take your action
                                                    } catch (Exception e) {
                                                        warning.setText("ERROR: " + e.getMessage());
                                                    }
                                                    warning.setVisibility(View.VISIBLE);
                                                } else {
                                                    Intent i = new Intent(SignupActivity.this, AccountActivity.class);
                                                    startActivity(i);
                                                }
                                            }
                                        }
                                );
                    }
            }
            } else {
                warning.setText("All fields must be filled out");
                warning.setVisibility(View.VISIBLE);
            }
        });

        if(signUp) {
            header.setText("Create Account");
            accountAction.setText("Sign Up");
            confirmPassword.setVisibility(View.VISIBLE);
            confirmLabel.setVisibility(View.VISIBLE);
        } else {
            header.setText("Account Credentials");
            accountAction.setText("Sign In");
            confirmPassword.setVisibility(View.INVISIBLE);
            confirmLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            //TODo what to do if they go to this page but they all good already
        }
    }

    public boolean validateInputs() {

        boolean valid = true;

        if(signUp) {
            if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                System.out.println("PASSWORD GOOD");
            } else {
                warning.setText("passwords do not match");
                warning.setVisibility(View.VISIBLE);
                valid = false;
            }
        }
        return valid;
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
                Intent intent = new Intent(SignupActivity.this,
                        AccountActivity.class);
                startActivity(intent);
                return true;
            }
        }
        if(item.getItemId() == R.id.action_home) {
            Intent intent = new Intent(SignupActivity.this,
                    MainActivity.class);
            startActivity(intent);
        }
        return false;
    }
}
