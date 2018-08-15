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

    Recycler_Adapter(List<PostLog> part_list)
    {
        this.part_list = part_list;
    }//End Recycler_Adapter()

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_itemlist, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }//End onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull final Recycler_Adapter.ViewHolder holder, int position)
    {
        holder.setIsRecyclable(false);

        final String PostID = part_list.get(position).PartsPostID;
        final String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        String desc_data = part_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = part_list.get(position).getImage_url();
        String thumbUri = part_list.get(position).getImage_thumb();
        holder.setPartImage(image_url, thumbUri);

        String user_id = part_list.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.setUserData(userName, userImage);

                }//End if()
            }//End onComplete()
        });//End OnCompleteListener()

        long millisec = part_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(millisec));
        holder.setTime(dateString);

        firebaseFirestore.collection("Parts/" + PostID + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if(!documentSnapshots.isEmpty())
                {
                    int count = documentSnapshots.size();
                    holder.updateLikesCount(count);
                }//End if()
                else {
                    holder.updateLikesCount(0);
                }//End else()
            }//End onEvent()
        });//End EventListener()

        //Creates/Access the collection onf FireBase for the Likes
        firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e)
            {
                //If the like button is unliked
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
                        if(!task.getResult().exists())
                        {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).set(likesMap);
                        }//End if()
                        else {
                            firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).delete();
                        }//End else()
                    }//End onComplete()
                });//End OnCompleteListener()
            }//End onClick()
        });//End OnClickListener()

        firebaseFirestore.collection("Parts/" + PostID + "/Comments").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
            {
                if(!documentSnapshots.isEmpty())
                {
                    int count = documentSnapshots.size();
                    holder.updateCommentCount(count);
                }//End if()
                else {
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

            RequestOptions requestOptions = new RequestOptions();
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

            RequestOptions placeOption = new RequestOptions();

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
