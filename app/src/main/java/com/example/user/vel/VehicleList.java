package com.example.user.vel;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/*
    This class handles the 'vehicles' of the app.
    This class displays all the vehicles with data
    posted by other users.
    The user can add vehicles via this page as well
 */

public class VehicleList extends AppCompatActivity
{
    //Declaring RecyclerView
    private RecyclerView vehicleList;
    //Declaring DatabaseReference to FireBase
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_vehicle_list);

        //Linking to the FireBase Real-time Database
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Vehicles");

        //XML variable
        vehicleList = findViewById(R.id.vehicle_list_view);
        vehicleList.setHasFixedSize(true);
        vehicleList.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton addVehicleBtn = findViewById(R.id.add_vehicle_btn);

        //If the user taps the addPostBtn
        addVehicleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //They will be redirected to the 'NewPost' class where they can add a new post
                Intent intent = new Intent(VehicleList.this, NewVehicle.class);
                startActivity(intent);
            }//End onClick()
        });//End addVehicleBtn()
    }//End onCreate()

    @Override
    protected void onStart()
    {
        super.onStart();

        //Recycler Adapter for Vehicles
        FirebaseRecyclerAdapter<VehicleLog, VehicleViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<VehicleLog, VehicleViewHolder>(
                VehicleLog.class,
                R.layout.vehicle_itemlist,
                VehicleViewHolder.class,
                databaseRef
        ){
            @Override
            protected void populateViewHolder(VehicleViewHolder viewHolder, VehicleLog model, int position)
            {
                //Get the unique identifier for each record in the database
                final String vehicle_key = getRef(position).getKey();
                Log.d(vehicle_key, "VEHICLE LIST");

                //Gathers the data
                viewHolder.setMakeText(model.getMake());
                viewHolder.setModelText(model.getModel());
                viewHolder.setRegText(model.getReg());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                //If a record is tapped on
                viewHolder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //Users redirected to the next screen
                        Intent intent = new Intent(VehicleList.this, PasswordProtected.class);
                        intent.putExtra("Vehicle_id", vehicle_key);
                        startActivity(intent);
                    }//End onClick()
                });//End OnClickListener()
            }//End populateViewHolder()
        };//End FirebaseRecyclerAdapter()

        vehicleList.setAdapter(firebaseRecyclerAdapter);

    }//End onStart()

    public static class VehicleViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public VehicleViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }//End VehicleViewHolder()

        //Sets the vehicles make on screen
        public void setMakeText(String make)
        {
            TextView vehicle_make = mView.findViewById(R.id.vehicle_make);
            vehicle_make.setText(make);
        }//End setMakeText()

        //Sets the vehicles model on screen
        public void setModelText(String model)
        {
            TextView vehicle_model = mView.findViewById(R.id.vehicle_model);
            vehicle_model.setText(model);
        }//End setModelText()

        //Sets the vehicles reg on screen
        public void setRegText(String reg)
        {
            TextView vehicle_reg = mView.findViewById(R.id.vehicle_reg);
            vehicle_reg.setText(reg);
        }//End setRegText()

        //Sets the vehicles image on screen using Glide
        public void setImage(Context ctx, String Image)
        {
            ImageView vehicle_image = mView.findViewById(R.id.post_image);
            Glide.with(ctx).load(Image).into(vehicle_image);
        }//End setImage()
    }//End VehicleViewHolder()

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }//End onCreateOptionsMenu()

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(VehicleList.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(VehicleList.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //The user is already located within this screen
        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(VehicleList.this, UserProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End VehicleList()
