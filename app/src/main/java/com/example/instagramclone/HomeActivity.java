package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.instagramclone.Adapters.PostsAdapter;
import com.example.instagramclone.Fragments.ComposeFragment;
import com.example.instagramclone.Fragments.TimeLineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

//    List<Post> allPosts;
//    RecyclerView rvPosts;
//    PostsAdapter postsAdapter;
//    BottomNavigationView bottomNavigationView;
//    private SwipeRefreshLayout swipeContainer;
//    private EndlessRecyclerViewScrollListener scrollListener;
    BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//        allPosts = new LinkedList<>();
//        postsAdapter = new PostsAdapter(this, allPosts);
//        //System.out.println("here2");
//        queryPosts();
//        // Setup refresh listener which triggers new data loading
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                fetchTimelineAsync();
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        // Retain an instance so that you can call `resetState()` for fresh searches
//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                loadNextDataFromApi();
//            }
//        };

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
                        fragment = new ComposeFragment();
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
//        rvPosts.addOnScrollListener(scrollListener);
//        rvPosts.setAdapter(postsAdapter);
//        rvPosts.setLayoutManager(linearLayoutManager);

    }

//    private void loadNextDataFromApi() {
//        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
//        query.whereLessThan("createdAt", allPosts.get(allPosts.size() - 1).getDate());
//        query.include(Post.USER);
//        query.addDescendingOrder("createdAt");
//        query.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if(e != null) {
//                    Log.i("HOME", "something went wrong obtaining posts " + e);
//                }
//                for(Post post: posts) {
//                    System.out.println("post refresh44 " + post.getCaption());
//                }
//
//                allPosts.addAll(posts);
//                postsAdapter.notifyDataSetChanged();
//            }
//
//        });
//    }

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

//    private void queryPosts() {
//        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
//        query.setLimit(2); //load first 20 posts
//        query.include(Post.USER);
//        query.addDescendingOrder("createdAt");
//        query.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if(e != null) {
//                    Log.i("HOME", "something went wrong obtaining posts " + e);
//                }
//
//                for(Post post: posts) {
//                    System.out.println("post " + post.getCaption());
//                }
//
//                // save received posts to list and notify adapter of new data
//                allPosts.addAll(posts);
//                postsAdapter.notifyDataSetChanged();
//                System.out.println("allpost " + allPosts.size());
//                scrollListener.resetState();
//            }
//
//        });
//        System.out.println("allpost2 " + allPosts.size());
//    }
//
//    public void fetchTimelineAsync() {
//        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
//        query.include(Post.USER);
//        query.addDescendingOrder("createdAt");
//        query.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if(e != null) {
//                    Log.i("HOME", "something went wrong obtaining posts " + e);
//                }
//
//                postsAdapter.clear();
//
//                postsAdapter.addAll(posts);
//                postsAdapter.notifyDataSetChanged();
//            }
//
//
//        });
//
//        swipeContainer.setRefreshing(false);
//    }

}