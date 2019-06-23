package com.example.fbuinstagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private final String TAG="HomeActivity";
    private EditText caption;
    private Button submitButton;
    private ImageView cameraClick;
    private ImageView photoDisplay;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName="photo.jpg";
    private File photoFile;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        caption= (EditText) findViewById(R.id.et_caption);
        submitButton= (Button) findViewById(R.id.bt_submit);
        cameraClick= (ImageView) findViewById(R.id.iv_photoButton);
        photoDisplay= (ImageView) findViewById(R.id.iv_displayphoto);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);

        cameraClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        //queryPosts();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description= caption.getText().toString();
                ParseUser user= ParseUser.getCurrentUser();

                //Some error checking before we call save post
                if(photoFile==null || photoDisplay.getDrawable() == null){
                    Log.e(TAG, "No Photo To Submit");
                    Toast.makeText(HomeActivity.this,"Please submit a photo", Toast.LENGTH_LONG).show();
                    return;
                }

                savePost(description, user, photoFile);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.profile_action){
                    intent= new Intent(HomeActivity.this, UserProfile.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.home_action){
                    intent= new Intent(HomeActivity.this, UserTimeline.class);
                    startActivity(intent);
                }
                if(menuItem.getItemId()==R.id.logout_action){
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                    intent= new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
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
                photoDisplay.setImageBitmap(takenImage);
                //means picture wasn't actually taken
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    //this is where we will actually put content back to the class
    private void savePost(String description, ParseUser parseUser, final File photoFile) {
        Post post= new Post();
        post.setDescription(description);
        post.setUser(parseUser);
        post.setImage(new ParseFile(photoFile));

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error in creating a new post");
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "Success in posting");
                caption.setText("");
                photoDisplay.setImageResource(0);

            }
        });

        Intent intent= new Intent(HomeActivity.this, UserTimeline.class);
        startActivity(intent);


    }



}
