/*



 */
package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;

public class UserHelp extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_user_help);

        ExpandableTextView expandableTextView = findViewById(R.id.expand_text_view);
        expandableTextView.setText(getString(R.string.FAQ));
        ExpandableTextView expandableTextView1 = findViewById(R.id.expand_text_view1);
        expandableTextView1.setText(getString(R.string.FAQ1));
        ExpandableTextView expandableTextView2 = findViewById(R.id.expand_text_view2);
        expandableTextView2.setText(getString(R.string.FAQ2));
        ExpandableTextView expandableTextView3 = findViewById(R.id.expand_text_view3);
        expandableTextView3.setText(getString(R.string.FAQ3));
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
            Intent intent = new Intent(UserHelp.this, SettingsActivity.class);
            startActivity(intent);
        }//End if()

        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(UserHelp.this, MainActivity.class);
            startActivity(intent);
        }//End if()

        //If the settings option is selected, user will be informed they are already on the help screen
        if (id == R.id.action_help)
        {
            Toast.makeText(UserHelp.this, "You are already in 'User Help'", Toast.LENGTH_LONG).show();
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
}//End Settings()