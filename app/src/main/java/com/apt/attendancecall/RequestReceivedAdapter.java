package com.apt.attendancecall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestReceivedAdapter extends RecyclerView.Adapter<RequestReceivedAdapter.RequestReceivedViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<String> mList;
    Context context;

    public RequestReceivedAdapter(Context context , ArrayList<String> mList, RecyclerViewInterface recyclerViewInterface) {
        this.mList = mList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RequestReceivedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_received_list, parent, false);
        return new RequestReceivedViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestReceivedViewHolder holder, int position) {
        String model = mList.get(position);
        holder.Sub.setText(model);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RequestReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView Sub;
        ImageView statusIcon;

        public RequestReceivedViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            Sub = itemView.findViewById(R.id.requestReceivedList_text);
            statusIcon = itemView.findViewById(R.id.requestStatusIcon);

            statusIcon.setOnClickListener(new View.OnClickListener() {
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
