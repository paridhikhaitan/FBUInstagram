package com.example.fbuinstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

@ParseClassName("Post")
public class Post extends ParseObject {

    //needs to exactly match the field names
    public static final String KEY_DESCRIPTION="description";
    public static final String KEY_IMAGE="image";
    public static final String KEY_USER="user";
    public static final String KEY_CREATE="createdAt";
    public static final String KEY_ISLIKED= "isLikedByUser";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    //the value will be what the user passes in
    public void setDescription(String userDescription){
        put(KEY_DESCRIPTION, userDescription);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFileImage){
        put(KEY_IMAGE, parseFileImage);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }

    public JSONArray getIsLiked(){
        return getJSONArray("isLikedByUser");
    }

    public void setIsLiked(JSONArray jsonArray){
        put(KEY_ISLIKED, jsonArray);
    }

    public String getCreate(){
        return getString(KEY_CREATE);
    }

    public void setCreate(String createTime){
        put(KEY_CREATE, createTime);
    }

}
