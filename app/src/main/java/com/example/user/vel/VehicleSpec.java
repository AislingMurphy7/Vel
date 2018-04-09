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
This class retrieves data from a database stored on Firebase and places this data into a ListView.
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

        /*Database variable is getting the connection to the firebase database via google-services
        JSON file and making reference to the child of "Make"*/
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Make");

        //Holds the valuse gathered from firebase
        final ArrayList<String> carlist = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.itemview, carlist);
        //Set the ArrayAdapter to the listview
        listView2.setAdapter(arrayAdapter);

        //ChildEventListener allows child events to be listened for
        database.addChildEventListener(new ChildEventListener()
        {
            //Will run when the app is started and when there is data added to the database
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                //Holds the Datasnapshot value of the database as type String
                String value = dataSnapshot.getValue(String.class);

                //Add the info retrieved from datasnapshot into the ArrayList
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
                    Intent intent = new Intent(VehicleSpec.this, volkswagen.class);
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
        int id = item.getItemId();
        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(VehicleSpec.this, MainActivity.class);
            startActivity(intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (id == R.id.action_help)
        {
            Intent intent = new Intent(VehicleSpec.this, UserHelp.class);
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
}//End VehicleSpec()
