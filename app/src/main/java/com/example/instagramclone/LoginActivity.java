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

    TextView userName;
    Button loginButton;
    TextView userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //check if someone already signed in
        if(ParseUser.getCurrentUser() != null) goToHome();

        userName = findViewById(R.id.userName);
        loginButton = findViewById(R.id.loginButton);
        userPassword = findViewById(R.id.userPassword);

        //listen to click on button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button clicked");
                String enteredName = userName.getText().toString();
                String enteredPassword = userPassword.getText().toString();

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