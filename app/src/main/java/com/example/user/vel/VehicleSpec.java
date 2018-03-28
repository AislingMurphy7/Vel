package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class VehicleSpec extends AppCompatActivity
{
    String[] manufact={ "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Bugatti", "Cadillac", "Chevrolet", "Chrysler", "Citroen", "Corvette", "DAF", "Dacia", "Daewoo", "Daihatsu",
            "Dodge", "Ferrari", "Fiat", "Ford", "Honda", "Hummer", "Hyundai", "Jaguar", "Jeep", "KIA", "Koenigsegg", "Lada", "Lamborghini", "Lancia", "Land Rover", "Lexus", "Lotus",
            "Maserati", "Mazda", "McLaren", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Noble", "Opel", "Peugeot", "Pontiac", "Porsche", "Renault", "Rolls-Royce", "Rover", "Saab",
            "Seat", "Skoda", "Smart", "Subaru", "Suzuki", "Toyota", "Vauxhall", "Volkswagen", "Volvo"};

    String[] model={ "golf"};

    String[] year={"2001, 2002"};

    String[] engine={"1.6"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_spec);

        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.auto);
        AutoCompleteTextView actv1 = (AutoCompleteTextView) findViewById(R.id.auto1);
        AutoCompleteTextView actv2 = (AutoCompleteTextView) findViewById(R.id.auto2);
        AutoCompleteTextView actv3 = (AutoCompleteTextView) findViewById(R.id.auto3);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, manufact);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, model);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, year);
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, engine);

        actv.setAdapter(adapter);
        actv.setThreshold(1);
        actv1.setAdapter(adapter1);
        actv1.setThreshold(1);
        actv2.setAdapter(adapter1);
        actv2.setThreshold(1);
        actv3.setAdapter(adapter1);
        actv3.setThreshold(1);

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
