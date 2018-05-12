package com.example.asus.vca;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static java.lang.Integer.parseInt;

/*Written by Jennifer Flynn
 */

public class TakeawayOrder extends AppCompatActivity {


    private Button Order;
    private String mainitemchosen;
    private String mainitemprice;
    private String subItem;
    private String subPrice;
    private String symbol = "€";
    private TextView item1;
    private TextView item2;
    private TextView item3;
    private TextView itemprice;
    private TextView etTotal;
    private String totalCombined;
    private double total;
    private double mainPrice;
    private double itemPrice;
    private EditText name;
    private EditText address;
    private EditText phone;

    String model = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {                  //loads takeaway order
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeaway_order);
        String symbol = "€";
        //mainitemchosen here will have been passed from the TakeAwayActivity -> SuborderActivity -> SuborderFragment -> HERE!
        mainitemchosen = (String) getIntent().getSerializableExtra("mainitemchosen");
        //mainitemprice here will have been passed from the TakeAwayActivity -> SuborderActivity -> SuborderFragment -> HERE!
        mainitemprice = (String) getIntent().getSerializableExtra("mainitemprice");
        //subItem here will have been passed from the TakeAwayActivity -> SuborderActivity -> SuborderFragment -> HERE!
        subItem = (String) getIntent().getSerializableExtra("subItem");
        //subPrice here will have been passed from the TakeAwayActivity -> SuborderActivity -> SuborderFragment -> HERE!
        subPrice = (String) getIntent().getSerializableExtra("subPrice");

        //id's textview item1
        TextView item1 = findViewById(R.id.mainItem);
        //sets item1 text as mainitemchosen
        item1.setText(mainitemchosen);

        //id's itemprice
        TextView itemprice = (TextView) findViewById(R.id.itemprice);
        //sets itemprice as mainitemprice and concats euro symbol to it
        itemprice.setText(symbol.concat(mainitemprice));

        //id's item2
        TextView item2 = (TextView) findViewById(R.id.subItem);
        //sets item2 as subItem
        item2.setText(subItem);

        //id's item3
        TextView item3 = (TextView) findViewById(R.id.subPrice);
        //sets item3 as subPrice and concats euro symbol to it
        item3.setText(symbol.concat(subPrice));

        //sets mainitemprice as mainPrice and sets it as a double value
        double mainPrice = Double.valueOf(mainitemprice);
        //sets subPrice as itemPrice and sets it as a double value
        double itemPrice = Double.valueOf(subPrice);

        //adds mainPrice and itemPrice and sets total as a double value
        double total = mainPrice + itemPrice;
        //creates decimal format df
        DecimalFormat df = new DecimalFormat("#.00");
        //sets totals value into decnimal format
        totalCombined = df.format(total);

        //id's etTotal
        TextView etTotal = (TextView) findViewById(R.id.etTotal);
       //sets etTotals's text value from totalCombined and concats euro symbol to it
        etTotal.setText(symbol.concat(totalCombined));

        //runs setupTakeawayOrder Method
        setupTakeawayOrder();

    }

    private void setupTakeawayOrder() {

        //id's Order button
        Order = findViewById(R.id.buttonOrder);
        {
            //sets listener on order button
            Order.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {



                    try {

                        //id's edit texts name, address and phone
                        name=(EditText)findViewById(R.id.etFullname);
                        address=(EditText)findViewById(R.id.etAddress);
                        phone=(EditText)findViewById(R.id.etPhone);

                        //creates JSON object obj and sends a JSON message to the database
                        //with the following value pairs: mainitem chosen, mainitemprice,
                        //subItem, subPrice, totalCostCombined, name, address and phone
                        JSONObject obj = new JSONObject();
                        obj.put("svc", "notification");
                        obj.put("dev", model);
                        obj.put("msg", "Takeaway ordered: (mainDish: " + mainitemchosen + ", " +
                                "mainDishPrice: " + mainitemprice+ ", subItemOrdered: " + subItem+
                                "," + "subItemOrderedPrice: " + subPrice + ", " + "totalCostCombined: " + totalCombined
                        +"name: " + name.getText().toString() + "address: " + address.getText().toString() + "phone: " + phone.getText().toString());
                        new PostData().execute(obj.toString());

                        Toast.makeText(getApplicationContext(), "Takeaway Ordered",
                                Toast.LENGTH_LONG).show();



                    }
                    //throws a catch exception if JSON object is empty
                    catch(JSONException e){
                        e.printStackTrace();
                        e.getMessage();
                    }

                }

            });
        }


    }
}