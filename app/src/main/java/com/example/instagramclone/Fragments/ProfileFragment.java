package com.example.instagramclone.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Adapters.PostsAdapter;
import com.example.instagramclone.Adapters.UserProfileAdapter;
import com.example.instagramclone.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.HomeActivity;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView mUserName;
    TextView mUserName2;
    ImageView mUserPic;
    Button mLogOutButton;
    Button mSaveNewPicBtn;
    private File mPhotoFile;
    ProgressBar mProgressBar;
    ImageButton mChangePicButton;
    protected List<Post> mAllPosts;
    protected RecyclerView mRvPosts;
    public String mPhotoFileName = "photo.jpg";
    protected UserProfileAdapter mUserProfileAdapter;
    private SwipeRefreshLayout mSwipeContainer;
    public final String APP_TAG = "InstagramClonePFFrag";
    protected EndlessRecyclerViewScrollListener mScrollListener;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1044;

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
        mLogOutButton = view.findViewById(R.id.log0utButton);
        mChangePicButton = view.findViewById(R.id.changePicButton);
        mSaveNewPicBtn = view.findViewById(R.id.saveNewButton);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pbLoading);
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

        mChangePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
                mSaveNewPicBtn.setVisibility(View.VISIBLE);
            }
        });

        mSaveNewPicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhotoFile == null) {
                    Toast.makeText(getContext(), "no image added", Toast.LENGTH_SHORT).show();
                    return;
                }
                upDatePhoto(ParseUser.getCurrentUser(), mPhotoFile);
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                mSaveNewPicBtn.setVisibility(View.GONE);
            }
        });

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

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

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        mPhotoFile = getPhotoFileUri(mPhotoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void upDatePhoto(ParseUser currentUser, File photoFile) {
        currentUser.put("profilePicture", new ParseFile(photoFile));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.i(APP_TAG, "error occured changing picture " + e);
                    Toast.makeText(getContext(), "error!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getContext(), "photo updated!", Toast.LENGTH_SHORT).show();
                // run a background job and once complete
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                //change pic currently on display while it saves in background
                mUserPic.setImageURI(Uri.parse(mPhotoFile.getPath()));
            }
        });
    }
}