package com.example.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity implements RecyclerViewInterface, RecyclerViewInterfaceTeacher{

    ShimmerFrameLayout shimmerFrameLayout;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    StudentAdapter adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    private DatabaseReference root_isUserExist;

    EncoderDecoder decoder;

    TextView sectionName;

    ImageView menu_icon;


    // For requesting with fab button
    FloatingActionButton AddRequestFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        shimmerFrameLayout = findViewById(R.id.shimmer_list_student);
        shimmerFrameLayout.startShimmer();

        FirebaseAuth isLoginfAuth = FirebaseAuth.getInstance();
        FirebaseUser isLoginfUser = isLoginfAuth.getCurrentUser();

        if (isLoginfUser == null){
            Intent intent = new Intent(StudentActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


        sectionName = findViewById(R.id.sectionStudent);

        menu_icon = findViewById(R.id.student_menu_img);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardDialogBox dialogBox = new DashBoardDialogBox();
                dialogBox.showDialog(StudentActivity.this, sectionName);
            }
        });

        // For if device user don't logged in
        SharedPreferences sharedPreferences_isLogin = getSharedPreferences("MyPrefsLogin",MODE_PRIVATE);
        if (!sharedPreferences_isLogin.getBoolean("hasLoggedIn",false)){
            Toast.makeText(this, "Please login account", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StudentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

//        request_adminName = findViewById(R.id.admin_name_request);

        // For starting teacher activity after exiting app
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details",MODE_PRIVATE);
        SharedPreferences.Editor editor_loginDetails = sharedPreferences_loginDetails.edit();

        editor_loginDetails.putString("role","student");
        editor_loginDetails.commit();

        // For retrieving admin user name
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        decoder = new EncoderDecoder();


        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("request_to");

        recyclerView = findViewById(R.id.teacher_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new StudentAdapter(this, list, this);

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
                    if (modelStatus.equals("true")){


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


        // For requesting with fab button
        AddRequestFab = findViewById(R.id.request_fab);

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(sharedPreferences_loginDetails.getString("email_id","null"))).child("request_to");

        AddRequestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder adder = new AlertDialog.Builder(StudentActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.request_dialogbox,null);

                final EditText email = (EditText)view1.findViewById(R.id.request_email_dlbox);
                Button btn_add = (Button)view1.findViewById(R.id.request_dlbox_btn);
                Button btn_cancel = (Button)view1.findViewById(R.id.reqCancel_dlbox_btn);
                // For validation error
                TextView invalidDisplay = (TextView)view1.findViewById(R.id.requestAdder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(true); // For dismiss if user click outside dialog box

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                                invalidDisplay.setText("");

                                String emailRequest = email.getText().toString().trim();

                                if(emailRequest.isEmpty()){
                                    email.setError("* Email field is empty");
                                }else{
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedEmailIdForRequest(emailRequest, invalidDisplay);

                                    if (validSubName){


                                        EncoderDecoder encoderDecoder = new EncoderDecoder();
                                        String finalEmailRequest = encoderDecoder.encodeUserEmail(emailRequest);

                                        final boolean[] isUserExist = {false};

                                        root_isUserExist = db.getReference().child("admin_users");
                                        root_isUserExist.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                    String model = dataSnapshot.getKey();

                                                    if(model.equals(finalEmailRequest)) {
                                                        isUserExist[0] = true;
                                                    }
                                                }

                                                root.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (isUserExist[0]){

                                                            boolean isRequestSent = false;

                                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                                String model = dataSnapshot.getKey();

                                                                if(model.equals(finalEmailRequest)) {
                                                                    isRequestSent = true;
                                                                }
                                                            }

                                                            if(!isRequestSent){
                                                                if (decoder.decodeUserEmail(finalEmailRequest).equals(sharedPreferences_loginDetails.getString("email_id","null"))){
                                                                    invalidDisplay.setText("* You should not send request to your self!");
                                                                }else {
                                                                    email.setText("");
                                                                    root.child(finalEmailRequest.toString()).setValue("null");
                                                                    Toast.makeText(StudentActivity.this, "Request sent Successfully to " + emailRequest, Toast.LENGTH_SHORT).show();

                                                                    alertDialog.dismiss();
                                                                }
                                                            }else{
                                                                invalidDisplay.setText("* Request already sent");
                                                            }
                                                        }else{
                                                            invalidDisplay.setText("* User not exist or User not created account using this email");
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });

                                    }
                                }

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

        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String itemText = decoder.encodeUserEmail(list.get(position).toString());
        String userEmail = decoder.encodeUserEmail(emailId);

        removeTeacherPopUpDialogBox(StudentActivity.this, database, itemText, userEmail, "Are you sure want to remove Teacher?");

    }

    @Override
    public void onItemClickTeacher(int position) {
        Intent intent = new Intent(StudentActivity.this, AvailableSubjects_Activity.class);
        intent.putExtra("available_teacher",decoder.encodeUserEmail(list.get(position).toString()));
        startActivity(intent);
    }

    private void removeTeacherPopUpDialogBox(StudentActivity activity, FirebaseDatabase database, String itemText, String userEmail, String s) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.remove_node_dialogobox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textView = (TextView) dialog.findViewById(R.id.removeErrorText);
        textView.setText(s);

        Button btn_ok = (Button) dialog.findViewById(R.id.remove_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.remove_cancel);

        dialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.getReference("admin_users").child(userEmail).child("request_to").child(itemText).removeValue();

                Toast.makeText(activity, "Teacher removed successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}