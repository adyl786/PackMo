package com.android.testappppp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    @NonNull
    ArrayList<OldBookingModel> arrayList;
    Context context;
    public MyListAdapter(@NonNull ArrayList<OldBookingModel> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View list=inflater.inflate(R.layout.booked_details,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(list);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        OldBookingModel model=arrayList.get(i);
        viewHolder.T_name.setText(model.getName());
        viewHolder.T_address.setText(model.getAddress());
        viewHolder.T_capacity.setText(model.getCapacity());
        viewHolder.T_date.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView T_name,T_address,T_capacity,T_date;
        CardView cardView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            cardView=itemView.findViewById(R.id.CardId);
            T_name=itemView.findViewById(R.id.name_ID);
            T_address=itemView.findViewById(R.id.address_ID);
            T_capacity=itemView.findViewById(R.id.capacity_ID);
            T_date=itemView.findViewById(R.id.date_ID);

        }
    }
}
