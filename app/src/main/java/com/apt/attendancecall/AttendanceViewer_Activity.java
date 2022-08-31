package com.apt.attendancecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class AttendanceViewer_Activity extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;

    // For back button
    ImageView back_img, active_img;

    //For refresher
//    SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private MyAdapter adapter;
    private ArrayList<User> list;

    TextView subject_title, date_title;

    EncoderDecoder decoder = new EncoderDecoder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_viewer);



        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
//        SharedPreferences sharedPreferences_loginDetails = getSharedPreferences("login_details", MODE_PRIVATE);
//        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

        // For back button
        back_img = findViewById(R.id.viewer_back_img);


        // For back button
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
