package com.example.attendancecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<User> mList;
    Context context;

    public MyAdapter(Context context , ArrayList<User> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_item , parent ,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User model = mList.get(position);
        holder.name_tv.setText(model.getName().substring(0, 1).toUpperCase() + (model.getName().substring(1)));
        holder.endrollment_tv.setText(model.getEnrollment_no());
        holder.branch_tv.setText(model.getBranch().substring(0, 1).toUpperCase() + (model.getBranch().substring(1)));
        holder.phone_tv.setText(model.getPhone_no());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name_tv , endrollment_tv , branch_tv, phone_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_tv = itemView.findViewById(R.id.tv_stud_name);
            endrollment_tv = itemView.findViewById(R.id.tv_stud_endrollment);
            branch_tv = itemView.findViewById(R.id.tv_stud_branch);
            phone_tv = itemView.findViewById(R.id.tv_stud_phone);
        }
    }
}