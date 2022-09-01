package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class AvailableDate_Activity extends AppCompatActivity implements RecyclerViewInterface {

    ShimmerFrameLayout shimmerFrameLayout;

    // For back button
    ImageView back_img;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    AvailableDataAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    TextView subject_title;

    EncoderDecoder decoder = new EncoderDecoder();

    String emailId;
    String available_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_date);

        shimmerFrameLayout = findViewById(R.id.shimmer_list_plain_available_date);
        shimmerFrameLayout.startShimmer();

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        emailId = getIntent().getStringExtra("available_teacher");
        available_subject = getIntent().getStringExtra("available_subject").toLowerCase(Locale.ROOT);
        /////////////////////////////////////////////////////////////////////////////////////////////

        // For back button
        back_img = findViewById(R.id.available_date_back_img);

        subject_title = findViewById(R.id.available_subject_title);
        subject_title.setText(available_subject.substring(0, 1).toUpperCase() + available_subject.substring(1));

        root = db.getReference().child("admin_users").child(emailId).child("subjects").child(available_subject);

        recyclerView = findViewById(R.id.available_date_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new AvailableDataAdapter(this, list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); // this work to clear old item
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String model = dataSnapshot.getKey();
                    list.add(0, model.replace("-", " / "));
                }

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public void onItemClick(int position) {
        Intent intent = new Intent(AvailableDate_Activity.this, AttendanceVIewStudent.class);
        intent.putExtra("available_emailId", emailId);
        intent.putExtra("available_subject_for_date", available_subject);
        intent.putExtra("available_date_of_subject", list.get(position).toString());
        startActivity(intent);
    }
}