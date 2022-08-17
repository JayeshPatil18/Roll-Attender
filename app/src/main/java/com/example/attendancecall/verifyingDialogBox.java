package com.example.attendancecall;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class verifyingDialogBox extends Activity{

    FirebaseAuth fAuth;
    FirebaseUser fUser;

    public void showDialog(Activity activity, String msg, String emailStr, String passwordStr){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.verifying_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textMsg = (TextView) dialog.findViewById(R.id.dialog_box_msg);
        textMsg.setText(msg);

        Button dialogBtn_verify = (Button) dialog.findViewById(R.id.dialog_box_verifyBtn);
        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.dialog_box_cancelBtn);

        RelativeLayout send_RelativeLayout = (RelativeLayout) dialog.findViewById(R.id.sendRelativeLayout);
        RelativeLayout valid_RelativeLayout = (RelativeLayout) dialog.findViewById(R.id.validRelativeLayout);

        send_RelativeLayout.setVisibility(View.VISIBLE);
        valid_RelativeLayout.setVisibility(View.GONE);

        fAuth = FirebaseAuth.getInstance();

        fUser = fAuth.getCurrentUser();

        final boolean[] isVerified = {false};


        fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(dialog.getContext(), "Verification Email has been sent successfully", Toast.LENGTH_SHORT).show();
                }else {
                    textMsg.setText("Email not sent: " + task.getException().getMessage());
                }
            }
        });

        dialogBtn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (fUser.isEmailVerified()){
                                isVerified[0] = true;

                                send_RelativeLayout.setVisibility(View.GONE);
                                valid_RelativeLayout.setVisibility(View.VISIBLE);

                                SharedPreferences sharedPreferences_isLogin = dialog.getContext().getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
                                SharedPreferences.Editor editor_isLogin = sharedPreferences_isLogin.edit();

                                SharedPreferences sharedPreferences_loginDetails = dialog.getContext().getSharedPreferences("login_details",MODE_PRIVATE);
                                SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

                                editor_isLogin.putBoolean("hasLoggedIn",true);
                                editor_loginDetails.putString("email_id",emailStr);
                                editor_loginDetails.putString("password",passwordStr);
                                editor_isLogin.commit();
                                editor_loginDetails.commit();

                                Toast.makeText(dialog.getContext(), "Email is verified successfully", Toast.LENGTH_SHORT).show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Intent intent = new Intent(activity, UsertypeActivity.class);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    }
                                }, 1000);


                            }else{
                                Toast.makeText(dialog.getContext(), "Please verify your email from link send to your email " + emailStr, Toast.LENGTH_SHORT).show();
                                textMsg.setText("* Please check email box then click verify email");
                            }
                        }
                    }
                });
            }
        });
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVerified[0] == false) {
                    dialog.dismiss();
                    return;
                }
            }
        });
        dialog.show();
    }
}
