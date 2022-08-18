package com.example.attendancecall;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class DashBoardDialogBox {

    public void showDialog(Activity activity){


        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dash_board_dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textMsg = (TextView) dialog.findViewById(R.id.dialog_box_msg);

        CardView teacherSection = (CardView) dialog.findViewById(R.id.section_teacher);
        CardView studentSection = (CardView) dialog.findViewById(R.id.section_student);
        CardView requestSendSection = (CardView) dialog.findViewById(R.id.section_requestSent);
        CardView requestReceivedSection = (CardView) dialog.findViewById(R.id.section_requestReceived);

        ImageView menuCancel = (ImageView) dialog.findViewById(R.id.dialogBox_menuCancel);

        dialog.show();

        teacherSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeacherActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        studentSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, StudentActivity.class);
                activity.startActivity(intent);
                activity.finish();
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
    }
}
