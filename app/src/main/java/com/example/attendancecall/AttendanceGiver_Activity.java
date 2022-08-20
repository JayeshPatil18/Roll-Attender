package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class AttendanceGiver_Activity extends AppCompatActivity {

    // For validation error
    TextView invalidDisplay;

    // For back button
    ImageView back_img;

    TextView avai_subject, avai_date;

    Button submit_btn;
    EditText stud_name, stud_endrollment_no, stud_branch, stud_phone;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root;

    String emailId, availableSubject, availableDate;

    EncoderDecoder decoder = new EncoderDecoder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_giver);

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        emailId = getIntent().getStringExtra("available_emailId");
        availableSubject = getIntent().getStringExtra("available_subject_for_date").toLowerCase(Locale.ROOT);
        availableDate = getIntent().getStringExtra("available_date_of_subject").replace(" / ","-");
        /////////////////////////////////////////////////////////////////////////////////////////////

        // For validation error
        invalidDisplay = findViewById(R.id.attendance_error);

        // For back button
        back_img = findViewById(R.id.giver_back_img);

        submit_btn = findViewById(R.id.submit_btn);
        stud_name = findViewById(R.id.stud_name);
        stud_endrollment_no= findViewById(R.id.stud_endrollment_no);
        stud_branch = findViewById(R.id.stud_branch);
        stud_phone = findViewById(R.id.stud_phone);

        avai_subject = findViewById(R.id.available_sub_name);
        avai_date = findViewById(R.id.available_date_title);

        avai_subject.setText(availableSubject.substring(0, 1).toUpperCase() + availableSubject.substring(1));
        avai_date.setText(availableDate.replace("-"," / "));


        // For going back if any dates data changed
        root = database.getReference().child("admin_users").child(emailId).child("subjects");

        // For getting email address
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(availableSubject+"/"+availableDate)) {
                    Toast.makeText(AttendanceGiver_Activity.this, "Something went wrong Please try again", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invalidDisplay.setText("");

                TextInputLayout nameLayout = (TextInputLayout)findViewById(R.id.stud_nameLayout);
                TextInputLayout enrollmentNoLayout = (TextInputLayout)findViewById(R.id.stud_endrollment_noLayout);
                TextInputLayout branchLayout = (TextInputLayout)findViewById(R.id.stud_branchLayout);
                TextInputLayout phoneNoLayout = (TextInputLayout)findViewById(R.id.stud_phoneLayout);

                nameLayout.setErrorEnabled(false);
                enrollmentNoLayout.setErrorEnabled(false);
                branchLayout.setErrorEnabled(false);
                phoneNoLayout.setErrorEnabled(false);

                String student_name = stud_name.getText().toString().toLowerCase(Locale.ROOT).trim();
                String student_enrollment_no = stud_endrollment_no.getText().toString().trim();
                String student_branch = stud_branch.getText().toString().toLowerCase(Locale.ROOT).trim();
                String student_phone_no = stud_phone.getText().toString().trim();

                boolean allFieldFilled = true;

                if (student_name.isEmpty()){
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("* Name field is empty");
                    allFieldFilled= false;
                }
                if (student_enrollment_no.isEmpty()){
                    enrollmentNoLayout.setErrorEnabled(true);
                    enrollmentNoLayout.setError("* Enrollment no field is empty");
                    allFieldFilled= false;
                }
                if (student_branch.isEmpty()){
                    branchLayout.setErrorEnabled(true);
                    branchLayout.setError("* Branch field is empty");
                    allFieldFilled= false;
                }
                if (student_phone_no.isEmpty()){
                    phoneNoLayout.setErrorEnabled(true);
                    phoneNoLayout.setError("* Phone no field is empty");
                    allFieldFilled= false;
                }
                if (allFieldFilled){
                    ValidationOfInput validation = new ValidationOfInput();
                    boolean validName = validation.validatedName(student_name, nameLayout);
                    boolean validEnrollmentNo = validation.validatedEnrollmentNo(student_enrollment_no, enrollmentNoLayout);
                    boolean validBranch = validation.validatedBranch(student_branch, branchLayout);
                    boolean validPhoneNo = validation.validatedPhoneNo(student_phone_no, phoneNoLayout);

                    if (validName && validEnrollmentNo && validBranch && validPhoneNo) {
                        SharedPreferences sharedPreferences_emailId = getSharedPreferences("login_details",MODE_PRIVATE);
                        String student_emailId = sharedPreferences_emailId.getString("email_id","null");

                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("email", student_emailId);
                        userMap.put("name", student_name);
                        userMap.put("enrollment_no", student_enrollment_no);
                        userMap.put("branch", student_branch);
                        userMap.put("phone_no", student_phone_no);

                        root = database.getReference().child("admin_users").child(emailId).child("subjects").child(availableSubject).child(availableDate);

                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                boolean isDateExist = false;

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    String model = dataSnapshot.getKey();

                                    if (model.equals(student_emailId)) {
                                        isDateExist = true;
                                    }
                                }

                                if (!isDateExist) {
                                    stud_name.setText("");
                                    stud_endrollment_no.setText("");
                                    stud_branch.setText("");
                                    stud_phone.setText("");
                                    root.child(student_emailId).setValue(userMap); // student details in student_name child
                                    Toast.makeText(AttendanceGiver_Activity.this, "Your Attendance is Submitted", Toast.LENGTH_SHORT).show();
                                } else {
                                    root.child(student_emailId).setValue(userMap); // student details in student_name child
                                    Toast.makeText(AttendanceGiver_Activity.this, "Your Details are Updated", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
            }
        });

        // For back button
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}