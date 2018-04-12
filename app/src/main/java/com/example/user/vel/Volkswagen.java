package com.example.user.vel;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
This class informs the user where to locate the OBD-II port for connecting the ELM-327 device
to the vehicle. This class informs the users of where the port is located within Audi vehicles.
 */

public class Volkswagen extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_volkswagen);

        //XML button variable
        Button bt1 = findViewById(R.id.button);
        Button bt2 = findViewById(R.id.button2);
        Button bt3 = findViewById(R.id.button3);


        //XML ViewPager variable for displaying the Image slider
        ViewPager viewPager = findViewById(R.id.viewPager);
        //Referencing 'ViewPagerAdapter.class' to create a new adapter
        ViewPagerAdapter2 viewPagerAdapter = new ViewPagerAdapter2 (this);
        viewPager.setAdapter(viewPagerAdapter);

        //If the user taps on the 'Next' button, app will progress to the 'SetupELM' class
        bt1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Volkswagen.this, GraphTempSpecs.class);
                startActivity(intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user taps on the 'Next' button, app will progress to the 'SetupELM' class
        bt2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Volkswagen.this, GraphEngineSpecs.class);
                startActivity(intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user taps on the 'Next' button, app will progress to the 'SetupELM' class
        bt3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Volkswagen.this, Graph3.class);
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
        int option_id = item.getItemId();
        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent language_intent = new Intent(Volkswagen.this, MainActivity.class);
            startActivity(language_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(Volkswagen.this, UserHelp.class);
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
}//End Audi()