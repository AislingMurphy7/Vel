package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/*
    This class is the main 'welcome page' of the VéL application,
    Here the user can choose from a selection of options
    such as 'Language Selection', 'Account Login' &
    'Create Account'
 */

public class Welcome_Page extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Removes actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_welcome__page);

        //XML button variables
        Button language = findViewById(R.id.langbtn);
        Button createAcc = findViewById(R.id.CreateAccbtn);
        Button loginAcc =  findViewById(R.id.Loginbtn);

        //If the user selects the 'Language Selection' button
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'Language Selection' screen
                Intent lang_intent = new Intent(Welcome_Page.this, LanguageSelect.class);
                startActivity(lang_intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user selects the 'Create Account' button
        createAcc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'Create Account' screen
                Intent create_intent = new Intent(Welcome_Page.this, SignupUser.class);
                startActivity(create_intent);
            }//End onClick()
        });//End setOnClickListener()

        //If the user selects the 'Account Login' button
        loginAcc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The user is redirected to the 'Account Login' screen
                Intent login_intent = new Intent(Welcome_Page.this, LoginUser.class);
                startActivity(login_intent);
            }//End onClick()
        });//End setOnClickListener()
    }//End onCreate()
}//End Welcome_Page()
