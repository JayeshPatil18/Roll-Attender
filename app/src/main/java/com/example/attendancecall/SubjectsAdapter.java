package com.example.attendancecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<String> mList;
    Context context;

    public SubjectsAdapter(Context context , ArrayList<String> mList, RecyclerViewInterface recyclerViewInterface){

        this.mList = mList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SubjectsAdapter.SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sub_list_text, parent ,false);
        return new SubjectsViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsAdapter.SubjectsViewHolder holder, int position) {
        String model = mList.get(position);
        holder.Sub.setText(model.substring(0, 1).toUpperCase() + (model.substring(1)));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class SubjectsViewHolder extends RecyclerView.ViewHolder{

        TextView Sub;

        public SubjectsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);


            Sub = itemView.findViewById(R.id.availableSub);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
