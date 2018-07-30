package com.example.user.vel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private List<Comments> commentsList;
    public Context context;

    private FirebaseFirestore firebaseFirestore;

    CommentsAdapter(List<Comments> commentsList)
    {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_itemlist, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentsAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);


        String user_id = commentsList.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.setUserData(userName, userImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(commentsList != null){
            return commentsList.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;

        private TextView CommentUsername;
        private CircleImageView CommentImage;

        private TextView comment_message;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_message(String message)
        {
            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }


        public void setUserData(String userName, String userImage) {
            CommentUsername = mView.findViewById(R.id.comment_username);
            CommentImage = mView.findViewById(R.id.comment_image);

            CommentUsername.setText(userName);

            RequestOptions placeOption = new RequestOptions();

            Glide.with(context).applyDefaultRequestOptions(placeOption).load(userImage).into(CommentImage);
        }
    }
}
