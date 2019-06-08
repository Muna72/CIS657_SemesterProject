package com.example.cis657_semesterproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;

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
    public static boolean validated;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final int ACCOUNT_SELECTION = 1;
    //todo to sign out: FirebaseAuth.getInstance().signOut();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        header = (TextView) findViewById(R.id.header);
        warning = (TextView) findViewById(R.id.warning);
        confirmLabel = (TextView) findViewById(R.id.confirmLabel);
        warning.setVisibility(View.INVISIBLE);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        accountAction = (Button) findViewById(R.id.accountAction);

        Intent intent = getIntent();
        signUp = (intent.getExtras().getBoolean("isSignUp"));

        accountAction.setOnClickListener(v-> {

            System.out.println("INSIDE LISTENER");
            boolean validEntry = validateInputs();
            warning.setVisibility(View.INVISIBLE);

            if(validEntry) {
                System.out.println("INSIDE VALID");
                emailContent = (String.valueOf(email.getText()));
                passwordContent = (String.valueOf(password.getText()));

                if (signUp) {
                    System.out.println("INSIDE SIGNUP");

                    mAuth.createUserWithEmailAndPassword(emailContent, passwordContent)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful())
                                            {
                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                    warning.setText("onComplete: weak_password");

                                                    // TODO: take your actions!
                                                }
                                                // if user enters wrong password.
                                                catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                    warning.setText("onComplete: malformed_email");

                                                    // TODO: Take your action
                                                } catch (FirebaseAuthUserCollisionException existEmail) {
                                                    warning.setText("onComplete: exist_email");

                                                    // TODO: Take your action
                                                } catch (Exception e) {
                                                    warning.setText("onComplete: " + e.getMessage());
                                                }
                                                warning.setVisibility(View.VISIBLE);
                                            } else {
                                                Intent i = new Intent(SignupActivity.this, AccountActivity.class);
                                                startActivityForResult(i, ACCOUNT_SELECTION);
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
                                                    warning.setText("onComplete: invalid_email");

                                                    // TODO: take your actions!
                                                }
                                                // if user enters wrong password.
                                                catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                                    warning.setText("onComplete: wrong_password");

                                                    // TODO: Take your action
                                                } catch (Exception e) {
                                                    warning.setText("onComplete: " + e.getMessage());
                                                }
                                                warning.setVisibility(View.VISIBLE);
                                            } else {
                                                Intent i = new Intent(SignupActivity.this, AccountActivity.class);
                                                startActivityForResult(i, ACCOUNT_SELECTION);
                                            }
                                        }
                                    }
                            );
                }
            }
        });

        if(signUp) {
            header.setText("Create Account");
            accountAction.setText("Sign Up");
            confirmPassword.setVisibility(View.VISIBLE);
            confirmLabel.setVisibility(View.VISIBLE);
        } else {
            header.setText("Enter Account Information");
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
}
