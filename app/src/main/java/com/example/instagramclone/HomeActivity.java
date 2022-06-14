package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.instagramclone.Adapters.PostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.LinkedList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    List<Post> allPosts;
    RecyclerView rvPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        allPosts = new LinkedList<>();
        System.out.println("here2");
        queryPosts();
        System.out.println("here3 " + allPosts.size());

        rvPosts = findViewById(R.id.rvPosts);
//        PostsAdapter postsAdapter = new PostsAdapter(this, allPosts);
//
//        rvPosts.setAdapter(postsAdapter);
//        rvPosts.setLayoutManager(new LinearLayoutManager(this));

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
            System.out.println("here");
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(20); //load first 20 posts
        query.include(Post.USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }
                PostsAdapter postsAdapter = new PostsAdapter(HomeActivity.this, posts);

                rvPosts.setAdapter(postsAdapter);
                rvPosts.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                for(Post post: posts) {
                    System.out.println("post " + post.getCaption());
                }

                allPosts.addAll(posts);
            }

        });
    }
}