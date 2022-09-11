package com.apt.attendancecall;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterForRollList extends RecyclerView.Adapter<AdapterForRollList.AdapterForTeacherViewHolder> {
    private final RecyclerViewRollList recyclerViewInterface;

    ArrayList<String> mList;
    Context context;

    public AdapterForRollList(Context context , ArrayList<String> mList, RecyclerViewRollList recyclerViewInterface) {
        this.mList = mList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AdapterForRollList.AdapterForTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.for_roll_list, parent ,false);
        return new AdapterForTeacherViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForRollList.AdapterForTeacherViewHolder holder, int position) {
        String model = mList.get(position);
        holder.Sub.setText(model);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdapterForTeacherViewHolder extends RecyclerView.ViewHolder {
        TextView Sub;
        ImageView removeIcon;
        LinearLayout item;

        public AdapterForTeacherViewHolder(View itemView, RecyclerViewRollList recyclerViewInterface) {
            super(itemView);
            Sub = itemView.findViewById(R.id.forRollList_text);
            removeIcon = itemView.findViewById(R.id.forRollListRemoveIcon);
            item = itemView.findViewById(R.id.rollListItem);

            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickRemove(pos);
                        }
                    }
                }
            });

            item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(Sub.getText().toString());
                        }
                    }
                    return false;
                }
            });
        }
    }
}
