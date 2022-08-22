package com.example.attendancecall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
                boolean hasLoggedIn = sharedPreferences_isLogin.getBoolean("hasLoggedIn",false);

                if (hasLoggedIn){
                    SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details",MODE_PRIVATE);

                    if (sharedPreferences_loginDetails.getString("role","null").equals("teacher")){
                        Intent intent = new Intent(SplashActivity.this, TeacherActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (sharedPreferences_loginDetails.getString("role","null").equals("student")){
                        Intent intent = new Intent(SplashActivity.this, StudentActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashActivity.this, UsertypeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}