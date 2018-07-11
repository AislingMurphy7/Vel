package com.example.user.vel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {

    public List<PartLogs> part_list;
    public Context context;

    public Recycler_Adapter(List<PartLogs> part_list){

        this.part_list = part_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_list_item, parent, false);

        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter.ViewHolder holder, int position) {

        String desc_data = part_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = part_list.get(position).getImage_url();
        holder.setPartImage(image_url);

        String user_id = part_list.get(position).getUser_id();

        long millisec = part_list.get(position).getTimestamp().getTime();
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(millisec));
        holder.setTime(dateString);
    }

    @Override
    public int getItemCount() {
        return part_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView descView;
        private ImageView PartImageview;
        private TextView PostDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescText(String descText)
        {
            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);
        }

        public void setPartImage (String downloadUri)
        {
            PartImageview = mView.findViewById(R.id.blog_image);
            Glide.with(context).load(downloadUri).into(PartImageview);
        }

        public void setTime(String Date)
        {
            PostDate = mView.findViewById(R.id.blog_date);
            PostDate.setText(Date);
        }
    }
}
