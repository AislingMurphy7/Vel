/*This class controls the users selection of vehicle(s) from the
* Firebase Database, it reads the values from a pre-populated DB
* with values of 'Make', 'Model' & 'Engine Size'. This data is read
* and placed into dropdown menus, which the user can select from*/
package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpec extends AppCompatActivity
{
    RecyclerView recview;
    MyAdapter adpt;
    List<MyDataset> listdata;
    FirebaseDatabase FDB;
    DatabaseReference DBR;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_vehicle_spec);

        recview = (RecyclerView)findViewById(R.id.rec_view);

        recview.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        recview.setLayoutManager(LM);
        recview.setItemAnimator(new DefaultItemAnimator());
        recview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        listdata = new ArrayList<>();
        adpt = new MyAdapter(listdata);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("com.example.user.vel")
                .setApiKey("AIzaSyBvBtL81H7aiUK90c3QfVccoU1CowKrmAA")
                .setDatabaseUrl("https://finalyearproject-vel1-aac42.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(this, options, "secondary");

        FirebaseApp app = FirebaseApp.getInstance("secondary");
        FDB = FirebaseDatabase.getInstance(app);
        GetDataFirebase();

        /*FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("com.example.user.vel")
                .setApiKey("AIzaSyBvBtL81H7aiUK90c3QfVccoU1CowKrmAA")
                .setDatabaseUrl("https://finalyearproject-vel1-aac42.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(this, options, "secondary");

       /* FirebaseApp app = FirebaseApp.getInstance("secondary");
        FirebaseDatabase database2 = FirebaseDatabase.getInstance(app);
        GetDataFirebase();
        /*
        DatabaseReference dbref = database2.getReference();
        DatabaseReference dbref2 = database2.getReference();
        DatabaseReference dbref3 = database2.getReference(); */

        /*
        dbref.child("Cars").addValueEventListener(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final List<String> Cars = new ArrayList<String>();

                for ( DataSnapshot suggestionSnap : dataSnapshot.getChildren() )
                {
                    String suggestion = suggestionSnap.child("Make").getValue(String.class);
                    Cars.add(suggestion);

                }//End For()

                //XML TextView variable
                TextView actv = (TextView) findViewById(R.id.auto);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleSpec.this, android.R.layout.simple_list_item_1, Cars);
                actv.setAdapter(adapter);

            }//End onDataChange()

            public void onCancelled(DatabaseError databaseError)
            {

            }//End onCancelled()

        });//End dbref ValueEventListener()

        //
        dbref2.child("Cars").addValueEventListener(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final List<String> Cars = new ArrayList<String>();

                for ( DataSnapshot suggestionSnap : dataSnapshot.getChildren() )
                {
                    String suggestion = suggestionSnap.child("Model").getValue(String.class);
                    Cars.add(suggestion);
                }//End for()

                //XML TextView variable
                TextView actv1 = (TextView) findViewById(R.id.auto1);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(VehicleSpec.this, android.R.layout.simple_list_item_1, Cars);
                actv1.setAdapter(adapter1);

            }//End onDataChange()

            public void onCancelled(DatabaseError databaseError)
            {

            }//End onCancelled()

        });//End dbref2 ValueEventListener()

        //
        dbref3.child("Cars").addValueEventListener(new ValueEventListener()
        {
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final List<String> Cars = new ArrayList<String>();

                for (DataSnapshot suggestionSnap : dataSnapshot.getChildren())
                {
                    String suggestion = suggestionSnap.child("Engine Size").getValue(String.class);
                    Cars.add(suggestion);
                }//End for()

                //XML TextView variable
                TextView actv2 = (TextView) findViewById(R.id.auto2);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(VehicleSpec.this, android.R.layout.simple_list_item_1, Cars);
                actv2.setAdapter(adapter2);

            }//End onDataChange()

            public void onCancelled(DatabaseError databaseError)
            {

            }//End onCancelled()

        });//End dbref3 ValueEventListener()*/

    }//End onCreate()

    void GetDataFirebase(){
        DBR = FDB.getReference("Cars");

        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyDataset data = dataSnapshot.getValue(MyDataset.class);
                listdata.add(data);
                recview.setAdapter(adpt);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {
        List<MyDataset> listArray;
        public MyAdapter(List<MyDataset> List){
            this.listArray = List;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview, parent,false);

            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            MyDataset data = listArray.get(position);

            holder.mytext.setText(data.getmake());
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView mytext;

            public MyViewHolder(View itemView) {
                super(itemView);

                mytext = (TextView)itemView.findViewById(R.id.textview);
            }
        }

        @Override
        public int getItemCount() {
            return listArray.size();
        }
    }

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
            Intent intent = new Intent(VehicleSpec.this, Settings.class);
            startActivity(intent);

        }//End if()

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

        return super.onOptionsItemSelected(item);

    }//End onOptionsItemSelected()

}//End VehicleSpec()
