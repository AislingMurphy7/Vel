package com.example.user.vel;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VehicleSpec extends AppCompatActivity
{
   /*String[] manufact={ "Alfa Romeo", "Aston Martin", "Audi", "Bentley", "BMW", "Bugatti", "Cadillac", "Chevrolet", "Chrysler", "Citroen", "Corvette", "DAF", "Dacia", "Daewoo", "Daihatsu",
            "Dodge", "Ferrari", "Fiat", "Ford", "Honda", "Hummer", "Hyundai", "Jaguar", "Jeep", "KIA", "Koenigsegg", "Lada", "Lamborghini", "Lancia", "Land Rover", "Lexus", "Lotus",
            "Maserati", "Mazda", "McLaren", "Mercedes-Benz", "Mini", "Mitsubishi", "Nissan", "Noble", "Opel", "Peugeot", "Pontiac", "Porsche", "Renault", "Rolls-Royce", "Rover", "Saab",
            "Seat", "Skoda", "Smart", "Subaru", "Suzuki", "Toyota", "Vauxhall", "Volkswagen", "Volvo"};

    String[] model={ "golf"};

    String[] year={"2001, 2002"};

    String[] engine={"1.6"};*/

   private static final String TAG = "VehicleSpec";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_spec);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("model");

        myRef.setValue("Audiii");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





    }

}
