package com.example.user.vel;

/*
    This class is used as a step of security for the
    vehicles data stored within the app. Each vehicles
    data is password protected with the password which
    is set by the composer of the post
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PasswordProtected extends AppCompatActivity
{
    //Declaring XML variables
    private ImageView imageView;
    private TextView vehicle_make, vehicle_model;
    private EditText ent_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_password_protected);

        //XML variables
        vehicle_model = findViewById(R.id.model);
        vehicle_make = findViewById(R.id.make);
        imageView = findViewById(R.id.image);

        Button cont = findViewById(R.id.cont);
        ent_pass = findViewById(R.id.editTextPassword);

        //FireBase Real-time Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Vehicles");

        //Gathers the unique key of each record in the database
        String vehicle_key = Objects.requireNonNull(getIntent().getExtras()).getString("Vehicle_id");
        Log.d("VEH KEY - PASS PROTECT", vehicle_key);

        //Retrieves all the required values from FireBase database
        databaseRef.child(Objects.requireNonNull(vehicle_key)).addValueEventListener(new ValueEventListener()
        {
            @Override
            //When the data changes the data will be retrieved
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String model = (String) dataSnapshot.child("Model").getValue();
                String make = (String) dataSnapshot.child("Make").getValue();
                String image = (String) dataSnapshot.child("Image").getValue();

                //Data is set and displayed on screen to the user
                vehicle_model.setText(model);
                vehicle_make.setText(make);
                //Displays the image
                Glide.with(PasswordProtected.this).load(image).into(imageView);
            }//End onDataChange()

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(PasswordProtected.this, "Database Error!", Toast.LENGTH_LONG).show();
            }//End onCancelled
        });//End ValueEventListener()

        //When the user taps the continue button
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //The password they entered will be saved to a string
                final String enter_pass = ent_pass.getText().toString().trim();

                //FireBase Real-time Database
                final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Vehicles");

                //Gathers the unique key of each record in the database
                final String vehicle_key = Objects.requireNonNull(getIntent().getExtras()).getString("Vehicle_id");

                //Retrieves all the required values from FireBase database
                databaseRef.child(Objects.requireNonNull(vehicle_key)).addValueEventListener(new ValueEventListener() {
                    @Override
                    //When the data changes the data will be retrieved
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //Will retrieve the password specified by the post author
                        String pass = (String) dataSnapshot.child("Password").getValue();

                        //Will print the password saved in FireBase to the Log
                        Log.v("Firebase:", pass);

                        //If the entered password is equal to the saved password
                        if(Objects.equals(pass, enter_pass))
                        {
                            //The sure will be allowed to view the vehicles data
                            Intent intent = new Intent(PasswordProtected.this, DataDisplay.class);
                            intent.putExtra("Vehicle_id", vehicle_key);
                            startActivity(intent);
                        }//End if()
                        //If they are not equal
                        else
                        {
                            //User is notified that the passwords don't match
                            Toast.makeText(getApplicationContext(),"PASSWORD DONT MATCH", Toast.LENGTH_LONG).show();
                        }//End else()
                    }//End onDataChange()

                    @Override
                    //If there is an issue
                    public void onCancelled(DatabaseError databaseError)
                    {
                        //User is notified
                        Toast.makeText(PasswordProtected.this, "Database Error!", Toast.LENGTH_LONG).show();
                    }//End onCancelled
                });//End ValueEventListener()

                //If the email EditText is empty
                if(enter_pass.isEmpty())
                {
                    //An error message is displayed
                    ent_pass.setError(getText(R.string.pass_empt));
                    //Show error
                    ent_pass.requestFocus();
                }//End if()

                //If the password length is less than six
                if(enter_pass.length()<6)
                {
                    //An error message is displayed
                    ent_pass.setError(getText(R.string.mini_length));
                    //Show error
                    ent_pass.requestFocus();
                }//End if()
            }//End onClick()
        });//End OnClickListener()
    }//End onCreate()

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
        //If the home option is selected
        if (option_id == R.id.action_home)
        {
            //The user will be re-directed to the Home screen
            Intent home_intent = new Intent(PasswordProtected.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected
        if (option_id == R.id.action_help)
        {
            //The user will be re-directed to help screen
            Intent help_intent = new Intent(PasswordProtected.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the profile option is selected
        if (option_id == R.id.action_prof)
        {
            //The user will be re-directed to profile screen
            Intent help_intent = new Intent(PasswordProtected.this, UserProfile.class);
            startActivity(help_intent);
        }//End if()

        //If the exit option is selected
        if (option_id == R.id.action_exit)
        {
            //The app will close
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End PasswordProtected()
