package com.example.asus.vca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MiscellaneousActivity extends AppCompatActivity {

    public Button Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous);
        setupUI();
    }

    private void setupUI() {
        Game = findViewById(R.id.buttonGame);
        {
            //set listener on miscellaneous button
            Game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create miscellaneous activity intent in context to main activity
                    Intent intentLoadGameActivity = new Intent(MiscellaneousActivity.this, GameActivity.class);
                    //run services activity intent
                    startActivity(intentLoadGameActivity);
                }
            });

        }
    }
}

