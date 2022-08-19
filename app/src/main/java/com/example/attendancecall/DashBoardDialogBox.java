package com.example.attendancecall;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class DashBoardDialogBox {

    public void showDialog(Activity activity, TextView sectionName) {


        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dash_board_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TextView textMsg = (TextView) dialog.findViewById(R.id.dialog_box_msg);

        CardView teacherSection = (CardView) dialog.findViewById(R.id.section_teacher);
        CardView studentSection = (CardView) dialog.findViewById(R.id.section_student);
        CardView requestSendSection = (CardView) dialog.findViewById(R.id.section_requestSent);
        CardView requestReceivedSection = (CardView) dialog.findViewById(R.id.section_requestReceived);

        CardView editProfile = (CardView) dialog.findViewById(R.id.dashBoard_editProfile);
        CardView addNewUser = (CardView) dialog.findViewById(R.id.dashBoard_addNewUser);
        CardView switchAccount = (CardView) dialog.findViewById(R.id.dashBoard_switchAccount);
        CardView logOut = (CardView) dialog.findViewById(R.id.dashBoard_logOut);

        ImageView menuCancel = (ImageView) dialog.findViewById(R.id.dialogBox_menuCancel);

        dialog.show();

        teacherSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sectionName.getText().toString().trim().toLowerCase(Locale.ROOT).equals("teacher")) {
                    dialog.dismiss();
                } else {
                    Intent intent = new Intent(activity, TeacherActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

            }
        });

        studentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sectionName.getText().toString().trim().toLowerCase(Locale.ROOT).equals("student")) {
                    dialog.dismiss();
                } else {
                    Intent intent = new Intent(activity, StudentActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

            }
        });

        requestSendSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RequestSent.class);
                activity.startActivity(intent);
            }
        });

        requestReceivedSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RequestReceived.class);
                activity.startActivity(intent);
            }
        });

        menuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditProfile.class);
                activity.startActivity(intent);
            }
        });

        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupLogoutForNewUser(activity);
            }
        });

        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupLogout(activity);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupLogout(activity);
            }
        });
    }

    private void showPopupLogout(Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogbox_of_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        Button btn_ok = (Button) dialog.findViewById(R.id.logout_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.logout_cancel);

        dialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = dialog.getContext().getSharedPreferences("login_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
//                FirebaseUser firebaseUser = fAuth.getCurrentUser();


                SharedPreferences sharedPreferences_isLogin = dialog.getContext().getSharedPreferences("MyPrefsLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_isLogin = sharedPreferences_isLogin.edit();

                editor_isLogin.putBoolean("hasLoggedIn", false);
                editor_isLogin.commit();

                editor.clear();
                editor.apply();

                fAuth.signOut();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void showPopupLogoutForNewUser(Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogbox_of_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        Button btn_ok = (Button) dialog.findViewById(R.id.logout_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.logout_cancel);

        dialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences preferences = dialog.getContext().getSharedPreferences("login_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
//                FirebaseUser firebaseUser = fAuth.getCurrentUser();

                SharedPreferences sharedPreferences_isLogin = dialog.getContext().getSharedPreferences("MyPrefsLogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_isLogin = sharedPreferences_isLogin.edit();

                editor_isLogin.putBoolean("hasLoggedIn", false);
                editor_isLogin.commit();

                editor.clear();
                editor.apply();

                fAuth.signOut();
                Intent intent = new Intent(activity, SignupActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
