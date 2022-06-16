package com.example.instagramclone.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.ViewHolder> {

    Context mContext;
    List<Post> mPosts;
    public UserProfileAdapter(Context context, List<Post> posts) {
        this.mPosts = posts;
        this.mContext = context;
    }

    @NonNull
    @Override
    public UserProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileAdapter.ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postedPicture;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postedPicture = itemView.findViewById(R.id.pictureFromDb);
        }

        public void bind(Post post) {
            ParseFile userPicture = post.getMedia();
            loadImage(userPicture, postedPicture);

        }

        //loads the specified parseFile image into the imageView
        public void loadImage(ParseFile mediaFile, ImageView imageView) {
            if(mediaFile != null) {
                mediaFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            // Decode the Byte[] into
                            // Bitmap
                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(
                                            data, 0,
                                            data.length);

                            // Set the Bitmap into the
                            // ImageView
                            imageView.setImageBitmap(bmp);

                        } else {
                            Log.d("test",
                                    "Problem load image the data.");
                        }
                    }
                });
            }
        }
    }
}
