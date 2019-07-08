package com.example.fbuinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.fbuinstagram.Fragments.ComposeFragment;
import com.example.fbuinstagram.Fragments.ProfileFragment;
import com.example.fbuinstagram.Fragments.TimelineFragment;
import com.parse.ParseUser;

public class CameraActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment composeFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.home_action:
                        composeFragment = new TimelineFragment();
                        break;
                    case R.id.post_action:
                        composeFragment = new ComposeFragment();
                        break;
                    case R.id.profile_action:
                        composeFragment = new ProfileFragment();
                        break;
                    case R.id.logout_action:
                        ParseUser.logOut();
                        return true;
                    default:
                        composeFragment = new TimelineFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flCamera, composeFragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home_action);

    }//end of onCreate

}
