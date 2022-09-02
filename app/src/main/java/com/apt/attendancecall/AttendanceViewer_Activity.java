package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AttendanceViewer_Activity extends AppCompatActivity implements RecyclerViewInterfaceTeacher {

    String flag;

    TextView tabP, tabA;

    TextView subject_name, subject_date;

    EncoderDecoder decoder = new EncoderDecoder();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    // For back button
    ImageView back_img;
    ImageView deleteAllImg;
    ImageView rangeImg;

    //For refresher
//    SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    AdapterForTeacher adapter;

    TextView subject_title, date_title;

    String strSubject;
    String strDate;
    String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_viewer);


        flag = "present";

        TextView emptyMsg = (TextView) findViewById(R.id.empty_view);

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_viewer);
        shimmerFrameLayout.startShimmer();

        tabP = findViewById(R.id.TFaketabP);
        tabA = findViewById(R.id.TFaketabA);

        deleteAllImg = findViewById(R.id.deleteAllRollNo);
        rangeImg = findViewById(R.id.resetRangeImg);

        strSubject = getIntent().getStringExtra("subject_for_date").toLowerCase(Locale.ROOT);
        strDate = getIntent().getStringExtra("date_of_subject").replace(" / ", "-");

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

        rangeDialogBox();


        // For back button
        back_img = findViewById(R.id.viewer_back_img);

        TextView addPresent = (TextView) findViewById(R.id.addPresentRoll);
        TextView addAbsent = (TextView) findViewById(R.id.addAbsentRoll);

        subject_name = findViewById(R.id.subject_title);
        subject_date = findViewById(R.id.date_title);

        subject_name.setText(strSubject.substring(0, 1).toUpperCase() + strSubject.substring(1));
        subject_date.setText(strDate.replace("-", " / "));


        recyclerView = findViewById(R.id.student_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new AdapterForTeacher(this, list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));


        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(strSubject).child(strDate);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // this work to clear old item
                String model = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    model = dataSnapshot.getKey();

                    if (flag.equals("present")) {
                        if (dataSnapshot.getValue(String.class).equals("p")) {
                            list.add(model);

                        }
                    } else if (flag.equals("absent")) {
                        if (dataSnapshot.getValue(String.class).equals("a")) {
                            list.add(model);

                        }
                    }
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                if (list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyMsg.setVisibility(View.VISIBLE);
                } else {
                    emptyMsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag = "present";

                tabP.setTextColor(getResources().getColor(R.color.white));
                tabA.setTextColor(getResources().getColor(R.color.app_default));
                tabA.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.stub));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.fake_tab_textview));

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear(); // this work to clear old item
                        String model = null;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            model = dataSnapshot.getKey();

                            if (dataSnapshot.getValue(String.class).equals("p")) {
                                list.add(model);

                            }

                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        if (list.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyMsg.setVisibility(View.VISIBLE);
                        } else {
                            emptyMsg.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tabA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = "absent";

                tabA.setTextColor(getResources().getColor(R.color.white));
                tabP.setTextColor(getResources().getColor(R.color.app_default));
                tabA.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.fake_tab_textview));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.stub));
                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear(); // this work to clear old item
                        String model = null;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            model = dataSnapshot.getKey();

                            if (dataSnapshot.getValue(String.class).equals("a")) {
                                list.add(model);

                            }

                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        if (list.isEmpty()) {
                            recyclerView.setVisibility(View.GONE);
                            emptyMsg.setVisibility(View.VISIBLE);
                        } else {
                            emptyMsg.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        addPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder adder = new AlertDialog.Builder(AttendanceViewer_Activity.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_by_teacher, null);

                final EditText rollNo = (EditText) view1.findViewById(R.id.name_dlbox);
                Button btn_add = (Button) view1.findViewById(R.id.add_btn_dlbox);
                Button btn_cancel = (Button) view1.findViewById(R.id.cancel_btn_dlbox);
                // For validation error
                TextView invalidDisplay = (TextView) view1.findViewById(R.id.Adder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(false); // For dismiss if user click outside dialog box

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
                                    invalidDisplay.setText("* Roll no. field is empty");
                                } else {
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedRollNo(strRoll_no, invalidDisplay);

                                    if (validSubName) {

                                        boolean isValidRollNo = validation.isRollNoFormatValid(strRoll_no, invalidDisplay);

                                        if (isValidRollNo) {
                                            boolean isSuccess = false;
                                            rollNo.setText("");
//                                            root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_for_date);
                                            if (strRoll_no.contains(",")) {

                                                ArrayList<String> arr = validation.findRollFromString(strRoll_no);
                                                for (int i = 0; i < arr.size(); i++) {

                                                    if (!arr.get(i).isEmpty()) {
                                                        root.child(arr.get(i).toString()).setValue("p");
                                                        isSuccess = true;
                                                    }
                                                }
                                            } else {
                                                root.child(strRoll_no.toString()).setValue("p");
                                                isSuccess = true;
                                            }
                                            if (isSuccess) {
                                                Toast.makeText(AttendanceViewer_Activity.this, "Roll no. Added Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }

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


                final AlertDialog.Builder adder = new AlertDialog.Builder(AttendanceViewer_Activity.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_by_teacher, null);

                final EditText rollNo = (EditText) view1.findViewById(R.id.name_dlbox);
                Button btn_add = (Button) view1.findViewById(R.id.add_btn_dlbox);
                Button btn_cancel = (Button) view1.findViewById(R.id.cancel_btn_dlbox);
                // For validation error
                TextView invalidDisplay = (TextView) view1.findViewById(R.id.Adder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(false); // For dismiss if user click outside dialog box

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
                                    invalidDisplay.setText("* Roll no. field is empty");
                                } else {
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedRollNo(strRoll_no, invalidDisplay);

                                    if (validSubName) {

                                        boolean isValidRollNo = validation.isRollNoFormatValid(strRoll_no, invalidDisplay);

                                        if (isValidRollNo) {
                                            boolean isSuccess = false;
                                            rollNo.setText("");
//                                            root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_for_date);
                                            if (strRoll_no.contains(",")) {

                                                ArrayList<String> arr = validation.findRollFromString(strRoll_no);
                                                for (int i = 0; i < arr.size(); i++) {

                                                    if (!arr.get(i).isEmpty()) {
                                                        root.child(arr.get(i).toString()).setValue("a");
                                                        isSuccess = true;
                                                    }
                                                }
                                            } else {
                                                root.child(strRoll_no.toString()).setValue("a");
                                                isSuccess = true;
                                            }
                                            if (isSuccess) {
                                                Toast.makeText(AttendanceViewer_Activity.this, "Roll no. Added Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
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

        deleteAllImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeAllRolNoPopUpDialogBox();
            }
        });

        rangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rangeDialogBox();
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

    @Override
    public void onItemClickTeacher(int position) {

    }

    @Override
    public void onItemClick(int position) {
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String itemText = list.get(position).toString();
        String userEmail = decoder.encodeUserEmail(emailId);

        database.getReference("admin_users").child(userEmail).child("subjects").child(strSubject).child(strDate).child(itemText).removeValue();
        Toast.makeText(AttendanceViewer_Activity.this, "Roll no. removed successfully", Toast.LENGTH_SHORT).show();
    }

    public void rangeDialogBox() {
        final Dialog dialog = new Dialog(AttendanceViewer_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.range_of_roll_no);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        dialog.show();

        Button submitRange = (Button) dialog.findViewById(R.id.setRangeBtn);
        ImageView cancelRange = (ImageView) dialog.findViewById(R.id.cancelRangeBtn);

        NumberPicker startRange = (NumberPicker) dialog.findViewById(R.id.rangeS);
        NumberPicker endRange = (NumberPicker) dialog.findViewById(R.id.rangeE);

        startRange.setMinValue(1);
        startRange.setMaxValue(1000);

        endRange.setMinValue(1);
        endRange.setMaxValue(1000);

        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        int sRangeNum = sharedPreferences_loginDetails.getInt("s_range", 1);
        int eRangeNum = sharedPreferences_loginDetails.getInt("e_range", 50);

        startRange.setValue(sRangeNum);
        endRange.setValue(eRangeNum);

        final boolean[] cancelAllow = {true};

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(strSubject).child(strDate);

        submitRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAllow[0] = false;

                int sNum = startRange.getValue();
                int eNum = endRange.getValue();

                /////
                SharedPreferences sharedPreferences_loginDetails = dialog.getContext().getSharedPreferences("login_details", MODE_PRIVATE);
                SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

                editor_loginDetails.putInt("s_range", sNum);
                editor_loginDetails.putInt("e_range", eNum);
                editor_loginDetails.commit();
                /////

                for (int i = sNum; i <= eNum; i++) {
                    root.child(String.valueOf(i)).setValue("p");
                }
                dialog.dismiss();
            }
        });

        cancelRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelAllow[0]) {
                    dialog.dismiss();
                }
            }
        });
    }

    private void removeAllRolNoPopUpDialogBox() {

        final Dialog dialog = new Dialog(AttendanceViewer_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.plain_logout_dilogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textView = (TextView) dialog.findViewById(R.id.plainLogoutErrorText);
        textView.setText("All Roll No Attendance will be delete!, Are you sure want to Delete Attendance?");

        Button btn_ok = (Button) dialog.findViewById(R.id.plainLogout_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.plainLogout_cancel);

        dialog.show();

        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(strSubject).child(strDate);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                root.setValue("null");
                Toast.makeText(AttendanceViewer_Activity.this, "All Roll no. removed successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
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
