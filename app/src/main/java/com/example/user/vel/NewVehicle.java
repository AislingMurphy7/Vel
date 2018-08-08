package com.example.user.vel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import id.zelory.compressor.Compressor;

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
    private EditText vehicleMake;
    private EditText vehicleModel;
    private EditText vehicleReg;
    private ProgressBar progressBar;

    //Image variable
    private Uri vehicleImage = null;

    //FireBase variables
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private String current_user;

    //Thumbnail variable
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_new_vehicle);

        //Instantiate variables
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //Gets current id from FireBase
        current_user = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        //XML variables
        newVehicleImage = findViewById(R.id.new_vehicle_image);
        vehicleMake =  findViewById(R.id.new_vehicle_make);
        vehicleModel =  findViewById(R.id.new_vehicle_model);
        vehicleReg =  findViewById(R.id.new_vehicle_reg);
        Button vehicleAdd = findViewById(R.id.save_btn);
        progressBar = findViewById(R.id.progressbar);

        //When the user taps on the image
        newVehicleImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewVehicle.this);
            }//End onClick()
        });//End setOnClickListener()

        //When user selects the 'Add' button
        vehicleAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The make, model and reg is saved to Strings
                final String make = vehicleMake.getText().toString();
                final String model = vehicleModel.getText().toString();
                final String reg = vehicleReg.getText().toString();

                //If the description and image is not empty
                if(!TextUtils.isEmpty(make) &&  !TextUtils.isEmpty(model) && !TextUtils.isEmpty(reg) && vehicleImage != null)
                {
                    //Set progressbar to visible
                    progressBar.setVisibility(View.VISIBLE);

                    //A random name is generated or the image in FireBase
                    final String rand_name = UUID.randomUUID().toString();

                    //File path of the 'vehicle_images' where the image is saved to in FireBase
                    final StorageReference filePath = storageReference.child("vehicle_images").child(rand_name + ".jpg");
                    //The file is added to the filePath variable for FireBase
                    filePath.putFile(vehicleImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                        {
                            //DownloadUrl is added to the variable
                            final String downloadUri = Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                            //If successful
                            if(task.isSuccessful())
                            {
                                //The image and path are saved into variable
                                File newImageFile = new File(vehicleImage.getPath());

                                //The app will then compress the file to make it smaller and easily handled
                                try
                                {
                                    compressedImageFile =  new Compressor(NewVehicle.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);

                                }//End try()
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }//End catch()

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumb_data = baos.toByteArray();

                                UploadTask uploadTask = storageReference
                                        .child("vehicle_images/thumbs").child(rand_name + ".jpg").putBytes(thumb_data);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        String download_thumUri = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

                                        Map<String, Object> vehicleMap =  new HashMap<>();
                                        vehicleMap.put("image_url", downloadUri);
                                        vehicleMap.put("image_thumb", download_thumUri);
                                        vehicleMap.put("make", make);
                                        vehicleMap.put("model", model);
                                        vehicleMap.put("reg", reg);
                                        vehicleMap.put("user_id", current_user);
                                        vehicleMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Vehicles").add(vehicleMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task)
                                            {
                                                //If successful
                                                if(task.isSuccessful())
                                                {
                                                    //The user is informed that the record has been added, then redirected to the PostList page
                                                    Toast.makeText(NewVehicle.this, "Record has been added", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(NewVehicle.this, VehicleList.class);
                                                    startActivity(intent);
                                                    finish();
                                                }//End if()

                                                //If not successful
                                                else {
                                                    //Error message is displayed to the user informing them of the issue
                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                    Toast.makeText(NewVehicle.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
                                                }//End else()

                                                //Sets progressbar to invisible
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }//End onComplete()
                                        });//End onCompleteListener()
                                    }//End OnSuccess()

                                    //If the process fails
                                }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        //Error message is displayed to the user informing them of the issue
                                        String error = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(NewVehicle.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
                                    }//End OnFailure()
                                });//End addOnFailureListener()
                            }//End if()

                            //If not a success
                            else {
                                //Progressbar is set to invisible
                                progressBar.setVisibility(View.INVISIBLE);
                                //Error message is displayed to the user informing them of the issue
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(NewVehicle.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
                            }//End else()
                        }//End onComplete()
                    });//End onCompleteListener()
                }//End if()
            }//End onClick()
        });//End setOnClickListener()
    }//End onCreate()

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                vehicleImage = result.getUri();
                newVehicleImage.setImageURI(vehicleImage);

            }//End if()
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
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
            finish();
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End NewVehicle()
