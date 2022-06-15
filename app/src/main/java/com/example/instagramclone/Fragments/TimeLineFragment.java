package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Adapters.PostsAdapter;
import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;


public class TimeLineFragment extends Fragment {

    List<Post> allPosts;
    RecyclerView rvPosts;
    PostsAdapter postsAdapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;


    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_line, container, false);
    }

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts = view.findViewById(R.id.rvPosts);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        allPosts = new LinkedList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts);
        //System.out.println("here2");
        queryPosts();
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi();
            }
        };

        rvPosts.addOnScrollListener(scrollListener);
        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(linearLayoutManager);

    }

    private void loadNextDataFromApi() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereLessThan("createdAt", allPosts.get(allPosts.size() - 1).getDate());
        query.include(Post.USER);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }
                for(Post post: posts) {
                    System.out.println("post refresh44 " + post.getCaption());
                }

                allPosts.addAll(posts);
                postsAdapter.notifyDataSetChanged();
            }

        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.setLimit(2); //load first 20 posts
        query.include(Post.USER);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }

                for(Post post: posts) {
                    System.out.println("post " + post.getCaption());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                postsAdapter.notifyDataSetChanged();
                System.out.println("all posts " + allPosts.size());
                scrollListener.resetState();
            }

        });
        System.out.println("allpost2 " + allPosts.size());
    }

    public void fetchTimelineAsync() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.USER);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }

                postsAdapter.clear();

                postsAdapter.addAll(posts);
                postsAdapter.notifyDataSetChanged();
            }


        });

        swipeContainer.setRefreshing(false);
    }

}