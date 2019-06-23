package com.example.fbuinstagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class UserPostActivity extends AppCompatActivity {

    private TextView handle;
    private ImageView userPhoto;
    private TextView description;
    private TextView createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        handle= (TextView) findViewById(R.id.tv_userPostName);
        userPhoto= (ImageView) findViewById(R.id.iv_PostImage);
        description= (TextView) findViewById(R.id.iv_postCaption);
        createTime= (TextView) findViewById(R.id.tv_dateCreated);

       handle.setText(getIntent().getStringExtra("userName"));
        description.setText(getIntent().getStringExtra("description"));
        createTime.setText(getIntent().getStringExtra("createTime"));

        String imageurl= getIntent().getStringExtra("imageUrl");

        Glide.with(this).load(imageurl).into(userPhoto);
    }

}
