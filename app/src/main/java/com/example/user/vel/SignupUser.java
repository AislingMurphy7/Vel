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

/*
    This class allows the user to create an account.
    It contains two edit texts which allows the user to inout their
    email address and password, a sign up button and finally clickable text which redirects
    the user to the login page if they already have an account created.
 */

public class SignupUser extends Activity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    ProgressBar progressbar;
    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup_user);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressbar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignup).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    private void registerUser()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError(getText(R.string.email_need));
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError(getText(R.string.ent_valid_email));
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError(getText(R.string.pass_empt));
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError(getText(R.string.mini_length));
            editTextPassword.requestFocus();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if(task.isSuccessful())
                        {
                            finish();
                            //Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), R.string.reg_user, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupUser.this, userProfile.class));

                        }else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(), R.string.reg_already, Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignup:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this, LoginUser.class));
                break;


        }

    }

}
