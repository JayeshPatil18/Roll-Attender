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

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AttendanceViewer_Activity extends AppCompatActivity implements RecyclerViewInterfaceTeacher{

    String flag;

    TextView tabP, tabA;

    TextView subject_name, subject_date;

    EncoderDecoder decoder = new EncoderDecoder();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    // For back button
    ImageView back_img;

    //For refresher
//    SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    AdapterForTeacher adapter;

    TextView subject_title, date_title;

    String strSubject;
    String strDate;

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

        strSubject = getIntent().getStringExtra("subject_for_date").toLowerCase(Locale.ROOT);
        strDate = getIntent().getStringExtra("date_of_subject").replace(" / ","-");

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

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
        adapter = new AdapterForTeacher(this ,list, this);

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

                    if (flag.equals("present")){
                        if (dataSnapshot.getValue(String.class).equals("p"))
                        {
                            list.add(model);

                        }
                    }
                    else if (flag.equals("absent")){
                        if (dataSnapshot.getValue(String.class).equals("a"))
                        {
                            list.add(model);

                        }
                    }
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                if (list.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyMsg.setVisibility(View.VISIBLE);
                }else{
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

                tabA.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.stub));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.fake_tab_textview));

                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear(); // this work to clear old item
                        String model = null;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            model = dataSnapshot.getKey();

                            if (dataSnapshot.getValue(String.class).equals("p"))
                            {
                                list.add(model);

                            }

                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        if (list.isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            emptyMsg.setVisibility(View.VISIBLE);
                        }else{
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
                tabA.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.fake_tab_textview));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceViewer_Activity.this, R.drawable.stub));
                root.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list.clear(); // this work to clear old item
                        String model = null;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            model = dataSnapshot.getKey();

                            if (dataSnapshot.getValue(String.class).equals("a"))
                            {
                                list.add(model);

                            }

                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        if (list.isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            emptyMsg.setVisibility(View.VISIBLE);
                        }else{
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
                                        Toast.makeText(AttendanceViewer_Activity.this, "Roll No Added Successfully", Toast.LENGTH_SHORT).show();

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


                final AlertDialog.Builder adder = new AlertDialog.Builder(AttendanceViewer_Activity.this);
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
                                        Toast.makeText(AttendanceViewer_Activity.this, "Roll No Added Successfully", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(AttendanceViewer_Activity.this, "Roll No removed successfully", Toast.LENGTH_SHORT).show();
    }
}
