package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
This class displays information informing the user
of when it is safe to use the application. This class also
displays information informing the user about important
information concerning the OBD protocol.
This class is selected from the Homepage of the
Application called 'General Information'
*/

public class General_Information extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_general_info);

        //XML Button variables
        Button Agree =  findViewById(R.id.ok);

        //If the user selects the 'OK' button
        Agree.setOnClickListener(new View.OnClickListener()
        {
            //The app moves from the current screen back to the Home screen
            public void onClick(View view)
            {
                Intent Agree_intent = new Intent(General_Information.this, Homepage.class);
                startActivity(Agree_intent);

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
        //If the home option is selected, user will be re-directed to home screen
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(General_Information.this, Homepage.class);
            startActivity(home_intent);

        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(General_Information.this, UserHelp.class);
            startActivity(help_intent);

        }//End if()

        //If the profile option is selected, user will be re-directed to profile screen
        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(General_Information.this, UserProfile.class);
            startActivity(prof_intent);

        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);

    }//End onOptionsItemSelected()
}//End General_Information()
