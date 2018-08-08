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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Vehicle_adapter extends RecyclerView.Adapter<Vehicle_adapter.ViewHolder>
{
    //List variable
    private List<VehicleLog> vehicle_list;
    public Context context;

    //FireBase variables
    private FirebaseFirestore firebaseFirestore;

    Vehicle_adapter(List<VehicleLog> vehicle_list)
    {
        this.vehicle_list = vehicle_list;
    }//End Vehicle_adapter()

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_itemlist, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new Vehicle_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Vehicle_adapter.ViewHolder holder, int position)
    {

        holder.setIsRecyclable(false);

        String make_data = vehicle_list.get(position).getMake();
        holder.setMakeText(make_data);

        String model_data = vehicle_list.get(position).getModel();
        holder.setModelText(model_data);

        String reg_data = vehicle_list.get(position).getReg();
        holder.setRegText(reg_data);

        String image_url = vehicle_list.get(position).getImage_url();
        String thumbUri = vehicle_list.get(position).getImage_thumb();
        holder.setVehicleImage(image_url, thumbUri);

        String user_id = vehicle_list.get(position).getUser_id();
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
    }

    @Override
    public int getItemCount() {
        return vehicle_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        //XML variables
        private TextView makeView;
        private TextView modelView;
        private TextView regView;
        private ImageView VehicleImageview;
        private TextView PostUserName;
        private CircleImageView PostUserImage;

        ViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;

        }//End ViewHolder()

        //Sets the make
        void setMakeText(String makeText)
        {
            //XML variable
            makeView = mView.findViewById(R.id.vehicle_make);
            makeView.setText(makeText);
        }//End setDescText()

        //Sets the make
        void setModelText(String modelText)
        {
            //XML variable
            modelView = mView.findViewById(R.id.vehicle_model);
            modelView.setText(modelText);
        }//End setDescText()

        //Sets the make
        void setRegText(String regText)
        {
            //XML variable
            regView = mView.findViewById(R.id.vehicle_reg);
            regView.setText(regText);
        }//End setDescText()

        //Sets vehicle image
        void setVehicleImage(String downloadUri, String thumbUri)
        {
            //XML variable
            VehicleImageview = mView.findViewById(R.id.post_image);

            RequestOptions requestOptions = new RequestOptions();
            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(VehicleImageview);
        }//End setPartImage()


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

    }//End ViewHolder()
}
