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
of when it is safe to use the application. The user is then
required to state if they agree or disagree with the message
*/

public class Agreements extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_agreements);

        //XML Button variables
        Button Agree =  findViewById(R.id.Agree);
        Button Disagree = findViewById(R.id.Disagree);

        //If user taps the 'Disagree' button
        Disagree.setOnClickListener(new View.OnClickListener()
        {
            //The application ends and closes itself
            public void onClick(View view)
            {
                Intent Disagree_intent = new Intent(Intent.ACTION_MAIN);
                Disagree_intent.addCategory(Intent.CATEGORY_HOME);
                Disagree_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Disagree_intent);
                finish();
                System.exit(0);
            } //End onClick()
        }); //End OnClickListener()

        //If the user selects the 'Agree' button
        Agree.setOnClickListener(new View.OnClickListener()
        {
            //The app moves from the current screen to the following screen
            public void onClick(View view)
            {
                Intent Agree_intent = new Intent(Agreements.this, OBDinfo.class);
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
        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent Language_intent = new Intent(Agreements.this, MainActivity.class);
            startActivity(Language_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(Agreements.this, UserHelp.class);
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
}//End Agreements()
