package com.example.asus.vca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MiscellaneousActivity extends AppCompatActivity {

    public Button Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //loads the miscellaneous activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous);
        setupUI(); //calls the setup user interface
    }

    //identifies the game button
    private void setupUI() {
        Game = findViewById(R.id.buttonGame);
        {
            //set listener on game button
            Game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create game activity intent in context to miscellaneous activity
                    Intent intentLoadGameActivity = new Intent(MiscellaneousActivity.this, GameActivity.class);
                    //run game activity intent
                    startActivity(intentLoadGameActivity);
                }
            });

        }
    }
}

