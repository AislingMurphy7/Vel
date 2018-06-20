package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/*
This class is the first screen of the VéL Android app
This class is simply a splash screen to display the logo
for added visual effect
*/

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_main);

        //XML variable
        ImageView imageView = findViewById(R.id.splash);

        //This calls on a anim file called fade. this creates the effect you see on the screen
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation);

        //This states for how long the image stays on screen for
        Thread timer = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    //Image will stay on screen for 4seconds
                    sleep(4000);
                    super.run();
                }//End try
                //If there is an error
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }//End catch

                //Then the application will progress to the next screen
                Intent intent = new Intent(getApplicationContext(), LoginUser.class);
                startActivity(intent);
                finish();
            }//End run()
        };// Thread()

        //Timer starts
        timer.start();
    }//End onCreate()
}//End MainActivity()
