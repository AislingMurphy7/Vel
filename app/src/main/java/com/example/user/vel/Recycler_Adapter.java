package com.example.user.vel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {

    public List<PartLogs> part_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    public Recycler_Adapter(List<PartLogs> part_list){

        this.part_list = part_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Recycler_Adapter.ViewHolder holder, int position) {

        final String PostID = part_list.get(position).PartsPostID;
        final String currentUserID = mAuth.getCurrentUser().getUid();

        String desc_data = part_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = part_list.get(position).getImage_url();
        holder.setPartImage(image_url);

        String user_id = part_list.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {

                    String userName = task.getResult().getString("name");
                    holder.setUserData(userName);

                }
            }
        });

        long millisec = part_list.get(position).getTimestamp().getTime();
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(millisec));
        holder.setTime(dateString);

        holder.CommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("Post_ID", PostID);
                context.startActivity(intent);
            }
        });
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
        private TextView PostUserName;

        private ImageView CommentBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            CommentBtn = mView.findViewById(R.id.blog_comment_icon);
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

        public void setUserData(String name)
        {
            PostUserName = mView.findViewById(R.id.blog_user_name);


            PostUserName.setText(name);

        }
    }
}
