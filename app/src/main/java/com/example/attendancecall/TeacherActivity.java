package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class TeacherActivity extends AppCompatActivity implements RecyclerViewInterface{

    // For manually adding date with fab button
    FloatingActionButton subAddFab;

    // For not scroll no creation of activity
    boolean isAdd = false;
    String subNameToAdd = null;

    // For subject adding bar
//    Button sub_add_btn;
//    EditText sub_name;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    SubjectsAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    TextView adminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        adminName = findViewById(R.id.admin_name);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // For if device user don't logged in
        SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
        if (!sharedPreferences_isLogin.getBoolean("hasLoggedIn",false)){
            Toast.makeText(this, "Please login account", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TeacherActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // For starting teacher activity after exiting app
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details",MODE_PRIVATE);
        SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

        editor_loginDetails.putString("role","teacher");
        editor_loginDetails.commit();

        // For retrieving admin user name
        SharedPreferences sharedPreferences_emailId = getSharedPreferences("login_details",MODE_PRIVATE);
        EncoderDecoder decoder = new EncoderDecoder();

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(sharedPreferences_emailId.getString("email_id","null"))).child("details");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminDetails str = snapshot.getValue(AdminDetails.class);
                adminName.setText(decoder.getFirstCharCapital(str.getName()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // For manually adding date with fab button
        subAddFab = findViewById(R.id.add_sub_fab);

        // For subject adding bar
//        sub_add_btn = findViewById(R.id.sub_add_btn);
//        sub_name = findViewById(R.id.sub_name);

        root = db.getReference().child("subjects");

        recyclerView = findViewById(R.id.subject_list);
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
                String model = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    model = dataSnapshot.getKey();
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                if(isAdd){
                    recyclerView.scrollToPosition(list.indexOf(subNameToAdd));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        subAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder adder = new AlertDialog.Builder(TeacherActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.manually_addsub_dilogbox,null);

                final EditText sub = (EditText)view1.findViewById(R.id.sub_name_dlbox);
                Button btn_add = (Button)view1.findViewById(R.id.sub_add_btn_dlbox);
                Button btn_cancel = (Button)view1.findViewById(R.id.sub_cancel_btn_dlbox);
                // For validation error
                TextView invalidDisplay = (TextView)view1.findViewById(R.id.subAdder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(true); // For dismiss if user click outside dialog box

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                invalidDisplay.setText("");

                                String subject = sub.getText().toString().toLowerCase(Locale.ROOT).trim();


                                if(subject.isEmpty()){
                                    sub.setError("* Subject field is empty");
                                }else{
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedSubject(subject, invalidDisplay);

                                    if (validSubName){
                                        sub.setError(null);

                                        // For not scroll no creation of activity
                                        subNameToAdd = subject;
                                        isAdd = true;

                                        boolean isSubExist = false;

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            String model = dataSnapshot.getKey();

                                            if(model.equals(subject)) {
                                                isSubExist = true;
                                            }
                                        }

                                        if(!isSubExist){
                                            sub.setText("");
                                            root.child(subject.toString()).setValue("subject");
                                            Toast.makeText(TeacherActivity.this, "Subject Added Successfully", Toast.LENGTH_SHORT).show();

                                            alertDialog.dismiss();
                                        }else{
                                            invalidDisplay.setText("* Subject already added");
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
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(TeacherActivity.this, AttendanceDate.class);
        intent.putExtra("subject",list.get(position).toString());
        startActivity(intent);
    }
}