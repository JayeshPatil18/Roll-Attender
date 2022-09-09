package com.apt.attendancecall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class UsertypeActivity extends AppCompatActivity {

    private AdView mAdView;

    Button student, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);

        mAdView = findViewById(R.id.adView_role);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);

        SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details",MODE_PRIVATE);
        SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

        boolean hasLoggedIn = sharedPreferences_isLogin.getBoolean("hasLoggedIn",false);

        if (!hasLoggedIn){
            Intent intent = new Intent(UsertypeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor_loginDetails.putString("role","student");
                editor_loginDetails.commit();

                Intent intent = new Intent(UsertypeActivity.this, StudentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor_loginDetails.putString("role","teacher");
                editor_loginDetails.commit();

                Intent intent = new Intent(UsertypeActivity.this, TeacherActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}