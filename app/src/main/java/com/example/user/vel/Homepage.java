package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
    This class is the main 'Homepage' of the VéL application,
    Here the user can choose from a selection of options
    such as 'General Information', 'ELM-327 Setup',
    'Vehicle Information', 'Edit Profile' & 'Social Forum'
 */

public class Homepage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_homepage);

        //XML button variables
        Button general_info = findViewById(R.id.Informationbtn);
        Button device_btn = findViewById(R.id.Devicebtn);
        Button vehicle_info = findViewById(R.id.Vehiclebtn);
        Button edit_prof = findViewById(R.id.EditProfbtn);
        Button social_forum = findViewById(R.id.FourmBtn);

        //If the user selects the 'General Information' button
        general_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'General Information' screen
                Intent general_info = new Intent(Homepage.this, General_Information.class);
                startActivity(general_info);
            }//End onClick()
        });//End setOnClickListener()

        //If the user selects the 'ELM-327 Setup' button
        device_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'ELM-327 Setup' screen
                Intent device = new Intent(Homepage.this, SetupELM.class);
                startActivity(device);
            }//End onClick()
        });//End setOnClickListener()

        //If the user selects the 'Vehicle Information' button
        vehicle_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'Vehicle Information' screens
                Intent vehicle_info = new Intent(Homepage.this, VehicleList.class);
                startActivity(vehicle_info);
            }//End onClick()
        });//End setOnClickListener()

        //If the user selects the 'Edit Profile' button
        edit_prof.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'Edit Profile' screen
                Intent profile = new Intent(Homepage.this, UserProfile.class);
                startActivity(profile);
            }//End onClick()
        });//End setOnClickListener()

        //If the user selects the 'Social Forum' button
        social_forum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'Social Forum' screen
                Intent social = new Intent(Homepage.this, PostList.class);
                startActivity(social);
            }//End onClick()
        });//End setOnClickListener()
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
            //The user will be informed they are already in the home page
            Toast.makeText(Homepage.this, R.string.options_page, Toast.LENGTH_LONG).show();
        }//End if()

        //If the help option is selected
        if (option_id == R.id.action_help)
        {
            //The user will be re-directed to help screen
            Intent help_intent = new Intent(Homepage.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the profile option is selected
        if (option_id == R.id.action_prof)
        {
            //The user will be re-directed to profile screen
            Intent help_intent = new Intent(Homepage.this, UserProfile.class);
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
}//End Homepage()
