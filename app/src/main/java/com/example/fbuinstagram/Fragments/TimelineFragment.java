package com.example.fbuinstagram.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuinstagram.InstaAdapter;
import com.example.fbuinstagram.Post;
import com.example.fbuinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    public static final String TAG="UserTimeline";
    RecyclerView rvPosts;
    InstaAdapter adapter;
    ArrayList<Post> posts;
    int TIMELINE_FRAGMENT=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts= (RecyclerView) view.findViewById(R.id.rv_posts);
        posts= new ArrayList<>();
        adapter= new InstaAdapter(posts, TIMELINE_FRAGMENT);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(adapter);

        queryPosts();

    }
    public void queryPosts(){
        // Toast.makeText(this, "Timeline", Toast.LENGTH_LONG).show();
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
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


}
