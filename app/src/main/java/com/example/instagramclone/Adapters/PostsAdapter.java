package com.example.instagramclone.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.Post;
import com.example.instagramclone.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView ownerOfPost;
        TextView dropDownMenu;
        ImageButton saveButton;
        ImageButton likeButton;
        ImageButton shareButton;
        ImageButton commentButton;
        TextView userNameAndCaption;
        ImageView ownerOfPostPicture;
        TextView viewAllCommmentsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImage);
            ownerOfPost = itemView.findViewById(R.id.ownerOfPost);
            saveButton = itemView.findViewById(R.id.saveButton);
            likeButton = itemView.findViewById(R.id.likeButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            dropDownMenu = itemView.findViewById(R.id.dropDownMenu);
            commentButton = itemView.findViewById(R.id.commentButton);
            viewAllCommmentsButton = itemView.findViewById(R.id.viewComments);
            ownerOfPostPicture = itemView.findViewById(R.id.userProfilePicture);
            userNameAndCaption = itemView.findViewById(R.id.userNameAndCaption);

        }

        public void bind(Post post) {
            //Bitmap takenImage = BitmapFactory.decodeFile(post.getMedia().getAbsolutePath());
            //postImage.setImageBitmap(post.getMedia());
            ownerOfPost.setText(post.getParseUser().getUsername());
            userNameAndCaption.setText(post.getParseUser().getUsername() + " " + post.getCaption());
            viewAllCommmentsButton.setText("view all 40 comments");

            ParseFile mediaFile = post.getMedia();
            loadImage(mediaFile, postImage);
            ParseFile userProfilePic = post.getParseUser().getParseFile("profilePicture");
            loadImage(userProfilePic, ownerOfPostPicture);

        }

        //loads the specified parseFile image into the imageView
        private void loadImage(ParseFile mediaFile, ImageView imageView) {
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
