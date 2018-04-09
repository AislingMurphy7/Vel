


package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.splash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation);

        Thread timer = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(5000);
                    super.run();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), LanguageSelect.class);
                startActivity(intent);
                finish();
            }
        };

        timer.start();
    }
}
