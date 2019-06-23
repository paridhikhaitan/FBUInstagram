package com.example.fbuinstagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {
    private List<Post> posts;
    Context context;

    public InstaAdapter(List<Post> posts){
        this.posts=posts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvHandle;
        public TextView tvDescription;
        public ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHandle= (TextView) itemView.findViewById(R.id.tv_handle);
            ivPhoto= (ImageView) itemView.findViewById(R.id.iv_timeline);
            tvDescription=(TextView) itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position= getAdapterPosition();
            if(position!= RecyclerView.NO_POSITION){
                Post post= posts.get(position);

                String imageUrl= post.getImage().getUrl();
                String createTime= post.getCreatedAt().toString();

                Intent intent= new Intent(context, UserPostActivity.class);
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
        context= viewGroup.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View timelineView= inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder= new ViewHolder(timelineView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post= posts.get(i);

        viewHolder.tvHandle.setText(post.getUser().getUsername());
        viewHolder.tvDescription.setText(post.getDescription());
        Glide.with(context).load(post.getImage().getUrl()).into(viewHolder.ivPhoto);

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
