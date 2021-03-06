package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;

/*
This class displays several ExpandableTextViews to the user which
all contain FAQs and general information concerning the application
 */

public class UserHelp extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_user_help);

        //XML variables
        ExpandableTextView expandableTextView = findViewById(R.id.expand_text_view);
        expandableTextView.setText(getString(R.string.FAQ));
        ExpandableTextView expandableTextView1 = findViewById(R.id.expand_text_view1);
        expandableTextView1.setText(getString(R.string.FAQ1));
        ExpandableTextView expandableTextView2 = findViewById(R.id.expand_text_view2);
        expandableTextView2.setText(getString(R.string.FAQ2));
        ExpandableTextView expandableTextView3 = findViewById(R.id.expand_text_view3);
        expandableTextView3.setText(getString(R.string.FAQ3));
        ExpandableTextView expandableTextView4 = findViewById(R.id.expand_text_view4);
        expandableTextView4.setText(getString(R.string.FAQ4));
        ExpandableTextView expandableTextView5 = findViewById(R.id.expand_text_view5);
        expandableTextView5.setText(getString(R.string.FAQ5));
        ExpandableTextView expandableTextView6 = findViewById(R.id.expand_text_view6);
        expandableTextView6.setText(getString(R.string.FAQ6));
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
        //If the home option is selected
        if (option_id == R.id.action_home)
        {
            //The user will be redirected to the home page
            Intent home_intent = new Intent(UserHelp.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected
        if (option_id == R.id.action_help)
        {
            //The user will be informed they are already in the help page
            Toast.makeText(UserHelp.this, R.string.user_page, Toast.LENGTH_LONG).show();
        }//End if()

        //If the profile option is selected
        if (option_id == R.id.action_prof)
        {
            //The user will be redirected to the profile page
            Intent prof_intent = new Intent(UserHelp.this, UserProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected
        if (option_id == R.id.action_exit)
        {
            //The app will close
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End UserHelp()