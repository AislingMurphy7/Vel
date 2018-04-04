/*This class displays an information screen informing the user
* of when it is safe to use the application. The user is then
* required to state if they agree or disagree with the message*/
package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Agreements extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_agreements);

        //XML Button variables
        Button agree =  (Button) findViewById(R.id.agree);
        Button disagree = (Button) findViewById(R.id.disagree);

        //ImageView for the VéL logo
        ImageView vel = (ImageView) findViewById(R.id.vel);

        //XMLTextView variables
        TextView text = (TextView) findViewById(R.id.text);
        TextView text2 = (TextView) findViewById(R.id.text2);

        //If user clicks the 'Disagree' button
        disagree.setOnClickListener(new View.OnClickListener()
        {
            //The application ends and closes itself
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);

            } //End onClick()

        }); //End OnClickListener()

        //If the user selects the 'Agree' button
        agree.setOnClickListener(new View.OnClickListener()
        {
            //The app moves from the current screen to the following screen
            public void onClick(View view)
            {
                Intent intent = new Intent(Agreements.this, OBDinfo.class);
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
        int id = item.getItemId();
        //If the settings option is selected, user will be re-directed to setting screen
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(Agreements.this, SettingsActivity.class);
            startActivity(intent);

        }//End if

        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(Agreements.this, MainActivity.class);
            startActivity(intent);

        }//End if

        //If the help option is selected, user will be re-directed to help screen
        if (id == R.id.action_help)
        {
            Intent intent = new Intent(Agreements.this, UserHelp.class);
            startActivity(intent);

        }//End if

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()

}//End Agreements()
