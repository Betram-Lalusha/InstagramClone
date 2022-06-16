package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    TextView mUserName;
    Button mLoginButton;
    TextView mUserPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //check if someone already signed in
        if(ParseUser.getCurrentUser() != null) goToHome();

        mUserName = findViewById(R.id.userName);
        mLoginButton = findViewById(R.id.loginButton);
        mUserPassword = findViewById(R.id.userPassword);

        //listen to click on button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button clicked");
                String enteredName = mUserName.getText().toString();
                String enteredPassword = mUserPassword.getText().toString();

                authenticateUser(enteredName, enteredPassword);
            }
        });
    }

    //check if user exits and if entered password matches user password in db
    private void authenticateUser(String enteredName, String enteredPassword) {
        System.out.println("authenticating user");
        //use parse to authenticate user
        ParseUser.logInInBackground(enteredName, enteredPassword, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //TODO...show user errors tthat occured
                if(e != null) {
                    Log.i("LOGIN", "Login failed because " + e);
                    return;
                }

                Toast.makeText(LoginActivity.this, "sucessfully logged in!", Toast.LENGTH_SHORT).show();
                goToHome();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}