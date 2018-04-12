package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

public class GraphEngineSpecs extends Activity implements
        OnChartGestureListener, OnChartValueSelectedListener {

    private static final String TAG = "GraphEngineSpecs";

    private LineChart chart;

    ArrayList<Entry> engineloadList = new ArrayList<>();
    ArrayList<Entry> throttlePositionList = new ArrayList<>();

    LineDataSet set1, set2;

    LineData data;

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_graph_enginespec);

        chart = findViewById(R.id.linechart);

        chart.setOnChartGestureListener(GraphEngineSpecs.this);
        chart.setOnChartValueSelectedListener(GraphEngineSpecs.this);

        //enable touch gestures
        chart.setTouchEnabled(true);

        //enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);

        //enable pinch zoom to avoid scaling x and y
        chart.setPinchZoom(true);

        //background
        chart.setBackgroundColor(Color.LTGRAY);

        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0f);
        left.setAxisMaximum(100f);
        left.setTextSize(13f);
        left.enableGridDashedLine(10f, 10f, 0f);

        YAxis left2 = chart.getAxisRight();
        left2.setEnabled(false);

        chart.getAxisRight().setEnabled(false);


        String[] vals = new String[] {"0s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "11s"};

        //get legend object
        Legend i = chart.getLegend();
        //cust legend
        i.setTextSize(15f);
        i.setForm(Legend.LegendForm.CIRCLE);
        i.setTextColor(Color.BLACK);

        XAxis x = chart.getXAxis();
        x.setTextSize(13f);
        x.setValueFormatter(new MyXAxisValueFormatter(vals));
        x.setGranularity(2);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);


        engineloadList.add(new Entry(0, 0));
        engineloadList.add(new Entry(1, 0));


        throttlePositionList.add(new Entry(0, 0));
        throttlePositionList.add(new Entry(1, 0));


        set1 = new LineDataSet(engineloadList, "Engine Load ");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);

        set2 = new LineDataSet(throttlePositionList, "Throttle Position ");
        set2.setFillAlpha(110);
        set2.setColor(Color.BLUE);
        set2.setLineWidth(3f);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(Color.BLACK);

        data = new LineData(set1, set2);

        chart.setData(data);

        downloadData();
        chart.notifyDataSetChanged();

    }

    private void downloadData()
    {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_graph);
        //Set the ArrayAdapter to the listview

        //DatabaseReference database = FirebaseDatabase.getInstance().getReference("/VehicleData/0").child("Bearing:");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("VehicleData");
        //ChildEventListener allows child events to be listened for
        database.addChildEventListener(new ChildEventListener()
        {
            //Will run when the app is started and when there is data added to the database
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
            {
                //Holds the Datasnapshot value of the database as type String
                VehicleData vehicleData = dataSnapshot.getValue(VehicleData.class);
                System.out.println("getEngineLoad: " + vehicleData.getEngineLoad());
                System.out.println("getThrottlePosition: " + vehicleData.getThrottlePosition());
                System.out.println("prevChildKey: " + prevChildKey);
                System.out.println("data.key" + dataSnapshot.getKey());
                //Add the info retrieved from datasnapshot into the ArrayList
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

        });
    }

    private void setData(int key, VehicleData vehicleData)
    {
        System.out.println("Using key: " + key);
        System.out.println("Setting Engine Load: " + vehicleData.getEngineLoad());
        engineloadList.add(new Entry(key + 2, Float.parseFloat(vehicleData.getEngineLoad())));

        System.out.println("Setting Throttle Position: " + vehicleData.getThrottlePosition());
        throttlePositionList.add(new Entry(key + 2, Float.parseFloat(vehicleData.getThrottlePosition())));

        set1.notifyDataSetChanged();
        data.notifyDataChanged();
        this.chart.notifyDataSetChanged();
        chart.invalidate();


    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{
        private String[] mVals;
        private MyXAxisValueFormatter(String[] vals)
        {
            this.mVals = vals;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mVals[(int)value];
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureStart: X:" + me.getX() + "Y:" + me.getY());
        Toast toast = Toast.makeText(this, "onChartGestureStart: X:" + me.getX() + "Y:" + me.getY(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 20);
        toast.show();
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i(TAG, "onChartGestureEnd: " + lastPerformedGesture);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i(TAG, "onChartLongPressed: ");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i(TAG, "onChartDoubleTapped: ");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i(TAG, "onChartSingleTapped: ");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i(TAG, "onChartFling: veloX: " + velocityX + "veloY" + velocityY);
        Toast.makeText(this, "onChartFling: veloX: " + velocityX + "veloY" + velocityY, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i(TAG, "onChartScale: ScaleX: " + scaleX + "ScaleY: " + scaleY);
        Toast.makeText(this, "onChartScale: ScaleX: " + scaleX + "ScaleY: " + scaleY, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i(TAG, "onChartTranslate: dX" + dX + "dY" + dY);
        Toast.makeText(this, "onChartTranslate: dX" + dX + "dY" + dY, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(TAG, "onValueSelected: " + e.toString());
        Toast.makeText(this, "onValueSelected: " + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "onNothingSelected: ");
    }

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }//End onCreateOptionMenu()

}