package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapters.PostsAdapter;
import com.example.instagramclone.Adapters.UserProfileAdapter;
import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.LinkedList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView userName;
    TextView userName2;
    ImageView mUserPic;
    protected List<Post> allPosts;
    protected RecyclerView rvPosts;
    protected UserProfileAdapter userProfileAdapter;
    private SwipeRefreshLayout swipeContainer;
    protected EndlessRecyclerViewScrollListener scrollListener;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {

        mUserPic = view.findViewById(R.id.profilePicture);
        userName = view.findViewById(R.id.nameOfUser);
        userName2 = view.findViewById(R.id.nameOfUser2);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvPosts = view.findViewById(R.id.userProfileRv);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer2);
        allPosts = new LinkedList<>();
        userProfileAdapter = new UserProfileAdapter(getContext(), allPosts);
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
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi();
            }
        };

        rvPosts.addOnScrollListener(scrollListener);
        rvPosts.setAdapter(userProfileAdapter);
        rvPosts.setLayoutManager(gridLayoutManager);

        ParseFile userPicture = ParseUser.getCurrentUser().getParseFile("profilePicture");
        Glide.with(getContext()).load(userPicture.getUrl()).into(mUserPic);

    }

    private void loadNextDataFromApi() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereLessThan("createdAt", allPosts.get(allPosts.size() - 1).getDate());
        query.include(Post.USER);
        query.whereEqualTo(Post.USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }
                allPosts.addAll(posts);
                userProfileAdapter.notifyDataSetChanged();
            }

        });
    }

    protected void queryPosts() {
        userName.setText(ParseUser.getCurrentUser().getUsername());
        userName2.setText(ParseUser.getCurrentUser().getUsername());
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.USER);
        query.whereEqualTo(Post.USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                userProfileAdapter.notifyDataSetChanged();
                scrollListener.resetState();
            }
        });
    }

    public void fetchTimelineAsync() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.USER);
        query.whereEqualTo(Post.USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }

                userProfileAdapter.clear();
                userProfileAdapter.addAll(posts);
                userProfileAdapter.notifyDataSetChanged();
            }


        });

        swipeContainer.setRefreshing(false);
    }
}