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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AttendanceVIewStudent extends AppCompatActivity implements RecyclerViewInterface{

    String flag;

    InterstitialAd mInterstitialAd;

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

        flag = "present";

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-6829345224658071/6553268931", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });


        TextView emptyMsg = (TextView) findViewById(R.id.empty_view);

        TextView numOfP = (TextView) findViewById(R.id.numOfPresentView);
        TextView numOfA = (TextView) findViewById(R.id.numOfAbsentView);

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

                list.clear(); // this work to clear old item
                String model = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    model = dataSnapshot.getKey();

                    if (flag.equals("present")) {
                        if (dataSnapshot.getValue(String.class).equals("p")) {
                            list.add(model);
                            numOfP.setText(String.valueOf(list.size()));
                            numOfA.setText("");
                        }
                    } else if (flag.equals("absent")) {
                        if (dataSnapshot.getValue(String.class).equals("a")) {
                            list.add(model);
                            numOfA.setText(String.valueOf(list.size()));
                            numOfP.setText("");
                        }
                    }
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                if (list.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    emptyMsg.setVisibility(View.VISIBLE);
                }else{
                    emptyMsg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag = "present";

                tabP.setTextColor(getResources().getColor(R.color.white));
                tabA.setTextColor(getResources().getColor(R.color.app_default));
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
                                numOfP.setText(String.valueOf(list.size()));
                                numOfA.setText("");
                            }

                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        if (list.isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            emptyMsg.setVisibility(View.VISIBLE);
                        }else{
                            emptyMsg.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
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
                flag = "absent";

                tabA.setTextColor(getResources().getColor(R.color.white));
                tabP.setTextColor(getResources().getColor(R.color.app_default));
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
                                numOfA.setText(String.valueOf(list.size()));
                                numOfP.setText("");
                            }

                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        if (list.isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            emptyMsg.setVisibility(View.VISIBLE);
                        }else{
                            emptyMsg.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
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
    public void onItemClick(int position) {

    }

    @Override
    public void onBackPressed() {
        finish();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(AttendanceVIewStudent.this);
        } else {
        }
    }
}