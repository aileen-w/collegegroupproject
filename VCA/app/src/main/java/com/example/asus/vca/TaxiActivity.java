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
    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText pickupDate;
    private EditText pickupTime;



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

                        name = (EditText) findViewById(R.id.etName);
                        address = (EditText) findViewById(R.id.etAddress);
                        phone = (EditText) findViewById(R.id.etPhone);
                        pickupDate = (EditText) findViewById(R.id.etDate);
                        pickupTime = (EditText) findViewById(R.id.etPickupTime);

                        if(name.getText().toString().isEmpty() && (address.getText().toString().isEmpty()
                                && phone.getText().toString().isEmpty() && pickupDate.getText().toString().isEmpty()
                                && pickupTime.getText().toString().isEmpty())) {

                            name.setError("Input Required");
                            address.setError("Input Required");
                            phone.setError("Input Required");
                            pickupDate.setError("Input Required");
                            pickupTime.setError("Input Required");
                        }

                            JSONObject obj = new JSONObject();
                            obj.put("svc", "notification");
                            obj.put("dev", model);
                            obj.put("msg", "Taxi ordered: (name:" + name.getText().toString() + ", " +
                                    "address:" + address.getText().toString() + ", phone:" + phone.getText().toString() +
                                    "," + "pickupDate:" + pickupDate.getText().toString() + ", " + "pickupTime" + pickupTime.getText().toString());
                            new PostData().execute(obj.toString());

                            name.setText("");
                            address.setText("");
                            phone.setText("");
                            pickupDate.setText("");
                            pickupTime.setText("");

                    }

                            catch(JSONException e){
                            e.printStackTrace();
                            e.getMessage();
                        }

                }
            });

        }

    }

}




