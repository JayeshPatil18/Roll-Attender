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
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity implements RecyclerViewInterface{

    private RecyclerView recyclerView;
    ArrayList<String> list;
    SubjectsAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // For if device user don't logged in
        SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
        if (!sharedPreferences_isLogin.getBoolean("hasLoggedIn",false)){
            Toast.makeText(this, "Please login account", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StudentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // For starting student activity after exiting app
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details",MODE_PRIVATE);
        SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

        editor_loginDetails.putString("role","student");
        editor_loginDetails.commit();

        root = db.getReference().child("subjects");

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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(StudentActivity.this, AvailableDate_Activity.class);
        intent.putExtra("available_subject",list.get(position).toString());
        startActivity(intent);
    }
}