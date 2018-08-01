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
        if (option_id == R.id.action_home)
        {
            Intent home_intent = new Intent(UserHelp.this, user_options.class);
            startActivity(home_intent);
        }//End if()

        //If the settings option is selected, user will be informed they are already on the help screen
        if (option_id == R.id.action_help)
        {
            Toast.makeText(UserHelp.this, R.string.user_page, Toast.LENGTH_LONG).show();
        }//End if()

        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(UserHelp.this, userProfile.class);
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
}//End UserHelp()