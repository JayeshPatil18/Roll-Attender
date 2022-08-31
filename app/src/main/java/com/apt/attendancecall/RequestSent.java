package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestSent extends AppCompatActivity implements RecyclerViewInterface {

    ShimmerFrameLayout shimmerFrameLayout;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    RequestSentAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    ImageView backImg;

    EncoderDecoder decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sent);

        shimmerFrameLayout = findViewById(R.id.shimmer_list_requestSent);
        shimmerFrameLayout.startShimmer();

        backImg = findViewById(R.id.requestSent_back_img);

        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        decoder = new EncoderDecoder();

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("request_to");

        recyclerView = findViewById(R.id.request_sent_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new RequestSentAdapter(this, list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // this work to clear old item
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String model = dataSnapshot.getKey();
                    String modelStatus = dataSnapshot.getValue(String.class);
                    if (!modelStatus.equals("true")){
                        list.add(decoder.decodeUserEmail(model));

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

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("admin_users").child(decoder.encodeUserEmail(emailId)).child("request_to").child(decoder.encodeUserEmail(list.get(position).toString())).removeValue();

        Toast.makeText(this, "Request is removed", Toast.LENGTH_SHORT).show();
    }
}