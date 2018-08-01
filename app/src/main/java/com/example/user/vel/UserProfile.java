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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_user_profile);

        //Instantiate mAuth
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

        progressbar.setVisibility(View.VISIBLE);
        buttonSave.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {

                    if(task.getResult().exists())
                    {
                        String name = task.getResult().getString("name");
                        String image =  task.getResult().getString("image");

                        uriProfileImage = Uri.parse(image);

                        editText.setText(name);

                        RequestOptions options = new RequestOptions();

                        Glide.with(UserProfile.this).setDefaultRequestOptions(options).load(image).into(imageView);
                    }

                }else
                {
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(UserProfile.this, "Data retrieve Error: " + error, Toast.LENGTH_LONG).show();
                }

                progressbar.setVisibility(View.INVISIBLE);
                buttonSave.setEnabled(true);
            }
        });

        EmailVerifiy();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String displayname = editText.getText().toString();

                if (!TextUtils.isEmpty(displayname) && uriProfileImage != null)
                {

                     progressbar.setVisibility(View.VISIBLE);

                     if(isChanged) {

                            user_id = mAuth.getCurrentUser().getUid();

                            StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");

                            image_path.putFile(uriProfileImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        storeFirestore(task, displayname);

                                    } else {
                                        String error = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(UserProfile.this, "Image Error: " + error, Toast.LENGTH_LONG).show();

                                        progressbar.setVisibility(View.INVISIBLE);
                                    }

                                }
                        });
                    } else{
                         storeFirestore(null, displayname);
                     }
                }
            }
        });

        //When image is clicked on the showImageChooser function will be ran
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ContextCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(UserProfile.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        ImagePick();
                    }
                }
                else
                {
                    ImagePick();
                }
            }//End onClick()

        });//End setOnClickListener()
    }//End onCreate()

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String displayname)
    {
        Uri download_uri;

        download_uri = task.getResult().getDownloadUrl();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", displayname);
        userMap.put("image", Objects.requireNonNull(download_uri).toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(UserProfile.this, "Profile set up", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserProfile.this, NewPost.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(UserProfile.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();

                }

                progressbar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void ImagePick() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(UserProfile.this);
    }


    private void EmailVerifiy() {
        //GEt the user from FireBase
        final FirebaseUser user = mAuth.getCurrentUser();

        //If the users email has been verified via email
        if (Objects.requireNonNull(user).isEmailVerified()) {
            //Message is displayed
            textView.setText(R.string.email_verified); }//End if()
            else {
                //If the email is not verified the below message is displayed
                textView.setText(R.string.email_NotVerified);
                //If the user tap on the text
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //A verification email is sent to the email address
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
          If the const value is = to requestcode
          result code is = to result_ok
          if the data is not null
         */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                uriProfileImage = result.getUri();
                imageView.setImageURI(uriProfileImage);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
            Intent home_intent = new Intent(UserProfile.this, Homepage.class);
            startActivity(home_intent);
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(UserProfile.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //The user is already located within this screen
        if (option_id == R.id.action_prof)
        {
            Toast.makeText(UserProfile.this, R.string.in_prof, Toast.LENGTH_LONG).show();
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
}//End UserProfile()
