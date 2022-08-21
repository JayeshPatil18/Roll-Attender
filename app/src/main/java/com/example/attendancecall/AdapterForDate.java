package com.example.attendancecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterForDate extends RecyclerView.Adapter<AdapterForDate.AdapterForDateViewHolder> {
    private final RecyclerViewInterfaceTeacher recyclerViewInterface;

    ArrayList<String> mList;
    Context context;

    public AdapterForDate(Context context , ArrayList<String> mList, RecyclerViewInterfaceTeacher recyclerViewInterface) {
        this.mList = mList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AdapterForDate.AdapterForDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.for_date_remove, parent ,false);
        return new AdapterForDateViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForDate.AdapterForDateViewHolder holder, int position) {
        String model = mList.get(position);
        holder.Sub.setText(model);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdapterForDateViewHolder extends RecyclerView.ViewHolder {
        TextView Sub;
        ImageView removeIcon;

        public AdapterForDateViewHolder(View itemView, RecyclerViewInterfaceTeacher recyclerViewInterface) {
            super(itemView);
            Sub = itemView.findViewById(R.id.forDateList_text);
            removeIcon = itemView.findViewById(R.id.forDateListRemoveIcon);

            Sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickTeacher(pos);
                        }
                    }
                }
            });

            removeIcon.setOnClickListener(new View.OnClickListener() {
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
