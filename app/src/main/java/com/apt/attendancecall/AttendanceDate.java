package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class AttendanceDate extends AppCompatActivity implements RecyclerViewInterfaceTeacher{

    ShimmerFrameLayout shimmerFrameLayout;

    // For manually adding date with fab button
    FloatingActionButton dateAddFab;

    // For back button
    ImageView back_img;

    // For date refresher
    SwipeRefreshLayout dateRefresh;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    AdapterForDate adapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    TextView subject_name;
    String subject_for_date;

    EncoderDecoder decoder = new EncoderDecoder();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_date);

        shimmerFrameLayout = findViewById(R.id.shimmer_list_date);
        shimmerFrameLayout.startShimmer();

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

        // For manually adding date with fab button
        dateAddFab = findViewById(R.id.add_date_fab);

        // For back button
        back_img = findViewById(R.id.date_back_img);

        // For date refresher
        dateRefresh = findViewById(R.id.date_refresher);

        subject_for_date = getIntent().getStringExtra("subject").toLowerCase(Locale.ROOT);


        subject_name = findViewById(R.id.subject_for_date);

        subject_name.setText(subject_for_date.substring(0, 1).toUpperCase() + subject_for_date.substring(1));

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_for_date);


        recyclerView = findViewById(R.id.date_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new AdapterForDate(this ,list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); // this work to clear old item
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String model = dataSnapshot.getKey();


                    list.add(0,model.replace("-"," / "));

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

        // For manually adding dates by teacher

        dateAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder adder = new AlertDialog.Builder(AttendanceDate.this);
                View view1 = getLayoutInflater().inflate(R.layout.manually_adddate_dilogbox,null);

                final EditText date = (EditText)view1.findViewById(R.id.date_name_dlbox);
                Button btn_add = (Button)view1.findViewById(R.id.date_add_btn_dlbox);
                Button btn_cancel = (Button)view1.findViewById(R.id.date_cancel_btn_dlbox);
                // For validation error
                TextView invalidDisplay = (TextView)view1.findViewById(R.id.dateAdder_error);

                adder.setView(view1);

                final AlertDialog alertDialog = adder.create();
                alertDialog.setCanceledOnTouchOutside(true); // For dismiss if user click outside dialog box

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime localDate = LocalDateTime.now();
                String defaultegDate = dtf.format(localDate);

                invalidDisplay.setText("* Date eg. " + defaultegDate);

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        invalidDisplay.setText("");

                        final String[] dateToAdd = {date.getText().toString().trim()};
                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                if(dateToAdd[0].isEmpty()){
                                    date.setError("* Date field is empty");
                                }else{
                                    ValidationOfInput validation = new ValidationOfInput();
                                    boolean validSubName = validation.validatedDate(dateToAdd[0], invalidDisplay);

                                    if (validSubName){
                                        date.setError(null);

                                        dateToAdd[0] = dateToAdd[0].replace("/","-");

                                        boolean isDateExist = false;

                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            String model = dataSnapshot.getKey();

                                            if(model.equals(dateToAdd[0])) {
                                                isDateExist = true;
                                            }
                                        }

                                        if(!isDateExist){
                                            date.setText("");
//                                            root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(subject_for_date);
                                            root.child(dateToAdd[0].toString()).setValue("date");
                                            Toast.makeText(AttendanceDate.this, "Date Added Successfully", Toast.LENGTH_SHORT).show();

                                            alertDialog.dismiss();
                                        }else{
                                            invalidDisplay.setText("Date already added");
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

        // For automatically adding dates every day
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean isDateExist = false;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime localDate = LocalDateTime.now();
                String str2 = dtf.format(localDate);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String model = dataSnapshot.getKey();

                    if(model.equals(str2)) {
                        isDateExist = true;
                    }
                }

                if(!isDateExist){
                    root.child(str2.toString()).setValue("date");
                    dateRefresh.setRefreshing(false);
                }else{
                    dateRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            dateRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            boolean isDateExist = false;
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDateTime localDate = LocalDateTime.now();
                            String str2 = dtf.format(localDate);

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                String model = dataSnapshot.getKey();

                                if(model.equals(str2)) {
                                    isDateExist = true;
                                }
                            }

                            if(!isDateExist){
                                root.child(str2.toString()).setValue("date");
                                dateRefresh.setRefreshing(false);
                            }else{
                                dateRefresh.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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
    public void onItemClickTeacher(int position) {
        Intent intent = new Intent(AttendanceDate.this, AttendanceViewTab.class);
        intent.putExtra("subject_for_date",subject_for_date);
        intent.putExtra("date_of_subject",list.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String itemText = list.get(position).toString().replace(" / ","-");
        String userEmail = decoder.encodeUserEmail(emailId);

        removeDatePopUpDialogBox(AttendanceDate.this, database, itemText, userEmail);

    }


    private void removeDatePopUpDialogBox(AttendanceDate activity, FirebaseDatabase database, String itemText, String userEmail) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.plain_logout_dilogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView textView = (TextView) dialog.findViewById(R.id.plainLogoutErrorText);
        textView.setText("All attendance history of " + itemText + " will be delete, Are you sure want to remove Date?");

        Button btn_ok = (Button) dialog.findViewById(R.id.plainLogout_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.plainLogout_cancel);

        dialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.getReference("admin_users").child(userEmail).child("subjects").child(subject_for_date).child(itemText).removeValue();

                Toast.makeText(activity, "Date removed successfully", Toast.LENGTH_SHORT).show();

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