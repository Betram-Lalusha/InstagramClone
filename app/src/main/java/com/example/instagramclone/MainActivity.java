package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView appIcon;
    private final int DELAY_TIME = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appIcon = findViewById(R.id.instaIcon);
        //delay screen so that icon is seen
        new Handler().postDelayed(new Runnable() {
            
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, DELAY_TIME);
    }
}