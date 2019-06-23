package com.example.fbuinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    public static final String TAG="UserTimeline";
    RecyclerView rvPosts;
    InstaAdapter adapter;
    ArrayList<Post> posts;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "UserProfile got called", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);

        rvPosts= (RecyclerView) findViewById(R.id.rv_profile_posts);
        posts= new ArrayList<>();
        adapter= new InstaAdapter(posts);

        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(adapter);

        queryPosts();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.post_action){
                    intent= new Intent(UserProfile.this, HomeActivity.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.home_action){
                    intent= new Intent(UserProfile.this, UserTimeline.class);
                    startActivity(intent);
                }

                if(menuItem.getItemId()==R.id.logout_action){
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    intent= new Intent(UserProfile.this, MainActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    }

    public void queryPosts(){
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);

        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        //postQuery.filter-> for whichever user
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "error in query");
                    e.printStackTrace();
                }
                //run the loop the other way around
                for(int i=0; i<objects.size(); i++){
                    Post post= objects.get(i);
                    posts.add(post);
                    Log.d(TAG, "Post: "+posts.get(i).getDescription());
                    Log.d(TAG, posts.get(i).getUser().getUsername());
                    adapter.notifyItemInserted(posts.size()-1);
                }
            }
        });
    }

}
