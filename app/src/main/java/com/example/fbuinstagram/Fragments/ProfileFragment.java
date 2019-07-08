package com.example.fbuinstagram.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fbuinstagram.InstaAdapter;
import com.example.fbuinstagram.Post;
import com.example.fbuinstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    public static final String TAG = "UserTimeline";
    RecyclerView rvPosts;
    InstaAdapter adapter;
    ArrayList<Post> posts;
    int PROFILE_FRAGMENT = 1;

    ImageView profileImage;
    TextView userName;
    TextView userHandle;
    ParseUser parseUser;

    //for allowing the user to take a photo
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = (RecyclerView) view.findViewById(R.id.rv_profile_posts);
        posts = new ArrayList<>();
        //you pass in the fragment number to check whether you came from timeline or progile
        adapter = new InstaAdapter(posts, PROFILE_FRAGMENT);

        parseUser = ParseUser.getCurrentUser();

        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvPosts.setAdapter(adapter);

        //creating instances of view components
        profileImage = view.findViewById(R.id.iv_profileItem_photo);
        userName = view.findViewById(R.id.tv_profile_username);
        userHandle = view.findViewById(R.id.tv_profile_handle);

        userName.setText(parseUser.getUsername());
        String userHandleText = "@" + parseUser.getString("handle");
        userHandle.setText(userHandleText);

        //TODO: ASK WHY IS THE PHOTO NOT GETTING RESET :((
        if (parseUser.getParseFile("profileImage") != null) {
            String url = parseUser.getParseFile("profileImage").getUrl();
            Glide.with(getContext()).load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImage);
            parseUser.saveInBackground();
        }else{
            profileImage.setImageResource(R.drawable.instagram_new_post_outline_24);
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchCamera();

/*                //error checking to see if the user submitted a photo or not
                if(photoFile==null || profileImage.getDrawable() == null){
                    Log.e(TAG, "No Photo To Submit");
                    Toast.makeText(getContext(),"Please submit a photo", Toast.LENGTH_LONG).show();
                    return;
                }*/


            }
        });


        queryPosts();

    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

        //getPackagemanager is called on a context
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if the code is what we started the activity with, we can continue
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                profileImage.setImageBitmap(takenImage);
                //means picture wasn't actually taken
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        savePhotoToDisk(file);

        return file;
    }

    public void savePhotoToDisk(File file) {
        ParseFile parseFile = new ParseFile(file);
        //puts our clicked profile image to
        parseUser.put("profileImage", parseFile);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Log.d("TAG", "Profile Image has been stored to the database");
                else
                    Log.d("NONTAG", "Post not saved");
            }

        });

    }


    public void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);

        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        //postQuery.filter-> for whichever user
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error in query");
                    e.printStackTrace();
                }
                //run the loop the other way around
                int len = objects.size() - 1;
                for (int i = len; i >= 0; i--) {
                    Post post = objects.get(i);
                    posts.add(post);
                    adapter.notifyItemInserted(0);
                }
            }
        });
    }

}

