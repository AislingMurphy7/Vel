/*
This class informs the user where to locate the OBD-II port for connecting the ELM-327 device
to the vehicle. This class informs the users of where the port is located within BMW vehicles.
 */
package com.example.user.vel;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Ford extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_ford);

        //XML Button variable
        Button button = findViewById(R.id.button);

        //XML ViewPager variable for displaying the Image slider
        ViewPager viewPager = findViewById(R.id.viewPager);
        //Referencing 'ViewPagerAdapter3.class' to create a new adapter
        ViewPagerAdapter4 viewPagerAdapter = new ViewPagerAdapter4(this);
        viewPager.setAdapter(viewPagerAdapter);

        //If the user taps on the 'Next' button, app will progress to the 'SetupELM' class
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Ford.this, SetupELM.class);
                startActivity(intent);
            }//End onClick()
        });//End seOnClickListener
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
        //If the settings option is selected, user will be re-directed to setting screen
        if (option_id == R.id.action_settings)
        {
            Intent settings_intent = new Intent(Ford.this, SettingsActivity.class);
            startActivity(settings_intent);
        }//End if()

        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent language_intent = new Intent(Ford.this, MainActivity.class);
            startActivity(language_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(Ford.this, UserHelp.class);
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
}//End BMW()