package com.apt.attendancecall;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class DashBoardDialogBox {

    private InterstitialAd mInterstitialAd;

    public void showDialog(Activity activity, TextView sectionName) {


        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dash_board_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) dialog.findViewById(R.id.dashBoardText_shimmer);
        shimmerFrameLayout.startShimmer();

        TextView textMsg = (TextView) dialog.findViewById(R.id.dialog_box_msg);

        CardView teacherSection = (CardView) dialog.findViewById(R.id.section_teacher);
        CardView studentSection = (CardView) dialog.findViewById(R.id.section_student);
        CardView requestSendSection = (CardView) dialog.findViewById(R.id.section_requestSent);
        CardView requestReceivedSection = (CardView) dialog.findViewById(R.id.section_requestReceived);
        CardView yourStudentSection = (CardView) dialog.findViewById(R.id.section_YourStudents);

        CardView editProfile = (CardView) dialog.findViewById(R.id.dashBoard_editProfile);
        CardView addNewUser = (CardView) dialog.findViewById(R.id.dashBoard_addNewUser);
        CardView switchAccount = (CardView) dialog.findViewById(R.id.dashBoard_switchAccount);
        CardView logOut = (CardView) dialog.findViewById(R.id.dashBoard_logOut);

        ImageView menuCancel = (ImageView) dialog.findViewById(R.id.dialogBox_menuCancel);

        dialog.show();

        /////////////
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, "ca-app-pub-6829345224658071/8632637361", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });


        /////////

        ////////////////////////////////////////////////////////////////////////////////////////////
        TextView AdminName = (TextView) dialog.findViewById(R.id.dashBoard_Name);
        TextView AdminEmailId = (TextView) dialog.findViewById(R.id.dashBoard_EmailId);

        SharedPreferences preferences = dialog.getContext().getSharedPreferences("login_details", Context.MODE_PRIVATE);
        String strAdminEmail = preferences.getString("email_id","null");
        AdminEmailId.setText(strAdminEmail);
        final String[] strAdminName = new String[1];

        EncoderDecoder decoder = new EncoderDecoder();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root;

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(strAdminEmail)).child("details");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminInfo adminInfo = snapshot.getValue(AdminInfo.class);
                strAdminName[0] = adminInfo.getName();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                AdminName.setVisibility(View.VISIBLE);

                AdminName.setText(strAdminName[0].substring(0, 1).toUpperCase() + (strAdminName[0].substring(1)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        teacherSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sectionName.getText().toString().trim().toLowerCase(Locale.ROOT).equals("teacher")) {
                    dialog.dismiss();
                } else {
                    Intent intent = new Intent(activity, TeacherActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                    show(view, activity);
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
                    show(view, activity);
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

        yourStudentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, YourStudents.class);
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
                showPopupLogoutForNewUser(activity,"You will logout from this account!");
            }
        });

        switchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupLogout(activity, "You will logout from this account!");
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupLogoutPlain(activity, "Are you sure want to logout?");
            }
        });
    }

    private void showPopupLogout(Activity activity, String s) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogbox_of_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textView = (TextView) dialog.findViewById(R.id.logoutErrorText);
        textView.setText(s);

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

    private void showPopupLogoutForNewUser(Activity activity, String s) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialogbox_of_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textView = (TextView) dialog.findViewById(R.id.logoutErrorText);
        textView.setText(s);
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



    private void showPopupLogoutPlain(Activity activity, String s) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.plain_logout_dilogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textView = (TextView) dialog.findViewById(R.id.plainLogoutErrorText);
        textView.setText(s);

        Button btn_ok = (Button) dialog.findViewById(R.id.plainLogout_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.plainLogout_cancel);

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

    private void show(View view, Activity activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
        }
    }
}
