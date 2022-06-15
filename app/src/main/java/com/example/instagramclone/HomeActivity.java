package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.instagramclone.Fragments.ComposeFragment;
import com.example.instagramclone.Fragments.ProfileFragment;
import com.example.instagramclone.Fragments.TimeLineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //hide action bar
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.homeButton:
                        fragment = new TimeLineFragment();
                        Toast.makeText(HomeActivity.this, "home clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.postButton:
                        fragment = new ComposeFragment();
                        Toast.makeText(HomeActivity.this, "post clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.userProfile:
                        fragment = new ProfileFragment();
                        Toast.makeText(HomeActivity.this, "profile clicked", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        fragment = new TimeLineFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.homeButton);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addPostButton) {
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.logoutButton) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}