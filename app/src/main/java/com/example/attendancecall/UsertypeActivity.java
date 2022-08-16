package com.example.attendancecall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UsertypeActivity extends AppCompatActivity {

    Button student, teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);

        SharedPreferences sharedPreferences_isLogin = getSharedPreferences(VerifyingEmail.PREFS_NAME,MODE_PRIVATE);
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
//                finish();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor_loginDetails.putString("role","teacher");
                editor_loginDetails.commit();

                Intent intent = new Intent(UsertypeActivity.this, TeacherActivity.class);
                startActivity(intent);
//                finish();
            }
        });

    }
}