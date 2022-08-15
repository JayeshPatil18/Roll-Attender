package com.example.attendancecall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsertypeActivity.this, StudentActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsertypeActivity.this, TeacherActivity.class);
                startActivity(intent);
//                finish();
            }
        });

    }
}