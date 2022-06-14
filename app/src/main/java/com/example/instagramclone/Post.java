package com.example.instagramclone;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String USER = "user";
    public static final String MEDIA = "media";
    public static final String CAPTION = "caption";

    public String getCaption() {
        return getString(CAPTION);
    }

    public void setCaption(String caption) {
        put(CAPTION, caption);
    }
    public ParseFile getMedia() {
        return getParseFile(MEDIA);
    }

    public void setMedia(ParseFile media) {
        put(MEDIA, media);
    }
    public ParseUser getParseUser() {
        return getParseUser(USER);
    }

    public void setUser(ParseUser user) {
        put(USER, user);
    }
}
