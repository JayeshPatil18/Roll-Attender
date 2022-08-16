package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyingEmail extends AppCompatActivity {

    public static String PREFS_NAME = "MyPrefsFile";

    Button resendBtn, verifyBtn;
    FirebaseAuth fAuth;
    FirebaseUser fuser;

    TextView invalidDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying_email);

        resendBtn = findViewById(R.id.resend_dilogbox_btn);
        verifyBtn = findViewById(R.id.verifyEmail_dilogbox_btn);
        invalidDisplay = findViewById(R.id.resend_dilogbox_error);

        fAuth = FirebaseAuth.getInstance();

        fuser = fAuth.getCurrentUser();

        String emailStr = getIntent().getStringExtra("email_str");
        String passwordStr = getIntent().getStringExtra("password_str");

        fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(VerifyingEmail.this, "Email id is verified successfully", Toast.LENGTH_SHORT).show();
                }else {
                    invalidDisplay.setText("Email not sent: " + task.getException().getMessage());
                }
            }
        });

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(VerifyingEmail.this, "Email id is verified successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            invalidDisplay.setText("Email not sent: " + task.getException().getMessage());
                        }
                    }
                });
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (fuser.isEmailVerified()){
                                SharedPreferences sharedPreferences = getSharedPreferences(VerifyingEmail.PREFS_NAME,0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putBoolean("hasLoggedIn",true);
                                editor.commit();

                                Toast.makeText(VerifyingEmail.this, "Email is verified successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyingEmail.this, UsertypeActivity.class);
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(VerifyingEmail.this, "Not Verified", Toast.LENGTH_SHORT).show();
                                invalidDisplay.setText("* Please check email box then click verify email");
                            }
                        }
                    }
                });
            }
        });
    }
}