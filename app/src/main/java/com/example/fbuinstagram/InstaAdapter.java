package com.example.fbuinstagram;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {
    private List<Post> posts;
    private int whichFragment;
    Context context;

    public InstaAdapter(List<Post> posts, int whichFragment) {
        this.posts = posts;
        this.whichFragment = whichFragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvHandle;
        public TextView tvDescription;
        public ImageView ivPhoto;
        public ImageView ivProfilePhoto;
        public ImageView likeButton;
        public ImageView commentButton;
        public ImageView directMessageButton;
        public TextView tv_displayNumLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHandle = (TextView) itemView.findViewById(R.id.tv_handle);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_timeline);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            ivProfilePhoto = (ImageView) itemView.findViewById(R.id.iv_itemProfileImage);
            likeButton = (ImageView) itemView.findViewById(R.id.iv_itemLikeButton);
            commentButton = (ImageView) itemView.findViewById(R.id.iv_itemCommentButton);
            directMessageButton = (ImageView) itemView.findViewById(R.id.iv_itemDMButton);
            tv_displayNumLikes= (TextView) itemView.findViewById(R.id.tv_displayNumLikes);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);

                String imageUrl = post.getImage().getUrl();
                String createTime = post.getCreatedAt().toString();

                ParseFile parseFile = post.getUser().getParseFile("profileImage");
                Intent intent = new Intent(context, UserPostActivity.class);
                intent.putExtra("userName", post.getUser().getUsername());
                intent.putExtra("imageUrl", imageUrl);
                intent.putExtra("description", post.getDescription());
                intent.putExtra("createTime", createTime);

                context.startActivity(intent);

            }
        }
    }

    @NonNull
    @Override

    //inflates the single posts that we're trying to inflate
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View timelineView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(timelineView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Post post = posts.get(i);

        if (whichFragment == 0) {


            viewHolder.tvHandle.setText(post.getUser().getUsername());
            viewHolder.tvDescription.setText(post.getDescription());
            Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivPhoto);

            JSONArray totalNumLikes= post.getIsLiked();
            if(totalNumLikes==null || totalNumLikes.length()==0){
                totalNumLikes= new JSONArray();
                post.setIsLiked(totalNumLikes);
            }

            Integer length= post.getIsLiked().length();
            String len= length.toString();

            viewHolder.tv_displayNumLikes.setText(len);

            //getting the profileImage
            if (post.getUser().getParseFile("profileImage") != null) {
                ParseFile parseFile = post.getUser().getParseFile("profileImage");
                String profileUrl = parseFile.getUrl();
                Glide.with(context).load(profileUrl).apply(RequestOptions.circleCropTransform()).into(viewHolder.ivProfilePhoto);
            } else {
                viewHolder.ivProfilePhoto.setVisibility(View.INVISIBLE);
            }


            viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    try {
                        handleLikeButton(viewHolder, post);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        } else if (whichFragment == 1) {
            profileFragment(viewHolder, post);
        }

    }

    public void profileFragment(ViewHolder viewHolder, Post post) {
        viewHolder.tvHandle.setVisibility(View.GONE);
        viewHolder.tvDescription.setVisibility(View.GONE);
        viewHolder.ivProfilePhoto.setVisibility(View.GONE);
        viewHolder.likeButton.setVisibility(View.GONE);
        viewHolder.commentButton.setVisibility(View.GONE);
        viewHolder.directMessageButton.setVisibility(View.GONE);
        viewHolder.tv_displayNumLikes.setVisibility(View.GONE);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        //float dpWidth = pxWidth / displayMetrics.density;


        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(pxWidth / 3, pxWidth / 3);
        viewHolder.ivPhoto.setLayoutParams(layoutParams);
        Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivPhoto);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleLikeButton(ViewHolder viewHolder, Post post) throws JSONException {

        boolean hasRemovedValue= false;
        JSONArray allLikes= post.getIsLiked();
        if(allLikes==null || allLikes.length()==0){
            allLikes= new JSONArray();
            viewHolder.likeButton.setImageResource(R.drawable.ufi_heart_active);
        }

        for(int i=0; i<allLikes.length(); i++){
            if(allLikes.getString(i).equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId())){
                viewHolder.likeButton.setImageResource(R.drawable.ufi_heart);
                Toast.makeText(context, "Similar object id", Toast.LENGTH_LONG).show();
                allLikes.remove(i);
                post.setIsLiked(allLikes);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.d("YAY", "YAAAAAY");
                        }else{
                            Log.d("NAY", "NAAAAY");
                        }
                    }
                });

                hasRemovedValue=true;
                break;

            }//this checks if that id is already in the array

        }
        if(hasRemovedValue==false){
            viewHolder.likeButton.setImageResource(R.drawable.ufi_heart_active);
            allLikes.put(ParseUser.getCurrentUser().getObjectId());
            post.setIsLiked(allLikes);
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(context, "Post has been saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Post has not been made", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        String len= String.valueOf(allLikes.length());
        viewHolder.tv_displayNumLikes.setText(len);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}
