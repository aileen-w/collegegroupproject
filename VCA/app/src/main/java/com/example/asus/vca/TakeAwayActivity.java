package com.example.asus.vca;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TakeAwayActivity extends AppCompatActivity {


    public Button add;
    public Button add2;
    public Button add3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_away);




        //find id of sweet and sour chicken add button
        add = findViewById(R.id.buttonAdd);
        {
            //set listener on add button
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create sub order activity intent in context to services activity
                    Intent intentLoadTakeAwayFragment = new Intent(TakeAwayActivity.this, SubOrderActivity.class);
                    //We need to use the extras of this intent to pass the selected item (i.e. Chicken, Prawn or Pork) to the SubOrderActivity
                    intentLoadTakeAwayFragment.putExtra("itemchosen", "Sweet and Sour Chicken");
                    intentLoadTakeAwayFragment.putExtra("itemprice", "7.90");

                    //run sub order activity intent
                    startActivity(intentLoadTakeAwayFragment);
                }
            });

        }

        //find id of sweet and sour prawn add button
        add2 = findViewById(R.id.buttonAdd2);
        {
            //set listener on add button
            add2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create sub order activity intent in context to services activity
                    Intent intentLoadTakeAwayFragment = new Intent(TakeAwayActivity.this, SubOrderActivity.class);
                    //We need to use the extras of this intent to pass the selected item (i.e. Chicken, Prawn or Pork) to the SubOrderActivity
                    intentLoadTakeAwayFragment.putExtra("itemchosen", "Sweet and Sour Prawn");
                    intentLoadTakeAwayFragment.putExtra("itemprice", "9.00");

                    //run sub order activity intent
                    startActivity(intentLoadTakeAwayFragment);
                }
            });

        }

        //find id of sweet and sour pork add button
        add3 = findViewById(R.id.buttonAdd3);
        {
            //set listener on add button
            add3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create sub order activity intent in context to services activity
                    Intent intentLoadTakeAwayFragment = new Intent(TakeAwayActivity.this, SubOrderActivity.class);
                    //We need to use the extras of this intent to pass the selected item (i.e. Chicken, Prawn or Pork) to the SubOrderActivity
                    intentLoadTakeAwayFragment.putExtra("itemchosen", "Sweet and Sour Pork");
                    intentLoadTakeAwayFragment.putExtra("itemprice", "7.90");

                    //run sub order activity intent
                    startActivity(intentLoadTakeAwayFragment);
                }
            });

        }

    }

}

