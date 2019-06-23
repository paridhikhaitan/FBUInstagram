package com.example.fbuinstagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupUser;
    private EditText signupEmail;
    private EditText signupPassword;
    private Button buttonSignup;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "got called", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupUser= (EditText) findViewById(R.id.signupName);
        signupEmail= (EditText) findViewById(R.id.signupEmail);
        signupPassword= (EditText) findViewById(R.id.signupPassword);
        buttonSignup= (Button) findViewById(R.id.button_signup);

        user= new ParseUser();

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setUsername(signupUser.getText().toString());
                user.setEmail(signupEmail.getText().toString());
                user.setPassword(signupPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Log.e("LoginActivity", "YAY WE LOGGED IN");
                            Intent intent= new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.e("LoginActivity", "Login FAILURE");
                        }
                    }
                });
            }
        });
    }
}
