package com.example.user.vel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/*
    This class allows the user to edit their profile by changing their profile picture which they
    choose from the camera gallery, they can also edit their display name.
 */

public class UserProfile extends AppCompatActivity {
    //Declare variables
    private TextView textView;
    private CircleImageView imageView;
    private EditText editText;
    private Uri uriProfileImage = null;
    private Button buttonSave;
    private ProgressBar progressbar;
    private String user_id;
    private boolean isChanged = false;

    //Declare FireBase object
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_user_profile);

        //Instantiate FireBase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        //XML variables
        editText = findViewById(R.id.editTextDisplayName);
        imageView = findViewById(R.id.imageView);
        buttonSave = findViewById(R.id.buttonSave);
        progressbar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textViewVerified);

        //Set the progressbar to visible
        progressbar.setVisibility(View.VISIBLE);
        //Disable the save button
        buttonSave.setEnabled(false);

        //The collection within FireBase is accessed
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            //When the process is complete
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                //If process is successful
                if(task.isSuccessful())
                {
                    //If the record exists
                    if(task.getResult().exists())
                    {
                        //Name and images are recorded
                        String name = task.getResult().getString("name");
                        String image =  task.getResult().getString("image");

                        uriProfileImage = Uri.parse(image);

                        //Set the username
                        editText.setText(name);

                        //Sets the placeholder image
                        RequestOptions options = new RequestOptions();

                        //Loads the image and placeholder from FireBase into the page
                        Glide.with(UserProfile.this).setDefaultRequestOptions(options).load(image).into(imageView);
                    }//End if()

                }//End if()
                //If the record doesn't exist
                else
                {
                    //Error message is displayed to the user
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(UserProfile.this, "Data retrieve Error: " + error, Toast.LENGTH_LONG).show();
                }//End else()

                //Progressbar is set to invisible
                progressbar.setVisibility(View.INVISIBLE);
                //Save button is enabled
                buttonSave.setEnabled(true);
            }//End onComplete()
        });//End OnCompleteListener()

        //Calls function
        EmailVerifiy();

        //When the save button is clicked
        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //name is saved to a string
                final String displayname = editText.getText().toString();

                //If the name and the image are not empty
                if (!TextUtils.isEmpty(displayname) && uriProfileImage != null)
                {
                    //Set progessBar to visible
                    progressbar.setVisibility(View.VISIBLE);

                    //If the data is changed
                    if(isChanged)
                    {
                        //User ID is gathered
                        user_id = mAuth.getCurrentUser().getUid();

                        //Path to the profile_images storage is stated
                        StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");

                        //Image is saved to FireBase
                        image_path.putFile(uriProfileImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            //When the process is complete
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                            {
                                //If the process is successful
                                if (task.isSuccessful())
                                {
                                    //Data is saved to FireBase
                                    storeFirestore(task, displayname);
                                }//End if()
                                //If process is unsuccessful
                                else {

                                    //Error message is displayed to the user
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(UserProfile.this, "Image Error: " + error, Toast.LENGTH_LONG).show();

                                    //ProgressBar is set to invisible
                                    progressbar.setVisibility(View.INVISIBLE);
                                }//End else()
                            }//End onComplete()
                        });//End OnCompleteListener()
                    }//End if()
                    else{
                        storeFirestore(null, displayname);
                    }//End else()
                }//End if()
            }//End onClick()
        });//End onClickListener()

        //When image is clicked on the showImageChooser function will be ran
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //If the SDK_INT is gather than or equal to the version code
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    //If the user denies access
                    if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        //An error message is displayed to the user
                        Toast.makeText(UserProfile.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }//End if()
                    //If permission is granted
                    else {
                        //Function is ran
                        ImagePick();
                    }//End else()
                }//End if()
                else
                {
                    ImagePick();
                }//End else()
            }//End onClick()
        });//End setOnClickListener()
    }//End onCreate()

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String displayname)
    {
        //Declare variable
        Uri download_uri;

        //Check if the download URI is still stored
        //Download URI is present
        download_uri = task.getResult().getDownloadUrl();

        //Create a Map with key: String, Value: String
        Map<String, String> userMap = new HashMap<>();
        //Data is stored inside the HashMap()
        userMap.put("name", displayname);
        //toString converts URI to a string
        userMap.put("image", Objects.requireNonNull(download_uri).toString());

        //The collection within FireBase is accessed
        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            //When process is complete
            public void onComplete(@NonNull Task<Void> task)
            {
                //If the process is successful
                if(task.isSuccessful())
                {
                    //User is informed their profile is created
                    Toast.makeText(UserProfile.this, "Profile set up", Toast.LENGTH_LONG).show();
                    //The user is redirected to the Homepage
                    Intent intent = new Intent(UserProfile.this, Homepage.class);
                    startActivity(intent);
                    finish();
                }//End if()
                //If not successful
                else {

                    //Error message is displayed to the user
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(UserProfile.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
                }//End else()

                //ProgressBar is set to invisible
                progressbar.setVisibility(View.INVISIBLE);
            }//End onComplete()
        });//End OnCompleteListener()
    }//End storeFireStore()

    //Allows the user is picking an image
    private void ImagePick()
    {
        //Crops the chosen image
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(UserProfile.this);
    }//End ImagePick()

    //Check is email is verified()
    private void EmailVerifiy()
    {
        //Gets the user from FireBase
        final FirebaseUser user = mAuth.getCurrentUser();

        //If the users email has been verified via email
        if (Objects.requireNonNull(user).isEmailVerified())
        {
            //Message is displayed
            textView.setText(R.string.email_verified);
        }//End if()
        //If not
        else {
            //If the email is not verified the below message is displayed
            textView.setText(R.string.email_NotVerified);
            //If the user tap on the text
            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //A verification email is sent to the email address
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        //One the email is sent a toast message is displayed
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UserProfile.this, R.string.email_sent, Toast.LENGTH_SHORT).show();
                        }//End onComplete()
                    });//End sendEmailVerification()
                }//End onClick()
            });//End setOnClickListener()
        }//End else()
    }//End if()

    @Override
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
                uriProfileImage = result.getUri();
                //Set the imageView to the new image
                imageView.setImageURI(uriProfileImage);
                isChanged = true;
            }//End if()
            //If the resultCode has an error
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                //Error exception is created
                Exception error = result.getError();
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
        //If the home option is selected
        if (option_id == R.id.action_home)
        {
            //The user will be re-directed to home screen
            Intent help_intent = new Intent(UserProfile.this, Homepage.class);
            startActivity(help_intent);
        }//End if()

        //If the help option is selected
        if (option_id == R.id.action_help)
        {
            //The user will be re-directed to help screen
            Intent help_intent = new Intent(UserProfile.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the profile option is selected
        if (option_id == R.id.action_prof)
        {
            //The user be informed they are already in their profile
            Toast.makeText(UserProfile.this, R.string.in_prof, Toast.LENGTH_LONG).show();
        }//End if()

        //If the exit option is selected
        if (option_id == R.id.action_exit)
        {
            //The app will close
            Intent exit_intent = new Intent(Intent.ACTION_MAIN);
            exit_intent.addCategory(Intent.CATEGORY_HOME);
            exit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exit_intent);
            finish();
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End userProfile()
