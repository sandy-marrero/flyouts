package com.flyoutsgroup.flyouts;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_CAPTION = "caption";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";

    public String getCaption() {
        return getString(KEY_CAPTION);
    }
    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
    public Date getCreatedAt(){
        return getDate(KEY_CREATED_KEY);
    }
}
