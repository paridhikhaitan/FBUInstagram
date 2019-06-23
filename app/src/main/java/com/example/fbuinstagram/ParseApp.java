package com.example.fbuinstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        //we're actually creating the parse configuration, similar to how we created it
        // from the parse-dashboard

        //this code will actually configure and set up a parse server
        final Parse.Configuration configuration= new Parse.Configuration.Builder(this)
                .applicationId("chote-pandit-heh")
                .clientKey("lmaololwowcute")
                .server("http://paridhikhaitan-fbu-instagram.herokuapp.com/parse")
                .build();

        //this will initialize Parse based on the configuration that we've established
        Parse.initialize(configuration);
    }
}
