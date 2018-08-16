package com.example.user.vel;

/*
    This class is used as a step of security for the
    vehicles data stored within the app. Each vehicles
    data is password protected with the password belonging
    to the user
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PasswordProtected extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_password_protected);
    }//End onCreate()
}//End PasswordProtected()
