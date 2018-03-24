package com.example.user.vel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
            Toast.makeText(Settings.this, "You are already in 'Settings'", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_Language)
        {
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_help)
        {
            Intent intent = new Intent(Settings.this, UserHelp.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

