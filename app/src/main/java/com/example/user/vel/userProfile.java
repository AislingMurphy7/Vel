package com.example.user.vel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/*
    This class allows the user to edit their profile by changing their profile picture which they
    choose from the camera gallery, they can also edit their display name.
 */

public class userProfile extends AppCompatActivity
{

    private static final int CHOOSE_IMAGE = 101;

    //Declare variables
    TextView textView;
    ImageView imageView;
    EditText editText;
    Uri uriProfileImage;
    ProgressBar progressbar;
    String profileImageUrl;

    //Declare FireBase object
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_user_profile);

        //Instantiate mAuth
        mAuth = FirebaseAuth.getInstance();

        //XML variables
        editText = findViewById(R.id.editTextDisplayName);
        imageView = findViewById(R.id.imageView);
        progressbar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.textViewVerified);

        //When image is clicked on the showImageChooser function will be ran
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showImageChooser();
            }//End onClick()
        });//End setOnClickListener()

        //LoadUserInfo funstion will be ran after the user chooses an image
        loadUserInfo();

        /*When the user has loaded all their information & they tap the save button, saveUserInfo()
         will be ran*/
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveUserInfo();
            }//End onClick()
        });//End setOnClickListener()
    }//End onCreate()

    @Override
    //When the activity opens
    protected void onStart()
    {
        super.onStart();

        //If the user is = to null, they are not logged in
        if(mAuth.getCurrentUser() == null)
        {
            //The activity will finish
            finish();
            //The user will be sent to the login screen
            startActivity(new Intent(this, LoginUser.class));
        }//End if()
    }//End onStart()

    private void loadUserInfo()
    {
        //GEt the user from FireBase
        final FirebaseUser user = mAuth.getCurrentUser();

        //If the user is not null
        if(user != null)
        {
            //If URL is not null
            if (user.getPhotoUrl() != null)
            {
                //Profile picture is added to the ImageView on screen
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }//End if()

            //If display name is not null
            if (user.getDisplayName() != null)
            {
                //Users display name is set
                editText.setText(user.getDisplayName());
            }//End if()

            //If the users email has been verified via email
            if(user.isEmailVerified())
            {
                //Message is displayed
                textView.setText(R.string.email_verified);
            }//End if()
            else
                {
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
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Toast.makeText(userProfile.this, R.string.email_sent, Toast.LENGTH_SHORT).show();
                                }//End onComplete()
                            });//End sendEmailVerification()
                        }//End onClick()
                    });//End setOnClickListener()
                }//End else()
        }//End if()
    }//End loadUserInfo()

    private void saveUserInfo()
    {
        //Take the name entered into the EditText and place it into a string
        String displayname = editText.getText().toString();

        //If the EditText is empty
        if(displayname.isEmpty())
        {
            //An error message is displayed
            editText.setError(getText(R.string.displayname_empt));
            //Show error
            editText.requestFocus();
            return;
        }//End if()

        //Get current user from FireBase
        FirebaseUser user = mAuth.getCurrentUser();

        //If user and image URL is not mull
        if(user != null && profileImageUrl != null)
        {
            //User updated information is requested for change in FireBase
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayname)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            //Users information is changed
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        //When execution is complete
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            //If the task was successful
                            if(task.isSuccessful())
                            {
                                //Message is displayed to the user
                                Toast.makeText(userProfile.this, R.string.prof_updated, Toast.LENGTH_SHORT).show();
                            }//End if()
                        }//End onComplete()
                    });//End addOnCompleteListener()
        }//End if()
    }//End saveUserInfo()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /*
          If the const value is = to requestcode
          result code is = to result_ok
          if the data is not null
         */
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            //URI of image is stored
            uriProfileImage = data.getData();

            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                //method to store image in FireBase
                uploadImageToFirebaseStorage();
                
            }//End try()
            catch (IOException e)
            {
                e.printStackTrace();
            }//End catch()
        }//End if()
    }//End onActivityResult()

    private void uploadImageToFirebaseStorage()
    {
        StorageReference profileimageRef =
                //FireBase reference to store images in 'profilepics' as .jpg
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis() + ".jpg");

        //If imafe is not null
        if(uriProfileImage != null)
        {
            //Sets the progressbar to visible on screen
            progressbar.setVisibility(View.VISIBLE);
            //Image is added to FireBase
            profileimageRef.putFile(uriProfileImage)
                    //Records if the upload is a success or not
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        //If the upload occurred successfully
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            //Sets the progressbar to invisible on screen
                            progressbar.setVisibility(View.GONE);
                            //Download URL is added to the string
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }//End onSuccess()
                    })//End addOnSuccessListener()

                    //If the upload occurred unseccessfully
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            //Sets the progressbar to invisible on screen
                            progressbar.setVisibility(View.GONE);
                            //Displays the appropriate error message
                            Toast.makeText(userProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }//End onFailure()
                    });//End addOnFailureListener()
        }//End if()
    }//End uploadImageToFirebaseStorage()

    private void showImageChooser()
    {
        //Create Intent
        Intent intent = new Intent();
        //Set type to Image
        intent.setType("image/*");
        //Set action of Intent
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Creates a file called Chooser and set title
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }//End showImageChooser()

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
        //If the language option is selected, user will be re-directed to language screen
        if (option_id == R.id.action_Language)
        {
            Intent Language_intent = new Intent(userProfile.this, LanguageSelect.class);
            startActivity(Language_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(userProfile.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //The user is already located within this screen
        if (option_id == R.id.action_prof)
        {
            Toast.makeText(userProfile.this, R.string.in_prof, Toast.LENGTH_LONG).show();
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
}//End userProfile()
