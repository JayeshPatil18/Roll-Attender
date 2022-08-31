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

public class RequestSentAdapter  extends RecyclerView.Adapter<RequestSentAdapter.RequestSentViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;

    ArrayList<String> mList;
    Context context;

    public RequestSentAdapter(Context context , ArrayList<String> mList, RecyclerViewInterface recyclerViewInterface) {
        this.mList = mList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RequestSentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_sent_list, parent ,false);
        return new RequestSentViewHolder(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestSentViewHolder holder, int position) {
        String model = mList.get(position);
        holder.Sub.setText(model);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RequestSentViewHolder extends RecyclerView.ViewHolder {

        TextView Sub;
        ImageView removeIcon;

        public RequestSentViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            Sub = itemView.findViewById(R.id.requestSentList_text);
            removeIcon = itemView.findViewById(R.id.requestRemoveIcon);

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
