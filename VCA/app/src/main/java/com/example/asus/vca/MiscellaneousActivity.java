package com.example.asus.vca;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.asus.vca.MainActivity.RESULT_SPEECH;

public class MiscellaneousActivity extends AppCompatActivity {

    public Button Game;
//    private ImageButton btnSpeak;
    private TextView txtText;
    public Button Calendar;
    protected static final int RESULT_SPEECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous);
        setupUI();

//        txtText = (TextView) findViewById(R.id.txtText);

//        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

//        btnSpeak.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(
//                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
//
//                try {
//                    startActivityForResult(intent, RESULT_SPEECH);
////                    txtText.setText("");
//                } catch (ActivityNotFoundException a) {
//                    Toast t = Toast.makeText(getApplicationContext(),
//                            "Opps! Your device doesn't support Speech to Text",
//                            Toast.LENGTH_SHORT);
//                    t.show();
//                }
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

//                    txtText.setText(text.get(0));
                    Log.e("txt", text.get(0));
                }
                break;
            }

        }
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

        //find id of calendar button
        Calendar = findViewById(R.id.buttonCalendar);
        {
            //set listener on services button
            Calendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create services activity intent in context to main activity
                    Intent intentLoadCalendarActivity = new Intent(MiscellaneousActivity.this, CalendarActivity.class);
                    //run services activity intent
                    startActivity(intentLoadCalendarActivity);
                }
            });

        }
    }
}

