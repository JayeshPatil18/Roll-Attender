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

public class RequestReceived extends AppCompatActivity implements RecyclerViewInterface {

    ShimmerFrameLayout shimmerFrameLayout;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    RequestReceivedAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    ImageView backImg;

    EncoderDecoder decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_received);

        shimmerFrameLayout = findViewById(R.id.shimmer_list_received);
        shimmerFrameLayout.startShimmer();

        backImg = findViewById(R.id.requestReceived_back_img);

        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        decoder = new EncoderDecoder();

        root = db.getReference().child("admin_users");

        recyclerView = findViewById(R.id.request_received_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new RequestReceivedAdapter(this, list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // this work to clear old item
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String model = dataSnapshot.getKey();

                    root = db.getReference().child("admin_users").child(model).child("request_to");
                    root.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                                String strRequest = dataSnapshot1.getKey();
                                String strRequestStatus = dataSnapshot1.getValue(String.class);

                                if (strRequest.equals(decoder.encodeUserEmail(emailId))){

                                    if (!strRequestStatus.equals("true")){
                                        list.add(decoder.decodeUserEmail(model));
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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
        database.getReference("admin_users").child(decoder.encodeUserEmail(list.get(position).toString())).child("request_to").child(decoder.encodeUserEmail(emailId)).setValue("true");

        Toast.makeText(this, "Request is accepted successfully", Toast.LENGTH_SHORT).show();
    }
}
