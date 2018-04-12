package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
This class displays an information screen informing the user
important information concerning the OBD protocol. Once the user
has finished reading they can then progress to the next screen
*/

public class OBDinfo extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_obdinfo);

        //XML variables
        Button next = findViewById(R.id.bt1);
        Button elmSetup = findViewById(R.id.bt2);

        //When the user selects the 'next' button
        next.setOnClickListener(new View.OnClickListener()
        {
            //The application moves to the following screen
            public void onClick(View v)
            {
                Intent intent = new Intent(OBDinfo.this, VehicleSpec.class);
                startActivity(intent);
            }//End onClick()
        });//End OnClickListener()

        //When the user selects the 'next' button
        elmSetup.setOnClickListener(new View.OnClickListener()
        {
            //The application moves to the following screen
            public void onClick(View v)
            {
                Intent intent = new Intent(OBDinfo.this, SetupELM.class);
                startActivity(intent);
            }//End onClick()
        });//End OnClickListener()
    }//End onCreate()

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);

    }//End onCreateOptionMenu()

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent language_intent = new Intent(OBDinfo.this, MainActivity.class);
            startActivity(language_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(OBDinfo.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            Intent exit_intent = new Intent(Intent.ACTION_MAIN);
            exit_intent.addCategory(Intent.CATEGORY_HOME);
            exit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exit_intent);
            finish();
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End OBDinfo()
