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
important information concerning the ELM-327 device.
*/

public class SetupELM extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_setup_elm);

        //XML variables
        Button ok = findViewById(R.id.bt1);

        //When the user selects the 'OK' button
        ok.setOnClickListener(new View.OnClickListener()
        {
            //The application moves to the following screen
            public void onClick(View view)
            {
                Intent intent = new Intent(SetupELM.this, Homepage.class);
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
        //If the home option is selected, user will be re-directed to home screen
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(SetupELM.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(SetupELM.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the profile option is selected, user will be re-directed to profile screen
        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(SetupELM.this, UserProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);

    }//End onOptionsItemSelected()
}//End SetupELM()
