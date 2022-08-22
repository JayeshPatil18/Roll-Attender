package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class AvailableSubjects_Activity extends AppCompatActivity implements RecyclerViewInterface{

    ShimmerFrameLayout shimmerFrameLayout;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    SubjectsAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    EncoderDecoder decoder = new EncoderDecoder();

    ImageView backIcon;

    String emailId, name;
    TextView availableTeacherId ,availableTeacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_subjects);

        shimmerFrameLayout = findViewById(R.id.shimmer_list_available_sub);
        shimmerFrameLayout.startShimmer();

        backIcon = findViewById(R.id.available_subject_back_img);

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
//        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
//        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        availableTeacherId = findViewById(R.id.availableTeacherId);
        availableTeacherName = findViewById(R.id.availableTeacherName);

        emailId = getIntent().getStringExtra("available_teacher");

        availableTeacherId.setText(decoder.decodeUserEmail(emailId));

        root = db.getReference().child("admin_users").child(emailId).child("details");
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminInfo adminInfo = snapshot.getValue(AdminInfo.class);
                name = adminInfo.getName();
                availableTeacherName.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // For if device user don't logged in
        SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
        if (!sharedPreferences_isLogin.getBoolean("hasLoggedIn",false)){
            Toast.makeText(this, "Please login account", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AvailableSubjects_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        root = db.getReference().child("admin_users").child(emailId).child("subjects");

        recyclerView = findViewById(R.id.available_sub_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new SubjectsAdapter(this ,list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // this work to clear old item
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String model = dataSnapshot.getKey();
                    list.add(model);
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

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(AvailableSubjects_Activity.this, AvailableDate_Activity.class);
        intent.putExtra("available_teacher",emailId);
        intent.putExtra("available_subject",list.get(position).toString().toLowerCase(Locale.ROOT));
        startActivity(intent);
    }
}