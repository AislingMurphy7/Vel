package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graphview extends AppCompatActivity {

    //Declarin LineGrapheSeries of type Datapoint
    LineGraphSeries<DataPoint> series;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_graphview);

        //Declaring x & y variables
        double y,x;
        //Graph will start at -6
        x = -5.0;

        //XML variables
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        //For loop to loop through & store DataPoints
        for(int i = 0; i<500; i++)
        {
            x = x + 0.01;
            //Function for graph
            y = Math.sin(x);
            series.appendData(new DataPoint(x, y), true, 500);
        }//End For()
        graph.addSeries(series);
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
            Intent intent = new Intent(Graphview.this, Settings.class);
            startActivity(intent);
        }//End if()

        //If the language option is selected, user will be re-directed to language screen
        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(Graphview.this, MainActivity.class);
            startActivity(intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (id == R.id.action_help)
        {
            Intent intent = new Intent(Graphview.this, UserHelp.class);
            startActivity(intent);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}
