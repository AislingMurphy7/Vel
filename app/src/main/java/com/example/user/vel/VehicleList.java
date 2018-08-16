package com.example.user.vel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
    This class handles the 'vehicles' of the app.
    This class displays all the vehicles with data
    posted by other users.
    The user can add vehicles via this page as well
 */

public class VehicleList extends AppCompatActivity
{
    //Declare FireBase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    //Declare a List and a Recycler_Adapter variable
    private List<VehicleLog> vehicle_list;
    private Vehicle_adapter recycler_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_vehicle_list);

        //Instantiate FireBase variables
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //XML variable
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

        //Declaring and instantiate a new ArrayList
        vehicle_list = new ArrayList<>();

        //XML variable
        RecyclerView vehicle_view = findViewById(R.id.vehicle_list_view);

        //Instantiate a new Recycler_Adapter with the ArrayList
        recycler_adapter = new Vehicle_adapter(vehicle_list);

        //Set the layout manager to Linear
        vehicle_view.setLayoutManager(new LinearLayoutManager(this));
        //Set the adapter
        vehicle_view.setAdapter(recycler_adapter);

        //If the current user is present in FireBase
        if(mAuth.getCurrentUser() != null)
        {
            FirebaseFirestore firebaseFirestore2 = FirebaseFirestore.getInstance();

            //The posts will be displayed in descending order depending on the timestamp attached
            Query firstquery = firebaseFirestore2.collection("Vehicles").orderBy("timestamp", Query.Direction.DESCENDING);

            firstquery.addSnapshotListener(new EventListener<QuerySnapshot>()
            {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
                {
                    //If there is changes made to the document
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges())
                    {
                        //If the data is added
                        if (doc.getType() == DocumentChange.Type.ADDED)
                        {
                            String VehicleID = doc.getDocument().getId();
                            //Document is gathered and passed to VehicleLog class with the VehicleID
                            VehicleLog vehicleLog = doc.getDocument().toObject(VehicleLog.class).withId(VehicleID);
                            //Adds the post to the vehicle_list
                            vehicle_list.add(vehicleLog);

                            //Changes when data is changed
                            recycler_adapter.notifyDataSetChanged();
                        }//End if()
                    }//End For()
                }//End onEvent()
            });//End EventListener()
        }//End if()
    }//End onCreate()

    @Override
    protected void onStart()
    {
        //When the class starts
        super.onStart();

        //The current user is retrieved from FireBase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //If there is no current user present
        if(currentUser == null)
        {
            //The user is sent to login
            sendToLogin();
        } //End if()

        //If the current user is present
        else {
            //The current user id is retrieved
            String current_user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            firebaseFirestore.collection("Users").document(current_user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    //If successful
                    if(task.isSuccessful())
                    {
                        if(!task.getResult().exists())
                        {
                            //User is redirected to profile page to set up a profile
                            Intent intent = new Intent(VehicleList.this, UserProfile.class);
                            startActivity(intent);
                            finish();
                        }//End if()
                    }//End if()

                    //If not successful
                    else
                    {
                        //Error message is displayed to the user informing them of the issue
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(VehicleList.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }//End else()
                }//End onComplete()
            });//End OnCompleteListener()
        }//End else()
    }//End onStart()

    //Sends user to login page
    private void sendToLogin()
    {
        Intent intent =  new Intent(VehicleList.this, LoginUser.class);
        startActivity(intent);
        finish();
    }//End sendToLogin()

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
            Intent exit_intent = new Intent(Intent.ACTION_MAIN);
            exit_intent.addCategory(Intent.CATEGORY_HOME);
            exit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exit_intent);
            finish();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End VehicleList()
