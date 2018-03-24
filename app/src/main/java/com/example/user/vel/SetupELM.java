package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SetupELM extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_elm);

        ImageView vel = (ImageView) findViewById(R.id.vel);
        TextView text = (TextView) findViewById(R.id.text);
        TextView text1 = (TextView) findViewById(R.id.text1);
        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressbar);
        Button next = (Button) findViewById(R.id.bt1);
        progressbar.setProgress(95);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupELM.this, VehicleSpec.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(SetupELM.this, Settings.class);
            startActivity(intent);
        }

        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(SetupELM.this, MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_help)
        {
            Intent intent = new Intent(SetupELM.this, UserHelp.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
