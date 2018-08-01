package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/*
This class retrieves data from a database stored on FireBase and places this data into a ListView.
From the ListView the user can select which Vehicle they would like to view
 */

public class VehicleSpec extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets layout according to XML file
        setContentView(R.layout.activity_vehicle_spec);

        //XML variable
        ListView listView2 = findViewById(R.id.listview2);

        /*Database variable is getting the connection to the FireBase database via google-services
        JSON file and making reference to the child of "Make"*/
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Make");

        //Holds the values gathered from FireBase
        final ArrayList<String> carlist = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.itemview, carlist);
        //Set the ArrayAdapter to the ListView
        listView2.setAdapter(arrayAdapter);

        //ChildEventListener allows child events to be listened for
        database.addChildEventListener(new ChildEventListener()
        {
            //Will run when the app is started and when there is data added to the database
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
            {
                //Holds the DataSnapshot value of the database as type String
                String value = dataSnapshot.getValue(String.class);

                //Add the info retrieved from DataSnapshot into the ArrayList
                carlist.add(value);
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

        //When the user clicks on any option on the ListView the following will occur
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //If option(i) is equal to the first row
                if(i == 0)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(VehicleSpec.this, Audi.class);
                    startActivity(intent);
                }//End if()

                //If option(i) is equal to the second row
                else if(i == 1)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(VehicleSpec.this, Volkswagen.class);
                    startActivity(intent);
                }//End if()

                //If option(i) is equal to the third row
                else if(i == 2)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(VehicleSpec.this, Ford.class);
                    startActivity(intent);
                }//End if()

                //If option(i) is equal to the fourth row
                else if(i == 3)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(VehicleSpec.this, BMW.class);
                    startActivity(intent);
                }//End if()

                //If option(i) is equal to the fifth row
                else if(i == 4)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(VehicleSpec.this, Renault.class);
                    startActivity(intent);
                }//End if()

                //If option(i) is equal to the sixth row
                else if(i == 5)
                {
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(VehicleSpec.this, Toyota.class);
                    startActivity(intent);
                }//End if()
            }//End onItemClick()
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
        int option_id = item.getItemId();
        //If the home option is selected
        if (option_id == R.id.action_home)
        {
            //The user will be re-directed to home screen
            Intent home_intent = new Intent(VehicleSpec.this, user_options.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected
        if (option_id == R.id.action_help)
        {
            //The user will be re-directed to help screen
            Intent intent = new Intent(VehicleSpec.this, UserHelp.class);
            startActivity(intent);
        }//End if()

        //If the profile option is selected
        if (option_id == R.id.action_prof)
        {
            //The user will be re-directed to profile screen
            Intent prof_intent = new Intent(VehicleSpec.this, userProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected
        if (option_id == R.id.action_exit)
        {
            //The app will close
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End VehicleSpec()
