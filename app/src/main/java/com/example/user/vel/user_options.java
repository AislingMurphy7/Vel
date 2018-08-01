package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }//End onCreateOptionsMenu()

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        if (option_id == R.id.action_home)
        {
            Toast.makeText(user_options.this, R.string.options_page, Toast.LENGTH_LONG).show();
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(user_options.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //The user is already located within this screen
        if (option_id == R.id.action_prof)
        {
            Intent help_intent = new Intent(user_options.this, userProfile.class);
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
}
