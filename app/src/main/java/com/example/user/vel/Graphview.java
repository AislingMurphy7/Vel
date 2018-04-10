/*

 */
package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class Graphview extends AppCompatActivity
{
    //Holds the values gathered from firebase
    DatabaseReference databaseReference;
    ArrayAdapter<String> arrayAdapter;
    ListView listViewG;
    VW_Data info;
    List<String> graphlist;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_graphview);

        //XML variable
        listViewG = findViewById(R.id.listviewG);

        graphlist = new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference("VW_Data").child("0");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    info = dataSnapshot1.getValue(VW_Data.class);
                    graphlist.add(info.Bearing);
                }
                arrayAdapter = new ArrayAdapter<>(Graphview.this, android.R.layout.simple_list_item_1, graphlist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








               /* String bearing = dataSnapshot.getValue(String.class);
                //Add the info retrieved from datasnapshot into the ArrayList
                graphlist.add(bearing);
                arrayAdapter = new ArrayAdapter<>(Graphview.this, android.R.layout.simple_list_item_1, graphlist);
                listViewG.setAdapter(arrayAdapter);
                //Will refresh app when the data changes in the database */



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
        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent language_intent = new Intent(Graphview.this, MainActivity.class);
            startActivity(language_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(Graphview.this, UserHelp.class);
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
}//End Graphview()
