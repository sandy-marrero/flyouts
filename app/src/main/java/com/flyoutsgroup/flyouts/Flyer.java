package com.flyoutsgroup.flyouts;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Flyer")
public class Flyer extends ParseObject {

    public static final String KEY_IMAGE = "image";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CREATED = "createdAt";

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setUser(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String Description) {
        put(KEY_DESCRIPTION, Description);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String Title) {
        put(KEY_TITLE, Title);
    }
}