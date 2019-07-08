package com.example.fbuinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

public class UserTimeline extends AppCompatActivity {

    public static final String TAG="UserTimeline";
    RecyclerView rvPosts;
    InstaAdapter adapter;
    ArrayList<Post> posts;

    private BottomNavigationView bottomNavigationView;
    //private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "UserTimeline got called", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_timeline);

        // Lookup the swipe container view
       /* swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });
        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        */


        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);

/*
        rvPosts= (RecyclerView) findViewById(R.id.rv_posts);
        posts= new ArrayList<>();
        adapter= new InstaAdapter(posts);

        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(adapter);
        
        queryPosts();
*/

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.post_action){
                    intent= new Intent(UserTimeline.this, CameraActivity.class);
                    startActivity(intent);
                    Toast.makeText(UserTimeline.this, "Camera", Toast.LENGTH_SHORT);
                }
                if(menuItem.getItemId()==R.id.profile_action){
                    intent= new Intent(UserTimeline.this, UserProfile.class);
                    startActivity(intent);
                    Toast.makeText(UserTimeline.this, "Profile", Toast.LENGTH_SHORT);
                }

                if(menuItem.getItemId()==R.id.logout_action){
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    intent= new Intent(UserTimeline.this, LoginActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    }

 /*   public void queryPosts(){
       // Toast.makeText(this, "Timeline", Toast.LENGTH_LONG).show();
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);

        //postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        //postQuery.filter-> for whichever user
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "error in query");
                    e.printStackTrace();
                }
                //run the loop the other way around

                int len= objects.size()-1;

                //adapter.clear();
                for(int i=len; i>=0; i--){
                    Post post= objects.get(i);
                    posts.add(post);
                    adapter.notifyItemInserted(posts.size()-1);
                }
            }
        });
    }
*/
}
