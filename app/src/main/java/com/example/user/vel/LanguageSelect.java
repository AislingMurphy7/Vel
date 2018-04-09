package com.example.user.vel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/*
This class is the first screen of the VéL Android app
This class displays a ListView containing languages which
the user can select from to change the following screens to
the language they have chosen
*/

public class LanguageSelect extends AppCompatActivity
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
        //Sets the layout according to the XML file
        setContentView(R.layout.activity_language_select);

        //XML variable
        ListView listView = findViewById(R.id.Lang);

        //CustomeAdapter is defined to allow for the listview to be more flexable to design change
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
                    Intent intent = new Intent(LanguageSelect.this, Agreements.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the second row
                else if(i == 1)
                {
                    //The Locale will be set to "French"
                    setLocale("fr");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, Agreements.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the third row
                else if(i == 2)
                {
                    //The Locale will be set to "Spanish"
                    setLocale("es");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, Agreements.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the fourth row
                else if(i == 3)
                {
                    //The Locale will be set to "German"
                    setLocale("de");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, Agreements.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the fifth row
                else if(i == 4)
                {
                    //The Locale will be set to "Swedish"
                    setLocale("sv");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, Agreements.class);
                    startActivity(intent);
                }//End if()
                //If option(i) is equal to the sixth row
                else if(i == 5)
                {
                    //The Locale will be set to "Russian"
                    setLocale("ru");
                    //The app will change from current screen to next screen
                    Intent intent = new Intent(LanguageSelect.this, Agreements.class);
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

    //Function creates the dropdown toolbar menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);

    }//End onCreateOptionMenu()

    //If one of the options from the dropdown menu is selected the following will occur
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Variable to hold id of selected menu option
        int option_id = item.getItemId();
        //If the settings option is selected, user will be informed they are already on the language screen
        if (option_id == R.id.action_Language)
        {
            Toast.makeText(LanguageSelect.this, "You are already in the language selection screen", Toast.LENGTH_LONG).show();
        }//End if()

        //If the help option is selected, user will be re-directed to help screen
        if (option_id == R.id.action_help)
        {
            Intent help_intent = new Intent(LanguageSelect.this, UserHelp.class);
            startActivity(help_intent);
        }//End if()

        //If the exit option is selected, the app will close
        if (option_id == R.id.action_exit)
        {
            Intent exit_intent = new Intent(Intent.ACTION_MAIN);
            exit_intent.addCategory(Intent.CATEGORY_HOME);
            exit_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exit_intent);
            finish();
            System.exit(0);
        }//End if()

        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected()
}//End MainActivity()

