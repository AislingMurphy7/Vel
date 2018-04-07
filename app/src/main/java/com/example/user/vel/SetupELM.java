/*
This class displays an information screen informing the user
important information concerning the ELM-327 device. Once the user
has finished reading they can then progress to the next screen
*/
package com.example.user.vel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class SetupELM extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_setup_elm);

        //XML variables
        Button next = findViewById(R.id.bt1);
        ProgressBar progressbar = findViewById(R.id.progressbar);
        //Changes colour of progressbar
        progressbar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        //Setting the progress bar to 95% fill
        progressbar.setProgress(95);

        //When the user selects the 'next' button
        next.setOnClickListener(new View.OnClickListener()
        {
            //The application moves to the following screen
            public void onClick(View view)
            {
                Intent intent = new Intent(SetupELM.this, VehicleSpec.class);
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
        int id = item.getItemId();
        //If the settings option is selected, user will be re-directed to setting screen
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(SetupELM.this, SettingsActivity.class);
            startActivity(intent);
        }//End if()

        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(SetupELM.this, MainActivity.class);
            startActivity(intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (id == R.id.action_help)
        {
            Intent intent = new Intent(SetupELM.this, UserHelp.class);
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
}//End OBDinfo()
