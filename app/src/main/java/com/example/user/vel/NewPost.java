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

public class NewPost extends AppCompatActivity {

    private ImageView newPartImage;
    private EditText partDesc;
    private ProgressBar progressBar;

    private Uri partImage = null;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private String current_user;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        current_user = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        newPartImage = findViewById(R.id.new_post_image);
        partDesc =  findViewById(R.id.new_post_desc);
        Button partAdd = findViewById(R.id.post_btn);
        progressBar = findViewById(R.id.progressbar);

        newPartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewPost.this);
            }
        });

        partAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = partDesc.getText().toString();

                if(!TextUtils.isEmpty(desc) && partImage != null){

                    progressBar.setVisibility(View.VISIBLE);

                    final String rand_name = UUID.randomUUID().toString();

                    final StorageReference filePath = storageReference.child("part_images").child(rand_name + ".jpg");
                    filePath.putFile(partImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri = Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                            if(task.isSuccessful()){

                                File newImageFile = new File(partImage.getPath());

                                try {
                                    compressedImageFile =  new Compressor(NewPost.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumb_data = baos.toByteArray();

                                UploadTask uploadTask = storageReference
                                        .child("part_images/thumbs").child(rand_name + ".jpg").putBytes(thumb_data);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        String download_thumUri = Objects.requireNonNull(taskSnapshot.getDownloadUrl()).toString();

                                        Map<String, Object> partMap =  new HashMap<>();
                                        partMap.put("image_url", downloadUri);
                                        partMap.put("image_thumb", download_thumUri);
                                        partMap.put("desc", desc);
                                        partMap.put("user_id", current_user);
                                        partMap.put("timestamp", FieldValue.serverTimestamp());

                                        firebaseFirestore.collection("Parts").add(partMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if(task.isSuccessful()){

                                                    Toast.makeText(NewPost.this, "Record has been added", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(NewPost.this, PostList.class);
                                                    startActivity(intent);
                                                    finish();

                                                } else {

                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                    Toast.makeText(NewPost.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();

                                                }

                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        String error = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(NewPost.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {

                                progressBar.setVisibility(View.INVISIBLE);
                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                Toast.makeText(NewPost.this, "FireStore Error: " + error, Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                partImage = result.getUri();
                newPartImage.setImageURI(partImage);

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
