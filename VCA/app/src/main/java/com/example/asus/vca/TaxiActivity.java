package com.example.asus.vca;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


public class TaxiActivity extends AppCompatActivity {

    String model = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;

    private Button Enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);
        setupTaxiOrder();


    }


    private void setupTaxiOrder(){


        //find id of enter button
        Enter = findViewById(R.id.buttonEnter);
        {


            //set listener on speaker button
            Enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        EditText name = (EditText) findViewById(R.id.etName);
                        EditText address = (EditText) findViewById(R.id.etAddress);
                        EditText phone = (EditText) findViewById(R.id.etPhone);
                        EditText pickupDate = (EditText) findViewById(R.id.etDate);
                        EditText pickupTime = (EditText) findViewById(R.id.etPickupTime);

                        JSONObject obj = new JSONObject();
                        obj.put("svc" , "notification");
                        obj.put("dev" , model);
                        obj.put("msg" , "Taxi ordered: (name:" + name.toString() + ", " +
                                "address:" + address.toString()+", phone:" + phone.toString() +
                                "," + "pickupDate:" + pickupDate.toString()+", " + "pickupTime" + pickupTime.toString());
                        new PostData().execute(obj.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        e.getMessage();
                    }
                }
            });

        }



    }



    }




