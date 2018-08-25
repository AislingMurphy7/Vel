package com.example.user.vel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;


/*
    This class allows the user to add a new vehicle to the app, Here
    they add a picture and  the make, model and registration of the vehicle
    This is then uploaded to FireBase and redisplayed when the user selects the
    vehicle information
 */

public class NewVehicle extends AppCompatActivity
{
    //XML variables
    private ImageView newVehicleImage;
    private EditText vehicleMake, vehicleModel, vehicleReg, password, con_password, engineSize;
    private ProgressDialog progress;

    //Image variable
    private Uri vehicleImage = null;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_new_vehicle);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Vehicles");

        //XML variables
        newVehicleImage = findViewById(R.id.new_vehicle_image);
        vehicleMake =  findViewById(R.id.new_vehicle_make);
        vehicleModel =  findViewById(R.id.new_vehicle_model);
        vehicleReg =  findViewById(R.id.new_vehicle_reg);
        engineSize =  findViewById(R.id.new_vehicle_engine);
        Button vehicleAdd = findViewById(R.id.save_btn);
        password = findViewById(R.id.password);
        con_password = findViewById(R.id.con_password);

        progress = new ProgressDialog(this);

        //When the user taps on the image
        newVehicleImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //They have to then crop the image to fit the application
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewVehicle.this);
            }//End onClick()
        });//End setOnClickListener()

        //When user selects the 'Add' button
        vehicleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartPosting();
            }
        });

    }//End onCreate()

    private void StartPosting()
    {
        progress.setMessage("Posting to Vehicle list");

        //The make, model and reg is saved to Strings
        final String make = vehicleMake.getText().toString().trim();
        final String model = vehicleModel.getText().toString().trim();
        final String reg = vehicleReg.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String engine = engineSize.getText().toString().trim();
        final String con_pass = con_password.getText().toString().trim();

        //If the description and image is not empty
        if(!TextUtils.isEmpty(make) &&  !TextUtils.isEmpty(model) && !TextUtils.isEmpty(reg) &&
                !TextUtils.isEmpty(engine) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(con_pass) && vehicleImage != null) {

            //Only when user has entered information will the progress be shown
            progress.show();

            //File path of the 'vehicle_images' where the image is saved to in FireBase
            StorageReference filePath = storageRef.child("vehicle_images").child(vehicleImage.getLastPathSegment());

            filePath.putFile(vehicleImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newpost = databaseRef.push();

                    newpost.child("Make").setValue(make);
                    newpost.child("Model").setValue(model);
                    newpost.child("Reg").setValue(reg);
                    newpost.child("Password").setValue(pass);
                    newpost.child("Engine").setValue(engine);
                    newpost.child("Confirmed_Password").setValue(con_pass);
                    newpost.child("Image").setValue(Objects.requireNonNull(downloadUrl).toString());

                    progress.dismiss();

                    startActivity(new Intent(NewVehicle.this, VehicleList.class));
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //If the const value is equal ro the requestCode
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            //The results will be gathered from the URI
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //If the resultCode is equal
            if (resultCode == RESULT_OK)
            {
                //Results are returned as a URI and set to variable
                vehicleImage = result.getUri();
                //Set the imageView to the new image
                newVehicleImage.setImageURI(vehicleImage);
            }//End if()
            //If the resultCode has an error
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                //Error message is displayed to the user informing them of the issue
                Toast.makeText(NewVehicle.this, "Application Error", Toast.LENGTH_LONG).show();
            }//End else if()
        }//End if()
    }//End onActivityResult()
















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
            Intent home_intent = new Intent(NewVehicle.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(NewVehicle.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(NewVehicle.this, UserProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            Intent exit_intent = new Intent(Intent.ACTION_MAIN);
            exit_intent.addCategory(Intent.CATEGORY_HOME);
            exit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exit_intent);
            android.os.Process.killProcess(android.os.Process.myPid());
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End NewVehicle()
