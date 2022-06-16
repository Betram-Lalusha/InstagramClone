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

    TextView mUserName;
    TextView mUserName2;
    ImageView mUserPic;
    protected List<Post> mAllPosts;
    protected RecyclerView mRvPosts;
    protected UserProfileAdapter mUserProfileAdapter;
    private SwipeRefreshLayout mSwipeContainer;
    protected EndlessRecyclerViewScrollListener mScrollListener;

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
        mUserName = view.findViewById(R.id.nameOfUser);
        mUserName2 = view.findViewById(R.id.nameOfUser2);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRvPosts = view.findViewById(R.id.userProfileRv);

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer2);
        mAllPosts = new LinkedList<>();
        mUserProfileAdapter = new UserProfileAdapter(getContext(), mAllPosts);
        //System.out.println("here2");
        queryPosts();
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync();
            }
        });
        // Configure the refreshing colors
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Retain an instance so that you can call `resetState()` for fresh searches
        mScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi();
            }
        };

        mRvPosts.addOnScrollListener(mScrollListener);
        mRvPosts.setAdapter(mUserProfileAdapter);
        mRvPosts.setLayoutManager(gridLayoutManager);

        ParseFile userPicture = ParseUser.getCurrentUser().getParseFile("profilePicture");
        Glide.with(getContext()).load(userPicture.getUrl()).into(mUserPic);

    }

    private void loadNextDataFromApi() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereLessThan("createdAt", mAllPosts.get(mAllPosts.size() - 1).getDate());
        query.include(Post.USER);
        query.whereEqualTo(Post.USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null) {
                    Log.i("HOME", "something went wrong obtaining posts " + e);
                }
                mAllPosts.addAll(posts);
                mUserProfileAdapter.notifyDataSetChanged();
            }

        });
    }

    protected void queryPosts() {
        mUserName.setText(ParseUser.getCurrentUser().getUsername());
        mUserName2.setText(ParseUser.getCurrentUser().getUsername());
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
                mAllPosts.addAll(posts);
                mUserProfileAdapter.notifyDataSetChanged();
                mScrollListener.resetState();
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

                mUserProfileAdapter.clear();
                mUserProfileAdapter.addAll(posts);
                mUserProfileAdapter.notifyDataSetChanged();
            }


        });

        mSwipeContainer.setRefreshing(false);
    }
}