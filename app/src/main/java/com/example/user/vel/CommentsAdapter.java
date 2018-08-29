package com.example.user.vel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/*
    This class looks after the recyclerView for the comments.
    in this class data is written and retrieved from FireBase
    as well as displayed in the application for the user to view
    using ViewHolder
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>
{

    //List to hold comments
    private List<Comments> commentsList;
    public Context context;

    //FireBase Variable
    private FirebaseFirestore firebaseFirestore;

    //Class receives List from Comments class
    CommentsAdapter(List<Comments> commentsList)
    {
        //Variable is set to what was received from Comments class
        this.commentsList = commentsList;
    }//End CommentsAdapter()

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_itemlist, parent, false);
        context = parent.getContext();

        //Get FireBase reference
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new CommentsAdapter.ViewHolder(view);
    }//End onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull final CommentsAdapter.ViewHolder holder, int position)
    {
        holder.setIsRecyclable(false);

        //Receive and set the commentMessage
        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);

        //Receive the user data from FireBase
        String user_id = commentsList.get(position).getUser_id();
        Log.d("USER ID FOR COMMENTS: ", user_id);

        //Retrieves data from FireStore
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                //If the process is successful
                if(task.isSuccessful())
                {
                    //Gathers data from FireBase and sets within the app
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.setUserData(userName, userImage);
                }//End if()
            }//End onComplete()
        });//End OnCompleteListener()
    }//End onBindViewHolder()

    @Override
    //Retrieves the amount of items
    public int getItemCount()
    {
        //If not null, return the size
        if(commentsList != null)
        {
            return commentsList.size();
        }//End if()
        //If null, return 0
        else {
            return 0;
        }//End else()
    }//End getItemCount()

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //Declare the view
        private View mView;

        //XML variables
        private TextView CommentUsername;
        private CircleImageView CommentImage;
        private TextView comment_message;

        ViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }//End ViewHolder()

        //Sets the comments
        public void setComment_message(String message)
        {
            //XML variable
            comment_message = mView.findViewById(R.id.comment_message);
            //Sets the comment message
            comment_message.setText(message);
        }//End setComment_message()

        //Sets the username and user image who posted the comment
        public void setUserData(String userName, String userImage)
        {
            //XML variables
            CommentUsername = mView.findViewById(R.id.comment_username);
            CommentImage = mView.findViewById(R.id.comment_image);

            //Sets username
            CommentUsername.setText(userName);

            //Sets the placeholder image
            RequestOptions placeOption = new RequestOptions();
            //Loads the images and placeholder from FireBase into the page
            Glide.with(context).applyDefaultRequestOptions(placeOption).load(userImage).into(CommentImage);
        }//End setUserData()
    }//End ViewHolder()
}//End CommentsAdapter()
