package com.example.user.vel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class GraphCoolantTemp extends AppCompatActivity implements
        OnChartGestureListener, OnChartValueSelectedListener
{
    private static final String TAG = "GraphCoolantTemps";

    //Declaring the chart
    private LineChart chart;

    //Declaring progress dialog
    private ProgressDialog progress;

    //Array to hold Mass Airflow data from FireBase
    ArrayList<Entry> coolantTemperatureList = new ArrayList<>();

    //Variables
    LineDataSet set1;
    LineData data;

    // Lowest value rendered on graph
    float lowestGraphedValue = 0;

    // Highest value rendered on graph
    float highestGraphedValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*This creates an Alert dialog on this screen, it also sets it so the user can cancel the message
          for the coolant temperature information*/
        AlertDialog.Builder builder = new AlertDialog.Builder(GraphCoolantTemp.this);
        builder.setCancelable(true);

        //Setting the title and message from the string.xml
        builder.setTitle(GraphCoolantTemp.this.getString(R.string.engine_coolant_title));
        builder.setMessage(GraphCoolantTemp.this.getString(R.string.engine_coolant_def));

        builder.setPositiveButton(GraphCoolantTemp.this.getString(R.string.Ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }//End onClick()
        });//End setPositiveButton()

        builder.show();

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_graph_coolant_temp);

        chart = findViewById(R.id.linechart);

        //Listens for on chart taps
        chart.setOnChartGestureListener(GraphCoolantTemp.this);
        chart.setOnChartValueSelectedListener(GraphCoolantTemp.this);

        //Enable touch gestures
        chart.setTouchEnabled(true);

        //Enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);

        //Enable pinch zoom
        chart.setPinchZoom(true);

        //Background color
        chart.setBackgroundColor(Color.WHITE);

        //Setting YAxis
        YAxis left = chart.getAxisLeft();
        left.setTextSize(13f);
        left.enableGridDashedLine(10f, 10f, 0f);

        YAxis left2 = chart.getAxisRight();
        left2.setEnabled(false);

        chart.getAxisRight().setEnabled(false);

        //Value string
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

        //Adding value to arrays as system will crash without
        coolantTemperatureList.add(new Entry(0, 0));
        coolantTemperatureList.add(new Entry(1, 0));

        //Setting the lines
        set1 = new LineDataSet(coolantTemperatureList, "Coolant Temp ");
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

        //XML button
        Button checkD = findViewById(R.id.checkdata);

        //If the user taps the button
        checkD.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //ProgressDialog will appear stating that the data is being checked
                progress = new ProgressDialog(GraphCoolantTemp.this);
                progress.setMax(100);
                progress.setMessage("Checking...");
                progress.setTitle("Checking Vehicle data");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.show();

                new Thread(new Runnable()
                {
                    @Override
                    //When running
                    public void run()
                    {
                        try
                        {
                            //Whilst the progress dialog is less than or equal to the max
                            while (progress.getProgress() <= progress.getMax())
                            {
                                //Dialog will pause
                                Thread.sleep(200);

                                //Handler will retrieve the message of increments
                                handler.sendMessage(handler.obtainMessage());

                                //If the progress dialog is equal to the max
                                if (progress.getProgress() == progress.getMax())
                                {
                                    //progressDialog will be shut
                                    progress.dismiss();
                                    //The AlertDialog will be opened
                                    progress.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                        @Override
                                        public void onDismiss(DialogInterface dialog)
                                        {
                                            showAlertDialogs();
                                        }//End onDismiss()
                                    });//End OnDismissListener()
                                }//End if()
                            }//End while()
                        }//End try()
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }//End catch()
                    }//End run()
                }).start();
            }//End onClick()
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler()
            {
                public void handleMessage (Message msg)
                {
                    super.handleMessage(msg);
                    //Sets the value to increment by 3
                    progress.incrementProgressBy(3);
                }//End handleMessage()
            };//End Handler()
        });//End OnClickListener()
    }//End onCreate

    public void showAlertDialogs()
    {
        if(this.highestGraphedValue < 220 && this.lowestGraphedValue > 190)
        {
            /*This creates an Alert dialog on this screen, it also sets it so the user can cancel the message
                for the Mass Airflow rate information retrieved from the database*/
            AlertDialog.Builder builder2 = new AlertDialog.Builder(GraphCoolantTemp.this);
            builder2.setCancelable(true);

            //Setting the title and message from the string.xml
            builder2.setTitle(GraphCoolantTemp.this.getString(R.string.NormalValues));
            builder2.setMessage(GraphCoolantTemp.this.getString(R.string.NormalValInstruct));

            //When the user selects the Cancel button the page will redirect back to the VehicleSpec page
            builder2.setNegativeButton(GraphCoolantTemp.this.getString(R.string.cancel), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    dialog.cancel();
                    Intent intent = new Intent(GraphCoolantTemp.this, DataDisplay.class);
                    startActivity(intent); }//End onClick()
            });//End setNegativeButton()

            //If the user taps Ok
            builder2.setPositiveButton(GraphCoolantTemp.this.getString(R.string.Ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }//End onClick()
            });//End setPositiveButton()

            //Show the Dialogs on screen
            builder2.show();
        }//End if()

        if(this.highestGraphedValue > 220)
        {
            /*This creates an Alert dialog on this screen, it also sets it so the user can cancel the message
                for the Mass Airflow rate information retrieved from the database*/
            AlertDialog.Builder builder2 = new AlertDialog.Builder(GraphCoolantTemp.this);
            builder2.setCancelable(true);

            //Setting the title and message from the string.xml
            builder2.setTitle(GraphCoolantTemp.this.getString(R.string.CoolantTemperatureThresholdHigh));
            builder2.setMessage(GraphCoolantTemp.this.getString(R.string.CoolantTemperatureThreshold_instructionsHigh));

            //When the user selects the Cancel button the page will redirect back to the VehicleSpec page
            builder2.setNegativeButton(GraphCoolantTemp.this.getString(R.string.cancel), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    dialog.cancel();
                    Intent intent = new Intent(GraphCoolantTemp.this, DataDisplay.class);
                    startActivity(intent); }//End onClick()
            });//End setNegativeButton()

            //If the user taps Ok
            builder2.setPositiveButton(GraphCoolantTemp.this.getString(R.string.Ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }//End onClick()
            });//End setPositiveButton()

            //Show the Dialogs on screen
            builder2.show();
        }//End if()

        if(this.lowestGraphedValue < 190)
        {
            /*This creates an Alert dialog on this screen, it also sets it so the user can cancel the message
             for the Mass Airflow rate information retrieved from the database*/
            AlertDialog.Builder builder2 = new AlertDialog.Builder(GraphCoolantTemp.this);
            builder2.setCancelable(true);

            //Setting the title and message from the string.xml
            builder2.setTitle(GraphCoolantTemp.this.getString(R.string.CoolantTemperatureThresholdLow));
            builder2.setMessage(GraphCoolantTemp.this.getString(R.string.CoolantTemperatureThreshold_instructionsLow));

            //When the user selects the Cancel button the page will redirect back to the VehicleSpec page
            builder2.setNegativeButton(GraphCoolantTemp.this.getString(R.string.cancel), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    dialog.cancel();
                    Intent intent = new Intent(GraphCoolantTemp.this, DataDisplay.class);
                    startActivity(intent); }//End onClick()
            });//End setNegativeButton()

            //If the user taps Ok
            builder2.setPositiveButton(GraphCoolantTemp.this.getString(R.string.Ok), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }//End onClick()
            });//End setPositiveButton()

            //Show the Dialogs on screen
            builder2.show();
        }
    }//End showAlertDialog()

    //Downloads Data from FireBase
    private void downloadData()
    {
        //ArrayAdapter
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_graph_temp_specs);

        Intent intent = getIntent();
        final String vehicle_key = intent.getStringExtra("Vehicle_id");
        Log.d("VEH_KEY - COOLANT TEMPS", vehicle_key);

        //Connecting into table "VehicleData" on the FireBase database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Vehicles").child(vehicle_key).child("VehiclesData");

        //ChildEventListener allows child events to be listened for
        database.addChildEventListener(new ChildEventListener()
        {
            //Will run when the app is started and when there is data added to the database
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
            {
                //Holds the DataSnapshot value of the database as type String
                VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);

                //Prints values to console to prove the download is working
                System.out.println("getCoolantTemperature: " + Objects.requireNonNull(vehicleData).getCoolantTemperature());
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

    //Function sets the data on the graph
    private void setData(int key, VehicleData vehicleData)
    {
        //Prints to console first
        System.out.println("Using key: " + key);
        System.out.println("Setting CoolantTemperature: " + vehicleData.getCoolantTemperature());

        float coolantTemperature = Float.parseFloat(vehicleData.getCoolantTemperature());

        if( (this.lowestGraphedValue == 0) || coolantTemperature < this.lowestGraphedValue)
        {
            this.lowestGraphedValue = coolantTemperature;
        }

        if( (this.highestGraphedValue == 0) || coolantTemperature > this.highestGraphedValue)
        {
            this.highestGraphedValue = coolantTemperature;
        }

        //Adds new entries to the arrayList and converts the string into a float
        coolantTemperatureList.add(new Entry(key + 2, coolantTemperature));

        //Change the chart when changes occurs
        set1.notifyDataSetChanged();
        data.notifyDataChanged();
        this.chart.notifyDataSetChanged();

        //Redisplay chart
        chart.invalidate();
    }//End setData()

    //Using the String to change the values of the XAis
    public class MyXAxisValueFormatter implements IAxisValueFormatter
    {
        private String[] mVals;
        private MyXAxisValueFormatter(String[] vals)
        {
            this.mVals = vals;
        }//End MyXAxisValueFormatter()

        @Override
        public String getFormattedValue(float value, AxisBase axis)
        {
            return mVals[(int)value];
        }//End getFormattedValue()
    }//End MyXAxisValueFormatter()

    @Override
    //Sends log message if action is performed
    public void onValueSelected(Entry e, Highlight h)
    {
        Log.i(TAG, "onValueSelected: " + e.toString());
        Toast.makeText(this, "onValueSelected: " + e.toString(), Toast.LENGTH_SHORT).show();
    }//End()

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
    }//End()

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
    }//End()

    @Override
    public void onChartLongPressed(MotionEvent me)
    {
    }//End()

    @Override
    public void onChartDoubleTapped(MotionEvent me)
    {
    }//End()

    @Override
    public void onChartSingleTapped(MotionEvent me)
    {
    }//End()

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
    {
    }//End()

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY)
    {
    }//End()

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY)
    {
    }//End()

    @Override
    public void onNothingSelected()
    {
    }//End()
}//End GraphIntakeAir()
