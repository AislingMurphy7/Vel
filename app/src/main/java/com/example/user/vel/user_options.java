package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class user_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_options);

        Button general_info = findViewById(R.id.Informationbtn);
        Button device_btn = findViewById(R.id.Devicebtn);
        Button vehicle_info = findViewById(R.id.Vehiclebtn);
        Button edit_prof = findViewById(R.id.EditProfbtn);
        Button social_fourm = findViewById(R.id.FourmBtn);

        general_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent general_info = new Intent(user_options.this, Agreements.class);
                startActivity(general_info);
            }
        });

        device_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent device = new Intent(user_options.this, SetupELM.class);
                startActivity(device);
            }
        });

        vehicle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vehicle_info = new Intent(user_options.this, VehicleSpec.class);
                startActivity(vehicle_info);
            }
        });

        edit_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(user_options.this, userProfile.class);
                startActivity(profile);
            }
        });

        social_fourm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent social = new Intent(user_options.this, PartList.class);
                startActivity(social);
            }
        });
    }
}
