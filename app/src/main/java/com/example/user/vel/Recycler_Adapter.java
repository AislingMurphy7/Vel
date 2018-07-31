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

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {

    private List<PartLogs> part_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    Recycler_Adapter(List<PartLogs> part_list){

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

        holder.setIsRecyclable(false);

        final String PostID = part_list.get(position).PartsPostID;
        final String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        String desc_data = part_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = part_list.get(position).getImage_url();
        String thumbUri = part_list.get(position).getImage_thumb();
        holder.setPartImage(image_url, thumbUri);

        String user_id = part_list.get(position).getUser_id();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");
                    holder.setUserData(userName, userImage);

                }
            }
        });

        long millisec = part_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date(millisec));
        holder.setTime(dateString);

        firebaseFirestore.collection("Parts/" + PostID + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty())
                {
                    int count = documentSnapshots.size();
                    holder.updateLikesCount(count);
                } else {
                    holder.updateLikesCount(0);
                }
            }
        });

        firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_accent_web));
                }else {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_gray_web));
                }
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists())
                        {
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).set(likesMap);
                        }else {
                            firebaseFirestore.collection("Parts/" + PostID + "/Likes").document(currentUserID).delete();
                        }
                    }
                });
            }
        });

        firebaseFirestore.collection("Parts/" + PostID + "/Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty())
                {
                    int count = documentSnapshots.size();
                    holder.updateCommentCount(count);
                } else {
                    holder.updateCommentCount(0);
                }
            }
        });

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
        private CircleImageView PostUserImage;

        private ImageView CommentBtn;
        private TextView commentCount;

        private ImageView likeBtn;
        private TextView likeCount;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            likeBtn = mView.findViewById(R.id.like_btn);
            CommentBtn = mView.findViewById(R.id.comment_icon);
        }

        void setDescText(String descText)
        {
            descView = mView.findViewById(R.id.post_desc);
            descView.setText(descText);
        }

        void setPartImage(String downloadUri, String thumbUri)
        {
            PartImageview = mView.findViewById(R.id.post_image);

            RequestOptions requestOptions = new RequestOptions();
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(PartImageview);
        }

        public void setTime(String Date)
        {
            PostDate = mView.findViewById(R.id.blog_date);
            PostDate.setText(Date);
        }

        public void setUserData(String name, String image)
        {
            PostUserName = mView.findViewById(R.id.user_name);
            PostUserImage = mView.findViewById(R.id.user_image);

            PostUserName.setText(name);

            RequestOptions placeOption = new RequestOptions();

            Glide.with(context).applyDefaultRequestOptions(placeOption).load(image).into(PostUserImage);

        }

        public void updateLikesCount(int count) {
            likeCount = mView.findViewById(R.id.like_count);
            likeCount.setText(count + " Comment");
        }

        public void updateCommentCount(int count) {
            commentCount = mView.findViewById(R.id.comment_count);
            commentCount.setText(count + " Comment");
        }
    }
}
