package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AttendanceViewer_Activity extends AppCompatActivity {

    // For back button
    ImageView back_img;

    //For refresher
//    SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private MyAdapter adapter;
    private ArrayList<User> list;

    TextView subject_title, date_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_viewer);

        // For back button
        back_img = findViewById(R.id.viewer_back_img);

        //For refresher id
//        refreshLayout = findViewById(R.id.refresher);

        subject_title = findViewById(R.id.subject_title);
        date_title = findViewById(R.id.date_title);

        String subject_name = getIntent().getStringExtra("subject_for_date").toLowerCase(Locale.ROOT);
        String subject_date = getIntent().getStringExtra("date_of_subject").replace(" / ","-");

        subject_title.setText(subject_name.substring(0, 1).toUpperCase() + subject_name.substring(1));
        date_title.setText(subject_date.replace("-"," / "));

        root = db.getReference().child("subjects").child(subject_name).child(subject_date);

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
                    User model = dataSnapshot.getValue(User.class);
                    list.add(model);
                }
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
