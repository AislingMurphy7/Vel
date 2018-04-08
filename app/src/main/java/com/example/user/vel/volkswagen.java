/*
This class informs the user where to locate the OBD-II port for connecting the ELM-327 device
to the vehicle. This class informs the users of where the port is located within Volkswagen vehicles.
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

public class volkswagen extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_volkswagen);

        //XML button variable
        Button bt1 = findViewById(R.id.bt1);

        //XML ViewPager variable for displaying the Image slider
        ViewPager viewPager = findViewById(R.id.volksviewPager);
        //Referencing 'ViewPagerAdapter5.class' to create a new adapter
        ViewPagerAdapter2 viewPagerAdapter = new ViewPagerAdapter2(this);
        viewPager.setAdapter(viewPagerAdapter);

        //If the user taps on the 'Next' button, app will progress to the 'SetupELM' class
        bt1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(volkswagen.this, SetupELM.class);
                startActivity(intent);
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
        int id = item.getItemId();
        //If the settings option is selected, user will be re-directed to setting screen
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(volkswagen.this, SettingsActivity.class);
            startActivity(intent);
        }//End if()

        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(volkswagen.this, MainActivity.class);
            startActivity(intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (id == R.id.action_help)
        {
            Intent intent = new Intent(volkswagen.this, UserHelp.class);
            startActivity(intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (id == R.id.action_exit)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End volkswagen()