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

import java.util.Objects;

/*
    This class allows the user to sign into their individual account if they have already
    registered. It contains two edit texts which allows the user to inout their
    email address and password, a sign in button and finally clickable text which redirects
    the user to the sign up / register page.
 */

public class LoginUser extends Activity implements View.OnClickListener
{
    //Declare FireBase object
    FirebaseAuth mAuth;

    //Define variables
    EditText editTextEmail, editTextPassword;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Removes actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_login_user);

        //Instantiate mAuth
        mAuth = FirebaseAuth.getInstance();

        //XML variables
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressbar = findViewById(R.id.progressbar);

        //Attach OnClickListener to TextView
        findViewById(R.id.textViewSignup).setOnClickListener(this);
        //Attach onClickListener to login button
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }//End onCreate()

    //Validate the user information
    private void userLogin()
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

        //Logging user in viz inforaation storred in FireBase
        mAuth.signInWithEmailAndPassword(email, password)
                //Detects the completion
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        //Sets the progressbar to invisible on screen
                        progressbar.setVisibility(View.GONE);
                        //If login in was successful
                        if(task.isSuccessful())
                        {
                            //Clear previous pages to prevent moving back to login screen
                            finish();
                            //Login success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginUser.this, user_options.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }//End if()
                        else
                            {
                                //Appropriate error message is displayed
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }//End else()
                    }//End onComplete()
                });//End addOnCompleteListener
        }//End userLogin()

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            /*If sign up text is selected the user will get redirected to the sign up
             screen using an Intent statement*/
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SignupUser.class));
                break;

            //If user taps login button, userLogin function will be ran
            case  R.id.buttonLogin:
                userLogin();
                break;
        }//End switch()
    }//End onClick()
}//End LoginUser()
