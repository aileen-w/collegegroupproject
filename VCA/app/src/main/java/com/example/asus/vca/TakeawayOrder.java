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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static java.lang.Integer.parseInt;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeaway_order);
        String symbol = "€";
        //itemchosen here will have been passed from the TakeAwayActivity -> SuborderActivity -> SuborderFragment -> HERE!
        mainitemchosen = (String) getIntent().getSerializableExtra("mainitemchosen");
        mainitemprice = (String) getIntent().getSerializableExtra("mainitemprice");
        subItem = (String) getIntent().getSerializableExtra("subItem");
        subPrice = (String) getIntent().getSerializableExtra("subPrice");

        TextView item1 = findViewById(R.id.mainItem);
        item1.setText(mainitemchosen);

        TextView itemprice = (TextView) findViewById(R.id.itemprice);
        itemprice.setText(symbol.concat(mainitemprice));

        TextView item2 = (TextView) findViewById(R.id.subItem);
        item2.setText(subItem);

        TextView item3 = (TextView) findViewById(R.id.subPrice);
        item3.setText(symbol.concat(subPrice));


        double mainPrice = Double.valueOf(mainitemprice);
        double itemPrice = Double.valueOf(subPrice);

        double total = mainPrice + itemPrice;
        DecimalFormat df = new DecimalFormat("#.00");
        totalCombined = df.format(total);

        TextView etTotal = (TextView) findViewById(R.id.etTotal);
        etTotal.setText(symbol.concat(totalCombined));

        setupTakeawayOrder();

    }

    private void setupTakeawayOrder() {

        Order = findViewById(R.id.buttonOrder);
        {
            Order.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {



                    try {

                        name=(EditText)findViewById(R.id.etFullname);
                        address=(EditText)findViewById(R.id.etAddress);
                        phone=(EditText)findViewById(R.id.etPhone);

                        JSONObject obj = new JSONObject();
                        obj.put("svc", "notification");
                        obj.put("dev", model);
                        obj.put("msg", "Takeaway ordered: (mainDish: " + mainitemchosen + ", " +
                                "mainDishPrice: " + mainitemprice+ ", subItemOrdered: " + subItem+
                                "," + "subItemOrderedPrice: " + subPrice + ", " + "totalCostCombined: " + totalCombined
                        +"name: " + name.getText().toString() + "address: " + address.getText().toString() + "phone: " + phone.getText().toString());
                        new PostData().execute(obj.toString());




                        item1.setText(" ");
                        item2.setText(" ");
                        item3.setText(" ");
                        itemprice.setText(" ");
                        etTotal.setText(" ");
                        name.setText(" ");
                        address.setText(" ");
                        phone.setText(" ");



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