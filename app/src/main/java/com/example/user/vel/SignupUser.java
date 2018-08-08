package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

/*
    This class allows the user to create an account.
    It contains two edit texts which allows the user to inout their
    email address and password, a sign up button and finally clickable text which redirects
    the user to the login page if they already have an account created.
    The account information is stored in FireBase
 */

public class SignupUser extends Activity implements View.OnClickListener
{
    //Declare FireBase object
    private FirebaseAuth mAuth;

    //Define variables
    ProgressBar progressbar;
    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Removes actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_signup_user);

        //XML variables
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressbar = findViewById(R.id.progressbar);

        //Instantiate mAuth
        mAuth = FirebaseAuth.getInstance();

        //Attach onClickListener to sign up button
        findViewById(R.id.buttonSignup).setOnClickListener(this);
        //Attach OnClickListener to TextView
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }//End OnCreate()

    //Validate the user information
    private void registerUser()
    {
        //Gather the input information
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //If the email EditText is empty
        if(email.isEmpty())
        {
            //An error message is displayed
            editTextEmail.setError(getText(R.string.email_need));
            //Show error
            editTextEmail.requestFocus();
            return;
        }//End if()

        //If the email entered does not match the layout of an email (eg, a@gmail.com)
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            //An error message is displayed
            editTextEmail.setError(getText(R.string.ent_valid_email));
            //Show error
            editTextEmail.requestFocus();
            return;
        }//End if()

        //If the password EditText is empty
        if(password.isEmpty())
        {
            //An error message is displayed
            editTextPassword.setError(getText(R.string.pass_empt));
            //Show error
            editTextPassword.requestFocus();
            return;
        }//End if()

        //If the password length is less than six
        if(password.length()<6)
        {
            //An error message is displayed
            editTextPassword.setError(getText(R.string.mini_length));
            //Show error
            editTextPassword.requestFocus();
            return;
        }//End if()

        //Sets the progressbar to visible on screen
        progressbar.setVisibility(View.VISIBLE);

        //Creating the user in FireBase
        mAuth.createUserWithEmailAndPassword(email, password)
                //Detects the completion
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //Sets the progressbar to invisible on screen
                        progressbar.setVisibility(View.GONE);
                        //If user creation was successful
                        if(task.isSuccessful())
                        {
                            //Clear previous pages
                            finish();
                            //Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), R.string.reg_user, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupUser.this, UserProfile.class));

                        }//End if()
                        else
                            {
                                //If the user already exists within FireBase
                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                {
                                    //Message is displayed
                                    Toast.makeText(getApplicationContext(), R.string.reg_already, Toast.LENGTH_SHORT).show();

                                }//End if()
                                //If there is another reason
                                else
                                    {
                                        //Appropriate error message is displayed
                                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }//End else()
                        }//End else()
                    }//End onComplete()
                });//End addOnCompleteListener
    }//End registerUser()

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //If user taps sign up button, Register function will be ran
            case R.id.buttonSignup:
                registerUser();
                break;

            /*If login text is selected the user will get redirected to the Login screen
             using an Intent statement*/
            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, LoginUser.class));
                break;
        }//End switch()
    }//End onClick()
}//End SignupUser()
