package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpec extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_spec);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("com.example.user.vel")
                .setApiKey("AIzaSyBvBtL81H7aiUK90c3QfVccoU1CowKrmAA")
                .setDatabaseUrl("https://finalyearproject-vel1-aac42.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(this, options, "secondary");

        FirebaseApp app = FirebaseApp.getInstance("secondary");
        FirebaseDatabase database2 = FirebaseDatabase.getInstance(app);
        DatabaseReference dbref = database2.getReference();
        DatabaseReference dbref2 = database2.getReference();
        DatabaseReference dbref3 = database2.getReference();
        dbref.child("Cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final List<String> Cars = new ArrayList<String>();
                for ( DataSnapshot suggestionSnap : dataSnapshot.getChildren())
                {
                    String suggestion = suggestionSnap.child("Make").getValue(String.class);
                    Cars.add(suggestion);
                }

                AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.auto);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleSpec.this, android.R.layout.simple_list_item_1, Cars);
                actv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbref2.child("Cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> Cars = new ArrayList<String>();
                for ( DataSnapshot suggestionSnap : dataSnapshot.getChildren())
                {
                    String suggestion = suggestionSnap.child("Model").getValue(String.class);
                    Cars.add(suggestion);
                }

                AutoCompleteTextView actv1 = (AutoCompleteTextView) findViewById(R.id.auto1);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(VehicleSpec.this, android.R.layout.simple_list_item_1, Cars);
                actv1.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbref3.child("Cars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> Cars = new ArrayList<String>();
                for (DataSnapshot suggestionSnap : dataSnapshot.getChildren()) {
                    String suggestion = suggestionSnap.child("Engine Size").getValue(String.class);
                    Cars.add(suggestion);
                }

                AutoCompleteTextView actv2 = (AutoCompleteTextView) findViewById(R.id.auto2);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(VehicleSpec.this, android.R.layout.simple_list_item_1, Cars);
                actv2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(VehicleSpec.this, Settings.class);
            startActivity(intent);
        }

        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(VehicleSpec.this, MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_help)
        {
            Intent intent = new Intent(VehicleSpec.this, UserHelp.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
