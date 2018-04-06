/*
This class contains the data which was read from the firebase database for only the Ford vehicles.
This class displays a listview with the models of Ford.
 */
package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Ford extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_ford);

        //XML variable
        final ListView listViewford = findViewById(R.id.listviewford);

        /*Database variable is getting the connection to the firebase database via google-services
        JSON file and making reference to the child of "ModelFord"*/
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("ModelFord");

        //Holds the valuse gathered from firebase
        final ArrayList<String> modellistford = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, modellistford);
        //Set the ArrayAdapter to the listview
        listViewford.setAdapter(arrayAdapter);

        //ChildEventListener allows child events to be listened for
        database.addChildEventListener(new ChildEventListener()
        {
            //Will run when the app is started and when there is data added to the database
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                //Holds the Datasnapshot value of the database as type String
                String value = dataSnapshot.getValue(String.class);

                //Add the info retrieved from datasnapshot into the ArrayList
                modellistford.add(value);
                //Will refresh app when the data changes in the database
                arrayAdapter.notifyDataSetChanged();
            }//End onChildAdded()
            //Will run when data within the database is changed/edited
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }//End onChildChanged()
            //Will run when data within the database is removed
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }//End onChildRemoved()
            //Will run when data within the database is moved to different location
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }//End onChildMoved()
            //Will run when any sort of error occurs
            public void onCancelled(DatabaseError databaseError)
            {

            }//End onCancelled()
        });//End addChildEventListener()

        listViewford.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //If option(i) is equal to the first row
                if(i == 0)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(Ford.this, TESTER.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the second row
                else if(i == 1)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(Ford.this, TESTER.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the third row
                else if(i == 2)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(Ford.this, TESTER.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the fourth row
                else if(i == 3)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(Ford.this, TESTER.class);
                    startActivity(intent);
                }//End if()
            }
        });//End setOnItemClickListener()
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
            Intent intent = new Intent(Ford.this, SettingsActivity.class);
            startActivity(intent);
        }//End if()

        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(Ford.this, MainActivity.class);
            startActivity(intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (id == R.id.action_help)
        {
            Intent intent = new Intent(Ford.this, UserHelp.class);
            startActivity(intent);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End Ford()
