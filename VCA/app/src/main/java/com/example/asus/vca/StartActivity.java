package com.example.asus.vca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;




public class StartActivity extends AppCompatActivity {

    Button Speaker;
    Button Home;
    Button Services;
    Button Miscellaneous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Speaker = findViewById(R.id.buttonSpeaker);
        {
            Speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentLoadAudioActivity = new Intent(StartActivity.this, AudioActivity.class);
                    startActivity(intentLoadAudioActivity);
                }
            });

        }

        Home = findViewById(R.id.buttonHome);
        {
            Home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentLoadHomeActivity = new Intent(StartActivity.this, HomeActivity.class);
                    startActivity(intentLoadHomeActivity);
                }
            });

        }

        Services = findViewById(R.id.buttonServices);
        {
            Services.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentLoadServicesActivity = new Intent(StartActivity.this, ServicesActivity.class);
                    startActivity(intentLoadServicesActivity);
                }
            });

        }

        Miscellaneous = findViewById(R.id.buttonMisc);
        {
            Miscellaneous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentLoadMiscActivity = new Intent(StartActivity.this, MiscellaneousActivity.class);
                    startActivity(intentLoadMiscActivity);
                }
            });

        }



    }
}



