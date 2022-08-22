package com.example.attendancecall;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AttendanceViewer_Activity extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;

    // For back button
    ImageView back_img, active_img;

    //For refresher
//    SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private MyAdapter adapter;
    private ArrayList<User> list;

    TextView subject_title, date_title;

    EncoderDecoder decoder = new EncoderDecoder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_viewer);

        shimmerFrameLayout = findViewById(R.id.shimmer_viewer);
        shimmerFrameLayout.startShimmer();

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

        // For back button
        back_img = findViewById(R.id.viewer_back_img);

        active_img = findViewById(R.id.viewer_activate_img);

        //For refresher id
//        refreshLayout = findViewById(R.id.refresher);

        subject_title = findViewById(R.id.subject_title);
        date_title = findViewById(R.id.date_title);

        String subject_name = getIntent().getStringExtra("subject_for_date").toLowerCase(Locale.ROOT);
        String subject_date = getIntent().getStringExtra("date_of_subject").replace(" / ","-");

        subject_title.setText(subject_name.substring(0, 1).toUpperCase() + subject_name.substring(1));
        date_title.setText(subject_date.replace("-"," / "));


        ///////////////////////////////////////////////////////////////////////////////////////////////////////


    // For activating attendance

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_name).child(subject_date).child("status");
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isActive = snapshot.getValue(String.class);
                if (isActive.equals("active")){
                    active_img.setImageResource(R.drawable.disallow_to_add_attendance);
                    Toast.makeText(AttendanceViewer_Activity.this, "Activate, Student allow to give attendance", Toast.LENGTH_SHORT).show();
                }else {
                    active_img.setImageResource(R.drawable.allow_to_add_attendance);
                    Toast.makeText(AttendanceViewer_Activity.this, "Inactivate, Student  disallow to give attendance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            active_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_name).child(subject_date).child("status");
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String isActive = snapshot.getValue(String.class);
                            if (isActive.equals("active")){
                                root.setValue("inactive");
                                active_img.setImageResource(R.drawable.allow_to_add_attendance);
                                Toast.makeText(AttendanceViewer_Activity.this, "Inactivated, Student disallow to give attendance", Toast.LENGTH_SHORT).show();
                            }else {
                                root.setValue("active");
                                active_img.setImageResource(R.drawable.disallow_to_add_attendance);
                                Toast.makeText(AttendanceViewer_Activity.this, "Activated, Student allow to give attendance", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////


        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_name).child(subject_date);

        recyclerView = findViewById(R.id.student_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new MyAdapter(this ,list);

        recyclerView.setAdapter(adapter);


        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); // this work to clear old item
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (!dataSnapshot.getKey().equals("status")){
                        User model = dataSnapshot.getValue(User.class);


                        list.add(model);
                    }
                }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // For refresher
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                root.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        list.clear(); // this work to clear old item
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                            User model = dataSnapshot.getValue(User.class);
//                            list.add(model);
//                        }
//                        adapter.notifyDataSetChanged();
//                        refreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

        // For back button
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
