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
    This class allows the user to add a new post to the forum within the app, Here
    they can add a picture and a simple description of what the picture is.
    This is then uploaded to FireBase and redisplayed when the user selects the
    social forum
 */

public class NewPost extends AppCompatActivity
{
    //XML variables
    private ImageView newPartImage;
    private EditText partDesc;
    private ProgressBar progressBar;

    //Image variable
    private Uri partImage = null;

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
        setContentView(R.layout.activity_new_post);

        //Instantiate variables
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //Gets current id from FireBase
        current_user = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        //XML variables
        newPartImage = findViewById(R.id.new_post_image);
        partDesc =  findViewById(R.id.new_post_desc);
        Button partAdd = findViewById(R.id.post_btn);
        progressBar = findViewById(R.id.progressbar);

        //When the user taps on the image
        newPartImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //They have to then crop the image to fit the application
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewPost.this);
            }//End onClick()
        });//End setOnClickListener()

        //When user selects the 'Add' button
        partAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The description is saved to a String
                final String desc = partDesc.getText().toString();

                //If the description and image is not empty
                if(!TextUtils.isEmpty(desc) && partImage != null)
                {
                    //Set progressbar to visible
                    progressBar.setVisibility(View.VISIBLE);

                    //A random name is generated or the image in FireBase
                    final String rand_name = UUID.randomUUID().toString();

                    //File path of the 'part_images' where the image is saved to in FireBase
                    final StorageReference filePath = storageReference.child("part_images").child(rand_name + ".jpg");
                    //The file is added to the filePath variable for FireBase
                    filePath.putFile(partImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
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
                                File newImageFile = new File(partImage.getPath());

                                //The app will then compress the file to make it smaller and easily handled
                                try
                                {
                                    compressedImageFile =  new Compressor(NewPost.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);

                                }//End try()
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }//End catch()

                                //Gets the data from the ImageView as bytes
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumb_data = baos.toByteArray();

                                //Uploads the data to the appropriate place on FireBase
                                UploadTask uploadTask = storageReference
                                        .child("part_images/thumbs").child(rand_name + ".jpg").putBytes(thumb_data);

                                //If upload is a success
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                    {

                                        //Gets download URI and changes to a string
                                        String download_thumUri = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

                                        //Create a Map with key: String, Value: Object
                                        Map<String, Object> partMap =  new HashMap<>();
                                        //Data is stored inside the HashMap()
                                        partMap.put("image_url", downloadUri);
                                        partMap.put("image_thumb", download_thumUri);
                                        partMap.put("desc", desc);
                                        partMap.put("user_id", current_user);
                                        partMap.put("timestamp", FieldValue.serverTimestamp());

                                        //New data is added to FireStore
                                        firebaseFirestore.collection("Parts").add(partMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task)
                                            {
                                                //If successful
                                                if(task.isSuccessful())
                                                {
                                                    //The user is informed that the record has been added, then redirected to the PostList page
                                                    Toast.makeText(NewPost.this, "Record has been added", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(NewPost.this, PostList.class);
                                                    startActivity(intent);
                                                    finish();
                                                }//End if()

                                                //If not successful
                                                else {
                                                    //Error message is displayed to the user informing them of the issue
                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                    Toast.makeText(NewPost.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(NewPost.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
                                    }//End OnFailure()
                                });//End addOnFailureListener()
                            }//End if()

                            //If not a success
                            else {
                                //Progressbar is set to invisible
                                progressBar.setVisibility(View.INVISIBLE);
                                //Error message is displayed to the user informing them of the issue
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(NewPost.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
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

        //If the const value is equal ro the requestCode
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            //The results will be gathered from the URI
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //If the resultCode is equal
            if (resultCode == RESULT_OK)
            {
                //Results are returned as a URI and set to variable
                partImage = result.getUri();
                //Set the imageView to the new image
                newPartImage.setImageURI(partImage);
            }//End if()
            //If the resultCode has an error
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                //Error message is displayed to the user informing them of the issue
                Toast.makeText(NewPost.this, "Application Error", Toast.LENGTH_LONG).show();
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
            Intent home_intent = new Intent(NewPost.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(NewPost.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        if (option_id == R.id.action_prof)
        {
            Intent prof_intent = new Intent(NewPost.this, UserProfile.class);
            startActivity(prof_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            finishAffinity();
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End NewPost()
