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
to the vehicle. This class informs the users of where the port is located within BMW vehicles.
The users can also view the data graphs belonging the the vehicle within this class as well
 */

public class BMW extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_bmw);

        //XML Button variable
        Button button = findViewById(R.id.button);
        Button bt2 = findViewById(R.id.button2);
        Button bt3 = findViewById(R.id.button3);
        Button bt4 = findViewById(R.id.button4);
        Button bt5 = findViewById(R.id.button5);

        //XML ViewPager variable for displaying the Image slider
        ViewPager viewPager = findViewById(R.id.bmwviewPager);
        //Referencing 'ViewPagerAdapter3.class' to create a new adapter
        ViewPagerAdapter3 viewPagerAdapter = new ViewPagerAdapter3(this);
        viewPager.setAdapter(viewPagerAdapter);

        //If the user taps on the button, app will progress to the 'GraphTempSpecs' class
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(BMW.this, GraphTempSpecs.class);
                startActivity(intent);
            }//End onClick()
        });//End setOnClickListener()


        //If the user taps on the button, app will progress to the 'GraphEngineSpecs' class
        bt2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(BMW.this, GraphEngineSpecs.class);
                startActivity(intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user taps on the button, app will progress to the 'GraphEngineRPM' class
        bt3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(BMW.this, GraphEngineRPM.class);
                startActivity(intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user taps on the button, app will progress to the 'GraphEngineAirflow' class
        bt4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(BMW.this, GraphEngineAirflow.class);
                startActivity(intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user taps on the button, app will progress to the 'SetupELM' class
        bt5.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(BMW.this, SetupELM.class);
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
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(BMW.this, user_options.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(BMW.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(BMW.this, userProfile.class);
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
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End BMW()
