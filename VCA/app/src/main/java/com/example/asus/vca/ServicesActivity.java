package com.example.asus.vca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ServicesActivity extends AppCompatActivity {


    public Button takeAway;
    public Button taxi;
    public Button shopping;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        //find id of takeAway button
        takeAway = findViewById(R.id.buttonTakeaway);
        {
            //set listener on takeAway button
            takeAway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create takeAway activity intent in context to services activity
                    Intent intentLoadTakeAwayActivity = new Intent(ServicesActivity.this, TakeAwayActivity.class);
                    //run takeAway activity intent
                    startActivity(intentLoadTakeAwayActivity);
                }
            });

        }

        //find id of taxi button
        taxi = findViewById(R.id.buttonTaxi);
        {
            //set listener on takeAway button
            taxi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create taxi activity intent in context to services activity
                    Intent intentLoadTaxiActivity = new Intent(ServicesActivity.this, TaxiActivity.class);
                    //run taxi activity intent
                    startActivity(intentLoadTaxiActivity);
                }
            });

        }

        //find id of taxi button
        shopping = findViewById(R.id.buttonShopping);
        {
            //set listener on takeAway button
            shopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //create taxi activity intent in context to services activity
                    Intent intentLoadTaxiActivity = new Intent(ServicesActivity.this, ShoppingActivity.class);
                    //run taxi activity intent
                    startActivity(intentLoadTaxiActivity);
                }
            });

        }
    }


}
