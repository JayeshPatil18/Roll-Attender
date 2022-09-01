package com.apt.attendancecall;

import static android.content.Context.MODE_PRIVATE;

import static com.apt.attendancecall.InfoForFrag.getStrDate;
import static com.apt.attendancecall.InfoForFrag.getStrSubject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AbsentActivity extends Fragment implements RecyclerViewInterfaceTeacher {
    EncoderDecoder decoder = new EncoderDecoder();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;

    private RecyclerView recyclerView;
    ArrayList<String> list;
    AdapterForTeacher adapter;

    String strSub;
    String strDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.absent, container, false);

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_viewerA);
        shimmerFrameLayout.startShimmer();

        strSub = getStrSubject();
        strDate = getStrDate();

        ///////////////////////////////////////////////////////////////////////////////////////////
        // For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getActivity().getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");
        /////////////////////////////////////////////////////////////////////////////////////////////

        recyclerView = view.findViewById(R.id.A_roll_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        adapter = new AdapterForTeacher(getActivity(), list, this);

        recyclerView.setAdapter(adapter);

        // For item divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));

        root = db.getReference().child("admin_users").child(decoder.encodeUserEmail(emailId)).child("subjects").child(strSub).child(strDate);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); // this work to clear old item
                String model = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    model = dataSnapshot.getKey();

                    if (dataSnapshot.getValue(String.class).equals("a")) {
                        list.add(model);
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

        return inflater.inflate(R.layout.present, container, false);
    }

    @Override
    public void onItemClickTeacher(int position) {

    }

    @Override
    public void onItemClick(int position) {
// For retrieving admin user name
        SharedPreferences sharedPreferences_loginDetails = getContext().getSharedPreferences("login_details", MODE_PRIVATE);
        String emailId = sharedPreferences_loginDetails.getString("email_id", "null");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String itemText = list.get(position).toString().toLowerCase(Locale.ROOT);
        String userEmail = decoder.encodeUserEmail(emailId);

        database.getReference("admin_users").child(userEmail).child("subjects").child(strSub).child(strDate).child(itemText).removeValue();
    }
}
