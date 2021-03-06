package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

/*
This class displays a ListView containing languages which
the user can select from to change the following screens to
the language they have chosen
*/

public class LanguageSelect extends Activity
{
    //IMAGES Array holds all the Images used within the ListView for the Languages
    int[] IMAGES = {R.drawable.english, R.drawable.french, R.drawable.spanish, R.drawable.german};

    //NAMES Array holds the languages the user can select from
    String[] NAMES = {"English", "French", "Spanish", "German"};

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Removes actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Executes the loadLocale() function
        loadLocale();
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_language_select);

        //XML variable
        ListView listView = findViewById(R.id.Lang);

        //CustomAdapter is defined to allow for the listview to be more flexible to design change
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
                    Intent intent = new Intent(LanguageSelect.this, LoginUser.class);
                    startActivity(intent);

                }//End if()

                //If option(i) is equal to the second row
                else if(i == 1)
                {
                    //The Locale will be set to "French"
                    setLocale("fr");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, LoginUser.class);
                    startActivity(intent);

                }//End if()

                //If option(i) is equal to the third row
                else if(i == 2)
                {
                    //The Locale will be set to "Spanish"
                    setLocale("es");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, LoginUser.class);
                    startActivity(intent);

                }//End if()

                //If option(i) is equal to the fourth row
                else if(i == 3)
                {
                    //The Locale will be set to "German"
                    setLocale("de");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, LoginUser.class);
                    startActivity(intent);

                }//End if()
            }//End onItemClick()
        });//End setOnItemClickListener()
    }//End onCreate()

    //Within this function the default locale is changed to the new chosen language
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

    }//End setLocale()

    //Within this function the selected language is set
    public void loadLocale()
    {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);

    }//End loadLocale()

    //This class gets the count of the IMAGES array and its item ids
    class CustomAdpater extends BaseAdapter
    {
        public int getCount()
        {
            return IMAGES.length;

        }//End getCount()

        public Object getItem(int i)
        {
            return null;

        }//End getItem()

        public long getItemId(int i)
        {
            return 0;

        }//End getItemId

        /*Within this function the custom layout is used and the IMAGES array is added to display
        images within the array as one image per each list item*/
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            view = getLayoutInflater().inflate(R.layout.custom_layout_lang,null);

            ImageView imageView= view.findViewById(R.id.imageView);
            TextView textViewLang= view.findViewById(R.id.textViewLang);

            imageView.setImageResource(IMAGES[i]);
            textViewLang.setText(NAMES[i]);

            return view;

        }//End getView()
    }//End CustomAdapter()
}//End MainActivity()

