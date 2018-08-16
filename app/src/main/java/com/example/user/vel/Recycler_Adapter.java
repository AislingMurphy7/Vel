package com.example.user.vel;

/*
    This class looks after the recyclerView for the posts.
    in this class data is written and retrieved from FireBase
    as well as displayed in the application for the user to view
    using ViewHolder
 */

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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder>
{
    //List variable
    private List<PostLog> part_list;
    public Context context;

    //FireBase variables
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    //Class receives List from PostLog class
    Recycler_Adapter(List<PostLog> part_list)
    {
        //Variable is set to what was received from PostLog class
        this.part_list = part_list;
    }//End Recycler_Adapter()

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_itemlist, parent, false);
        context = parent.getContext();

        //Instantiate the FireBase variables
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }//End onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull final Recycler_Adapter.ViewHolder holder, int position)
    {
        holder.setIsRecyclable(false);

        //PartsPostID is retrieved
        final String PostID = part_list.get(position).PartsPostID;
        //Signed in User is retrieved
        final String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        //Receive and set the desc_data
        String desc_data = part_list.get(position).getDesc();
        holder.setDescText(desc_data);

        //Receive and set the image and thumbnail data
        String image_url = part_list.get(position).getImage_url();
        String thumbUri = part_list.get(position).getImage_thumb();
        holder.setPartImage(image_url, thumbUri);

        //Receive the user data from FireBase
        String user_id = part_list.get(position).getUser_id();
        //Retrieves data from FireStore
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            //When process is complete
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

        //Takes timestamp and converts to a long value of time
        long millisec = part_list.get(position).getTimestamp().getTime();
        //Date and time is re-formatted
        String dateString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(millisec));
        //Set the time on the page
        holder.setTime(dateString);

        //Accesses the collection in real-time so pages will change when liked/disliked
        firebaseFirestore.collection("Parts/" + PostID + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                //If not empty, there is likes
                if(!documentSnapshots.isEmpty())
                {
                    //Retrieves the number of likes
                    int count = documentSnapshots.size();
                    //Sets the count number
                    holder.updateLikesCount(count);
                }//End if()
                //There are no likes
                else {
                    //Sets the count to 0
                    holder.updateLikesCount(0);
                }//End else()
            }//End onEvent()
        });//End EventListener()

        //Accesses the collection in real-time so pages will change when liked/disliked
        firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e)
            {
                //If the like button is un-liked
                if(documentSnapshot.exists())
                {
                    //The image is set to grey heart
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_accent_web));
                }//End if()

                //If the like button is liked by the user
                else {
                    //The image is set to red heart
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_gray_web));
                }//End else()
            }//End onEvent()
        });//End EventListener()

        //If the like button is taped
        holder.likeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The collection within FireBase is accessed and the like/dislike is recorded
                firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    //When the process is complete
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        //If data does not exist
                        if(!task.getResult().exists())
                        {
                            //Create a Map with key: String, Value: Object
                            Map<String, Object> likesMap = new HashMap<>();
                            //Data is stored inside the HashMap()
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            //The collection within FireBase is accessed and like is set
                            firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).set(likesMap);
                        }//End if()
                        else {
                            //The collection within FireBase is accessed and like is deleted
                            firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).delete();
                        }//End else()
                    }//End onComplete()
                });//End OnCompleteListener()
            }//End onClick()
        });//End OnClickListener()

        //Accesses the collection in real-time when the posts are commented on
        firebaseFirestore.collection("Parts/" + PostID + "/Comments").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                //If not empty
                if(!documentSnapshots.isEmpty())
                {
                    //Number of comments is retrieved
                    int count = documentSnapshots.size();
                    //Set the number of comments
                    holder.updateCommentCount(count);
                }//End if()
                //If is empty
                else {
                    //Sets count to 0
                    holder.updateCommentCount(0);
                }//End else()
            }//End onEvent()
        });//End EventListener()

        //When the user taps the comment button
        holder.CommentBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the CommentsActivity
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("Post_ID", PostID);
                context.startActivity(intent);
            }//End onClick()
        });//End OnClickListener()
    }//End OnBindViewHolder()

    @Override
    //Retrieves the amount of items
    public int getItemCount()
    {
        return part_list.size();
    }//End getItemCount()

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //Declare the view
        private View mView;

        //XML variables
        private TextView descView;
        private ImageView PartImageview;
        private TextView PostDate;
        private TextView PostUserName;
        private CircleImageView PostUserImage;
        private ImageView CommentBtn;
        private TextView commentCount;
        private ImageView likeBtn;
        private TextView likeCount;

        ViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;

            //XML variables
            likeBtn = mView.findViewById(R.id.like_btn);
            CommentBtn = mView.findViewById(R.id.comment_icon);
        }//End ViewHolder()

        //Sets the posts description
        void setDescText(String descText)
        {
            //XML variable
            descView = mView.findViewById(R.id.post_desc);
            descView.setText(descText);
        }//End setDescText()

        //Sets part image
        void setPartImage(String downloadUri, String thumbUri)
        {
            //XML variable
            PartImageview = mView.findViewById(R.id.post_image);

            //Sets the placeholder image
            RequestOptions requestOptions = new RequestOptions();
            //Loads the images and placeholder from FireBase into the page
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(PartImageview);
        }//End setPartImage()

        //Sets the post time
        public void setTime(String Date)
        {
            //XML variable
            PostDate = mView.findViewById(R.id.blog_date);
            PostDate.setText(Date);
        }//End setTime()

        //Sets the username and image
        public void setUserData(String name, String image)
        {
            //XML variables
            PostUserName = mView.findViewById(R.id.user_name);
            PostUserImage = mView.findViewById(R.id.user_image);

            //Sets username
            PostUserName.setText(name);

            //Sets the placeholder image
            RequestOptions placeOption = new RequestOptions();

            //Loads the image and placeholder from FireBase into the page
            Glide.with(context).applyDefaultRequestOptions(placeOption).load(image).into(PostUserImage);
        }//End setUserData()

        //Updates the likes count on each post
        public void updateLikesCount(int count)
        {
            //XML variable
            likeCount = mView.findViewById(R.id.like_count);
            likeCount.setText(count + " Likes");
        }//End updateLikesCount()

        //Updates comment count on each post
        public void updateCommentCount(int count)
        {
            //XML variable
            commentCount = mView.findViewById(R.id.comment_count);
            commentCount.setText(count + " Comment");
        }//End updateCommentCount()
    }//End ViewHolder()
}//End Recycler_Adapter()
