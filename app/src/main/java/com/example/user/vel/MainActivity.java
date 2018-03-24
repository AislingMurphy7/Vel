/*This class is the Main Activity of the VéL Android app
* This class displays a ListView containing languages which
* the user can select from to change the following screens to
* the language they have chosen*/
package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity
{
    //IMAGES Array holds all the Images used within the ListView for the Languages
    int[] IMAGES = {R.drawable.english, R.drawable.french, R.drawable.spanish, R.drawable.german, R.drawable.swedish, R.drawable.russia};

    //NAMES Array holds the languages the user can select from
    String[] NAMES = {"English", "French", "Spanish", "German", "Swedish", "Russian"};

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Executes the loadLocale() function
        loadLocale();
        setContentView(R.layout.activity_main);

        //
        ImageView vel = (ImageView)findViewById(R.id.vel) ;
        TextView text = (TextView) findViewById(R.id.text);
        ListView listView = (ListView)findViewById(R.id.Lang);

        //
        CustomAdpater customadapter = new CustomAdpater();
        listView.setAdapter(customadapter);

        //When the user clicks on any option on the ListView the following will occur
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //If option(i) is equal to the first row
                if(i == 0)
                {
                    //The Locale will be set to "English"
                    setLocale("en");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(MainActivity.this, Agreements.class);
                    startActivity(intent);
                }
                //If option(i) is equal to the second row
                else if(i == 1)
                {
                    //The Locale will be set to "French"
                    setLocale("fr");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(MainActivity.this, Agreements.class);
                    startActivity(intent);
                }
                //If option(i) is equal to the third row
                else if(i == 2)
                {
                    //The Locale will be set to "Spanish"
                    setLocale("es");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(MainActivity.this, Agreements.class);
                    startActivity(intent);
                }
                //If option(i) is equal to the fourth row
                else if(i == 3)
                {
                    //The Locale will be set to "German"
                    setLocale("de");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(MainActivity.this, Agreements.class);
                    startActivity(intent);
                }
                //If option(i) is equal to the fifth row
                else if(i == 4)
                {
                    //The Locale will be set to "Swedish"
                    setLocale("sv");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(MainActivity.this, Agreements.class);
                    startActivity(intent);
                }
                //If option(i) is equal to the sixth row
                else if(i == 5)
                {
                    //The Locale will be set to "Russian"
                    setLocale("ru");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(MainActivity.this, Agreements.class);
                    startActivity(intent);
                }
            }
        });
    }

    //
    private void setLocale(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    //
    public void loadLocale()
    {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    //
    class CustomAdpater extends BaseAdapter
    {
        public int getCount()
        {
            return IMAGES.length;
        }

        public Object getItem(int i)
        {
            return null;
        }

        public long getItemId(int i)
        {
            return 0;
        }

        //
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            view = getLayoutInflater().inflate(R.layout.custom_layout_lang,null);

            ImageView imageView=(ImageView)view.findViewById(R.id.imageView);
            TextView textViewLang=(TextView)view.findViewById(R.id.textViewLang);

            imageView.setImageResource(IMAGES[i]);
            textViewLang.setText(NAMES[i]);

            return view;
        }
    }
}

