package com.example.fbuinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private EditText UserName;
    private EditText UserPassword;
    private Button LoginButton;
    private Button SignUpButton;
    ParseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //makes the user data persist across activities
        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent= new Intent(MainActivity.this, UserTimeline.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this just binds the value, doesn't store it
        UserName= (EditText) findViewById(R.id.UserName);
        UserPassword= (EditText) findViewById(R.id.UserPassword);
        LoginButton= (Button) findViewById(R.id.buttonLogIn);
        SignUpButton= (Button) findViewById(R.id.signUpButton);

        //this is how you set an onClickListener on a button
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //want to log the user in
               final String username= UserName.getText().toString();
                 final String password= UserPassword.getText().toString();
                onLogin(username, password);
            }
        });

       SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    }

    //actually performs the login
    private void onLogin(String userName, String password){
        Toast.makeText(this, "called onLogin", Toast.LENGTH_LONG);


        //Parse will check verification itself to see if the username and password matches
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            //handles a situation where the user has not responded
            @Override
            public void done(ParseUser user, ParseException e) {
                //if you don't have any exceptions thrown
                if(e ==null) {
                    Log.d("LoginActivity", "Log in successfull woo");
                    Intent intent= new Intent(MainActivity.this, UserTimeline.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure right here!");
                    e.printStackTrace();
                }
            }
        });

        }
}
