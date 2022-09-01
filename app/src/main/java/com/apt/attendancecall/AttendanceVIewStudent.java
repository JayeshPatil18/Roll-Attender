package com.apt.attendancecall;

import static com.apt.attendancecall.InfoForFrag.getStrDate;
import static com.apt.attendancecall.InfoForFrag.getStrSubject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AttendanceVIewStudent extends AppCompatActivity implements RecyclerViewInterface{

    TextView tabP, tabA;

    EncoderDecoder decoder = new EncoderDecoder();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    SubjectsAdapter adapter;

    // For validation error
    TextView invalidDisplay;

    // For back button
    ImageView back_img;

    TextView avai_subject, avai_date;

    String emailId, availableSubject, availableDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_view_student);

        emailId = getIntent().getStringExtra("available_emailId");
        availableSubject = getIntent().getStringExtra("available_subject_for_date").toLowerCase(Locale.ROOT);
        availableDate = getIntent().getStringExtra("available_date_of_subject").replace(" / ","-");

        // For back button
        back_img = findViewById(R.id.giver_back_img);

        avai_subject = findViewById(R.id.available_sub_name);
        avai_date = findViewById(R.id.available_date_title);

        avai_subject.setText(availableSubject.substring(0, 1).toUpperCase() + availableSubject.substring(1));
        avai_date.setText(availableDate.replace("-"," / "));

        tabP = findViewById(R.id.FaketabP);
        tabA = findViewById(R.id.FaketabA);


        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_list_view_student);
        shimmerFrameLayout.startShimmer();

        recyclerView = findViewById(R.id.available_attendance_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new SubjectsAdapter(this ,list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(availableSubject).child(availableDate);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tabA.setBackground(ContextCompat.getDrawable(AttendanceVIewStudent.this, R.drawable.stub));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceVIewStudent.this, R.drawable.fake_tab_textview));

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
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tabA.setBackground(ContextCompat.getDrawable(AttendanceVIewStudent.this, R.drawable.stub));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceVIewStudent.this, R.drawable.fake_tab_textview));

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
                        recyclerView.setVisibility(View.VISIBLE);
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
                tabA.setBackground(ContextCompat.getDrawable(AttendanceVIewStudent.this, R.drawable.fake_tab_textview));
                tabP.setBackground(ContextCompat.getDrawable(AttendanceVIewStudent.this, R.drawable.stub));

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
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public void onItemClick(int position) {

    }
}