package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView userName;
    Button loginButton;
    TextView userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    }


}