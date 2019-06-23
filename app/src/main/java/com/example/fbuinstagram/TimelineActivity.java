package com.example.fbuinstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG="Timeline Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);
/*
        queryPosts();
    }

    public void queryPosts(){
        Toast.makeText(this, "Querying the posts now", Toast.LENGTH_LONG).show();
        ParseQuery<Post> postQuery= new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "error in query");
                    e.printStackTrace();
                }
                for(int i=0; i<posts.size(); i++){
                    userName.setText(posts.get(i).getUser().getUsername());
                    description.setText(posts.get(i).getDescription());
                    Log.d(TAG, "Post: "+posts.get(i).getDescription());
                    Log.d(TAG, posts.get(i).getUser().getUsername());
                }
            }
        });*/
    }
}
