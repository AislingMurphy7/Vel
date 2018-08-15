package com.example.user.vel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

/*
    This class is used to graph the Data for the "Engine RPM which
    is retrieved from the Database. This retrieves the data from the database which is stored on
    Firebase. This data is then used to plot the data on a LineChart. Alert Dialogs Pop up before
    the graph appears and explains the data to the user, as well as stating if the data recorded is
    normal readings or if there are variances present
 */
public class GraphEngineRPM extends Activity implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private static final String TAG = "GraphEngineRPM";

    private LineChart chart;

    //Array to hold Mass Airflow data from FireBase
    ArrayList<Entry> engineRPMlist = new ArrayList<>();

    //Variables
    LineDataSet set1;
    LineData data;

    protected void onCreate(Bundle savedInstanceState) {

        /*This creates an Alert dialog on this screen, it also sets it so the user can cancel the message
          for the Engine RPM information*/
        AlertDialog.Builder builder = new AlertDialog.Builder(GraphEngineRPM.this);
        builder.setCancelable(true);

        //Setting the title and message from the string.xml
        builder.setTitle(GraphEngineRPM.this.getString(R.string.engine_rpm_title));
        builder.setMessage(GraphEngineRPM.this.getString(R.string.engine_rpm_def));

        //When the user selects the Cancel button the page will redirect back to the VehicleSpec page
        builder.setNegativeButton(GraphEngineRPM.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                Intent intent = new Intent(GraphEngineRPM.this, Homepage.class);
                startActivity(intent);
            }//End onClick()
        });//End setNegativeButton()

        builder.setPositiveButton(GraphEngineRPM.this.getString(R.string.Ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }//End onClick()
        });//End setPositiveButton()

        /*This creates an Alert dialog on this screen, it also sets it so the user can cancel the message
          for the Engine RPM information retrieved from the database*/
        AlertDialog.Builder builder2 = new AlertDialog.Builder(GraphEngineRPM.this);
        builder2.setCancelable(true);

        //Setting the title and message from the string.xml
        builder2.setTitle(GraphEngineRPM.this.getString(R.string.IMPORTANT));
        builder2.setMessage(GraphEngineRPM.this.getString(R.string.rpm_info));

        //When the user selects the Cancel button the page will redirect back to the VehicleSpec page
        builder2.setNegativeButton(GraphEngineRPM.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
                Intent intent = new Intent(GraphEngineRPM.this, Homepage.class);
                startActivity(intent);
            }//End onClick()
        });//End setNegativeButton()

        builder2.setPositiveButton(GraphEngineRPM.this.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }//End onClick()
        });//End setPositiveButton()

        //Show the Dialogs on screen
        builder2.show();
        builder.show();

        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_graph_engine_rpm);

        //XML reference
        chart = findViewById(R.id.linechart);

        //Listens for on chart taps
        chart.setOnChartGestureListener(GraphEngineRPM.this);
        chart.setOnChartValueSelectedListener(GraphEngineRPM.this);

        //Enable touch gestures
        chart.setTouchEnabled(true);

        //Enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);

        //Enable pinch zoom
        chart.setPinchZoom(true);

        //Background color
        chart.setBackgroundColor(Color.LTGRAY);

        //Setting YAxis
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0f);
        left.setAxisMaximum(1500f);
        left.setTextSize(13f);
        left.enableGridDashedLine(10f, 10f, 0f);

        YAxis left2 = chart.getAxisRight();
        left2.setEnabled(false);

        chart.getAxisRight().setEnabled(false);

        //Value String
        String[] vals = new String[] {"0s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "11s"};

        //Legend object
        Legend i = chart.getLegend();
        //Customise legend
        i.setTextSize(15f);
        i.setForm(Legend.LegendForm.CIRCLE);
        i.setTextColor(Color.BLACK);

        //Setting XAis
        XAxis x = chart.getXAxis();
        x.setTextSize(13f);
        x.setValueFormatter(new MyXAxisValueFormatter(vals));
        x.setGranularity(1);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Adding value to array as system will crash without
        engineRPMlist.add(new Entry(0, 0));
        engineRPMlist.add(new Entry(1, 0));

        //Setting the line
        set1 = new LineDataSet(engineRPMlist, "Engine RPM ");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);


        data = new LineData(set1);

        chart.setData(data);

        //Calls the downloadData()
        downloadData();
        //Change the chart when a change occurs
        chart.notifyDataSetChanged();
    }//End onCreate

    private void downloadData()
    {
        //ArrayAdapter
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_graph_engine_rpm);

        //Connecting into table "VehicleData" on the Firebase database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("VehicleData");
        //ChildEventListener allows child events to be listened for
        database.addChildEventListener(new ChildEventListener()
        {
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
            {
                //Holds the Datasnapshot value of the database as type String
                VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                //Prints values to console to prove the download is working
                System.out.println("getEngineRPM: " + Objects.requireNonNull(vehicleData).getEngineRPM());
                System.out.println("prevChildKey: " + prevChildKey);
                System.out.println("data.key" + dataSnapshot.getKey());
                //Converting value to integer
                setData(Integer.parseInt(dataSnapshot.getKey()), vehicleData);
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
    }//End DownloadData()
    private void setData(int key, VehicleData vehicleData)
    {
        //Prints to console first
        System.out.println("Using key: " + key);
        System.out.println("Setting EngineRPM: " + vehicleData.getEngineRPM());
        //Adds new entrys to the arrayList and converts the string into a float
        engineRPMlist.add(new Entry(key + 2, Float.parseFloat(vehicleData.getEngineRPM())));

        //Change the chart when changes occurs
        set1.notifyDataSetChanged();
        data.notifyDataChanged();
        this.chart.notifyDataSetChanged();
        //Redisplay chart
        chart.invalidate();
    }//End setData()

    //Using the String to change the values of the XAis
    public class MyXAxisValueFormatter implements IAxisValueFormatter{
        private String[] mVals;
        private MyXAxisValueFormatter(String[] vals)
        {
            this.mVals = vals;
        }//End MyXAxisValueFormatter()

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mVals[(int)value];
        }//End getFormattedValue()
    }//End MyXAxisValueFormatter()

    @Override
    //Sends log message if action is performed
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
        Log.i(TAG, "onChartGestureStart: X:" + me.getX() + "Y:" + me.getY());
    }//End()

    @Override
    //Sends log message if action is performed
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
        Log.i(TAG, "onChartGestureEnd: " + lastPerformedGesture);
    }//End()

    @Override
    //Sends log message if action is performed
    public void onChartLongPressed(MotionEvent me)
    {
        Log.i(TAG, "onChartLongPressed: ");
    }//End()

    @Override
    //Sends log message if action is performed
    public void onChartDoubleTapped(MotionEvent me)
    {
        Log.i(TAG, "onChartDoubleTapped: ");
    }//End()

    @Override
    //Sends log message if action is performed
    public void onChartSingleTapped(MotionEvent me)
    {
        Log.i(TAG, "onChartSingleTapped: ");
    }//End()

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
    {
        Log.i(TAG, "onChartFling: veloX: " + velocityX + "veloY" + velocityY);
    }//End()

    @Override
    //Sends log message if action is performed
    public void onChartScale(MotionEvent me, float scaleX, float scaleY)
    {
        Log.i(TAG, "onChartScale: ScaleX: " + scaleX + "ScaleY: " + scaleY);
    }//End()

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY)
    {
        Log.i(TAG, "onChartTranslate: dX" + dX + "dY" + dY);
    }//End()

    @Override
    //Sends log message if action is performed
    public void onValueSelected(Entry e, Highlight h)
    {
        Log.i(TAG, "onValueSelected: " + e.toString());
        Toast.makeText(this, "onValueSelected: " + e.toString(), Toast.LENGTH_SHORT).show();
    }//End()

    @Override
    //Sends log message if action is performed
    public void onNothingSelected()
    {
        Log.i(TAG, "onNothingSelected: ");
    }//End()
}//End GraphEngineRPM
