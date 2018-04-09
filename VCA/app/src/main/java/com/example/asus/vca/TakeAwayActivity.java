package com.example.asus.vca;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TakeAwayActivity extends AppCompatActivity {


    public Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_away);


        //find id of first add button
        add = findViewById(R.id.buttonAdd);
        {
            //set listener on speaker button
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create audio activity intent in context to main activity
                    Intent intentLoadTakeAwayFragment = new Intent(TakeAwayActivity.this, TakeAwaySubOrder.class);
                    //run audio activity intent
                    startActivity(intentLoadTakeAwayFragment);
                }
            });

        }

    }


//testing

}
