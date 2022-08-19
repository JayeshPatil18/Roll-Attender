package com.example.attendancecall;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class ValidationOfEditProfile {

    public void updateName(String str_name, TextInputLayout nameLayout, String str_emailId, FirebaseAuth fAuth, FirebaseUser firebaseUser, DatabaseReference root, Activity activity) {
        boolean allFieldFilled = true;

        if (allFieldFilled) {
            ValidationOfInput validation = new ValidationOfInput();
            boolean validName = validation.validatedName(str_name, nameLayout);

            if (validName) {

                EncoderDecoder encoderDecoder = new EncoderDecoder();
                str_emailId = encoderDecoder.encodeUserEmail(str_emailId);

                root.child(str_emailId).child("details").child("name").setValue(str_name);

                Toast.makeText(activity, "Name updated successfully", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                }, 1000);
            }
        }
    }

    public void updatePassword(String str_password, String str_rEnterPassword, TextInputLayout passwordLayout, TextInputLayout rEnterPasswordLayout, String str_emailId, FirebaseAuth fAuth, FirebaseUser firebaseUser, DatabaseReference root, Activity activity) {
        boolean allFieldFilled = true;

        if (str_rEnterPassword.isEmpty()) {
            rEnterPasswordLayout.setErrorEnabled(true);
            rEnterPasswordLayout.setError("* Field is empty");
            allFieldFilled = false;
        } else {
            if (allFieldFilled) {
                ValidationOfInput validation = new ValidationOfInput();
                boolean validPassword = validation.isValidCreation(str_password, passwordLayout);
                boolean validRePassword = validation.isPasswordMatched(str_password, str_rEnterPassword, rEnterPasswordLayout);

                if (validPassword && validRePassword) {

                    EncoderDecoder encoderDecoder = new EncoderDecoder();
                    str_emailId = encoderDecoder.encodeUserEmail(str_emailId);

                    root.child(str_emailId).child("details").child("password").setValue(str_password);

                    Toast.makeText(activity, "Password updated successfully", Toast.LENGTH_SHORT).show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, 1000);
                }
            }
        }
    }

    public void updateName_Password(String str_name, String str_password, String str_rEnterPassword, TextInputLayout nameLayout, TextInputLayout passwordLayout, TextInputLayout rEnterPasswordLayout, String str_emailId, FirebaseAuth fAuth, FirebaseUser firebaseUser, DatabaseReference root, Activity activity) {
        boolean allFieldFilled = true;

        if (str_rEnterPassword.isEmpty()) {
            rEnterPasswordLayout.setErrorEnabled(true);
            rEnterPasswordLayout.setError("* Field is empty");
            allFieldFilled = false;
        } else {
            if (allFieldFilled) {
                ValidationOfInput validation = new ValidationOfInput();
                boolean validName = validation.validatedName(str_name, nameLayout);
                boolean validPassword = validation.isValidCreation(str_password, passwordLayout);
                boolean validRePassword = validation.isPasswordMatched(str_password, str_rEnterPassword, rEnterPasswordLayout);

                if (validName && validPassword && validRePassword) {

                    EncoderDecoder encoderDecoder = new EncoderDecoder();

                    firebaseUser.updatePassword(str_password).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            String strEmailId = encoderDecoder.encodeUserEmail(str_emailId);

                            root.child(strEmailId).child("details").child("name").setValue(str_name);
                            root.child(strEmailId).child("details").child("password").setValue(str_password);

                            Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    activity.finish();
                                }
                            }, 1000);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Something went wrong! Try again", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        }
    }
}
