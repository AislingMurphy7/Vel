package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
    This class contains all the links for the user to view the
    data-sets which would be retrieved from the ELM-327 device.
    Each data-set differs between vehicles
 */

public class DataDisplay extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_data_display);

        Intent intent = getIntent();
        final String vehicle_key = intent.getStringExtra("Vehicle_id");
        Log.d(vehicle_key, "DATA DISPLAY ");

        //XML variables
        Button mass_air = findViewById(R.id.mass_air);
        Button engine_load = findViewById(R.id.engine_throttle);
        Button engine_RPM = findViewById(R.id.RPM);
        Button engine_temps = findViewById(R.id.engine_temps);

        //If the Engine mass airflow button is tapped
        mass_air.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to view the data
                Intent intent = new Intent(DataDisplay.this, GraphEngineAirflow.class);
                intent.putExtra("Vehicle_id", vehicle_key);
                startActivity(intent);
            }//End onClick()
        });//End OnClickListener()

        //If the Engine temperature button is tapped
        engine_temps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DataDisplay.this, GraphTempSpecs.class);
                intent.putExtra("Vehicle_id", vehicle_key);
                startActivity(intent);
            }//End onClick()
        });//End OnClickListener()

        //If the Engine throttle load button is tapped
        engine_load.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(DataDisplay.this, GraphEngineSpecs.class);
                intent.putExtra("Vehicle_id", vehicle_key);
                startActivity(intent);
            }//End onClick()
        });//End OnClickListener()

        //If the Engine RPM button is tapped
        engine_RPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataDisplay.this, GraphEngineRPM.class);
                intent.putExtra("Vehicle_id", vehicle_key);
                startActivity(intent);
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
            Intent home_intent = new Intent(DataDisplay.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected
        if (option_id == R.id.action_help)
        {
            //The user will be re-directed to help screen
            Intent help_intent = new Intent(DataDisplay.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the profile option is selected
        if (option_id == R.id.action_prof)
        {
            //The user will be re-directed to profile screen
            Intent help_intent = new Intent(DataDisplay.this, UserProfile.class);
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
}//End DataDisplay()
