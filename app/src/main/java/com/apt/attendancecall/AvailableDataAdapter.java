package com.apt.attendancecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AvailableDataAdapter extends RecyclerView.Adapter<AvailableDataAdapter.AvailableViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<String> mList;
    Context context;

    public AvailableDataAdapter(Context context , ArrayList<String> mList, RecyclerViewInterface recyclerViewInterface){

        this.mList = mList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public AvailableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.date_list_text, parent ,false);
        return new AvailableViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableViewHolder holder, int position) {
        String model = mList.get(position);
        holder.available_Sub.setText(model.substring(0, 1).toUpperCase() + (model.substring(1)));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class AvailableViewHolder extends RecyclerView.ViewHolder{

        TextView available_Sub;

        public AvailableViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);


            available_Sub = itemView.findViewById(R.id.availabledate);

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
