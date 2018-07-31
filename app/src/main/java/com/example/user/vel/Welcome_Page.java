package com.example.user.vel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__page);

        Button language = findViewById(R.id.langbtn);
        Button createAcc = findViewById(R.id.CreateAccbtn);
        Button loginAcc =  findViewById(R.id.Loginbtn);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lang_intent = new Intent(Welcome_Page.this, LanguageSelect.class);
                startActivity(lang_intent);
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create_intent = new Intent(Welcome_Page.this, SignupUser.class);
                startActivity(create_intent);
            }
        });

        loginAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(Welcome_Page.this, LoginUser.class);
                startActivity(login_intent);
            }
        });

    }
}
