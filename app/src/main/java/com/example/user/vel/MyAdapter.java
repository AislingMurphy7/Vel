package com.example.user.vel;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;


public class MyAdapter extends FirebaseRecyclerAdapter<MyAdapter.ViewHolder, MyDataset> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewAge;

        public ViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.textview_name);
            textViewAge = (TextView) view.findViewById(R.id.textview_age);
        }
    }

    public MyAdapter(com.google.firebase.database.Query query, @Nullable ArrayList<MyDataset> items,
                     @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        MyDataset item = getItem(position);
        holder.textViewName.setText(item.getmake());
        holder.textViewAge.setText(String.valueOf(item.getmodel()));
    }

    protected void itemAdded(MyDataset item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    protected void itemChanged(MyDataset oldItem, MyDataset newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    protected void itemRemoved(MyDataset item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    protected void itemMoved(MyDataset item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}

