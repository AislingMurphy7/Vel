package com.example.user.vel;

/*
    This class is used as a step of security for the
    vehicles data stored within the app. Each vehicles
    data is password protected with the password belonging
    to the user
 */
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

public class PasswordProtected extends AppCompatActivity
{
    //Declare FireBase object
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private boolean isChanged = false;

    private String user_id;
    private ImageView imageView;
    private Uri uriImage;
    private Button cont;
    public EditText ent_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_password_protected);

        //Instantiate FireBase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        imageView = findViewById(R.id.image);
        cont = findViewById(R.id.cont);
        ent_pass = findViewById(R.id.editTextPassword);




        firebaseFirestore.collection("Vehicles").document("dNH7sMf5Y311ReBfgAyq").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            //When the process is complete
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //If process is successful
                if (task.isSuccessful()) {
                    //If the record exists
                    if (task.getResult().exists()) {
                        //images are recorded
                        String image = task.getResult().getString("image_url");

                        uriImage = Uri.parse(image);

                        //Sets the placeholder image
                        RequestOptions options = new RequestOptions();

                        //Loads the image and placeholder from FireBase into the page
                        Glide.with(PasswordProtected.this).setDefaultRequestOptions(options).load(image).into(imageView);
                    }//End if()
                }//End if()
                //If the record doesn't exist
                else
                {
                    //Error message is displayed to the user
                    String error = task.getException().getMessage();
                    Toast.makeText(PasswordProtected.this, "Data retrieve Error: " + error, Toast.LENGTH_LONG).show();
                }//End else()
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String enter_pass = ent_pass.getText().toString();

                firebaseFirestore.collection("Vehicles").document("dNH7sMf5Y311ReBfgAyq").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        //If process is successful
                        if (task.isSuccessful()) {
                            //If the record exists
                            if (task.getResult().exists()) {
                                //images are recorded
                                String pass = task.getResult().getString("password");
                                if (enter_pass == pass) {
                                    Intent intent = new Intent(PasswordProtected.this, Homepage.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(PasswordProtected.this, "ERROR", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

    }


}//End PasswordProtected()
