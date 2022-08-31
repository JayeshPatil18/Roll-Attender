package com.apt.attendancecall;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apt.attendancecall.ui.main.SectionsPagerAdapter;
import com.apt.attendancecall.databinding.ActivityAttendanceViewTabBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AttendanceViewTab extends AppCompatActivity {
    TextView subject_name, subject_date;

    EncoderDecoder decoder = new EncoderDecoder();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    // For back button
    ImageView back_img;

    private ActivityAttendanceViewTabBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EncoderDecoder encoderDecoder = new EncoderDecoder();

        String strSubject = getIntent().getStringExtra("subject_for_date").toLowerCase(Locale.ROOT);
        String strDate = getIntent().getStringExtra("date_of_subject");

        InfoForFrag info = new InfoForFrag();
        info.setStrDate(strDate.replace(" / ", "-"));
        info.setStrSubject(strSubject);

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

        binding = ActivityAttendanceViewTabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        ////
        TextView addPresent = (TextView) findViewById(R.id.addPresentRoll);
        TextView addAbsent = (TextView) findViewById(R.id.addAbsentRoll);

        addPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder adder = new AlertDialog.Builder(AttendanceViewTab.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_by_teacher, null);

                final EditText rollNo = (EditText) view1.findViewById(R.id.name_dlbox);
                Button btn_add = (Button) view1.findViewById(R.id.add_btn_dlbox);
                Button btn_cancel = (Button) view1.findViewById(R.id.cancel_btn_dlbox);
                // For validation error
                TextView invalidDisplay = (TextView) view1.findViewById(R.id.Adder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(true); // For dismiss if user click outside dialog box

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(strSubject).child(strDate.replace(" / ", "-"));

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        invalidDisplay.setText("");

                        final String strRoll_no = rollNo.getText().toString().trim();
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                if (strRoll_no.isEmpty()) {
                                    invalidDisplay.setText("* Roll No field is empty");
                                } else {
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedRollNo(strRoll_no, invalidDisplay);

                                    if (validSubName) {

                                        rollNo.setText("");
//                                            root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_for_date);
                                        root.child(strRoll_no.toString()).setValue("p");
                                        Toast.makeText(AttendanceViewTab.this, "Roll No Added Successfully", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });

        addAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder adder = new AlertDialog.Builder(AttendanceViewTab.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_by_teacher, null);

                final EditText rollNo = (EditText) view1.findViewById(R.id.name_dlbox);
                Button btn_add = (Button) view1.findViewById(R.id.add_btn_dlbox);
                Button btn_cancel = (Button) view1.findViewById(R.id.cancel_btn_dlbox);
                // For validation error
                TextView invalidDisplay = (TextView) view1.findViewById(R.id.Adder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(true); // For dismiss if user click outside dialog box

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(strSubject).child(strDate.replace(" / ", "-"));

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        invalidDisplay.setText("");

                        final String strRoll_no = rollNo.getText().toString().trim();
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                if (strRoll_no.isEmpty()) {
                                    invalidDisplay.setText("* Roll No field is empty");
                                } else {
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedRollNo(strRoll_no, invalidDisplay);

                                    if (validSubName) {

                                        rollNo.setText("");
//                                            root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_for_date);
                                        root.child(strRoll_no.toString()).setValue("a");
                                        Toast.makeText(AttendanceViewTab.this, "Roll No Added Successfully", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });

        // For back button
        back_img = findViewById(R.id.viewer_back_img);
        // For back button
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}