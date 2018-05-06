package com.example.asus.vca;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Basket extends AppCompatActivity {

    String items = "";
    String model = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;

    List<ProductOrder> productOrderList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);
        ShoppingCart cart = new ShoppingCart();

        final Button buttonPay = (Button)findViewById(R.id.pay);

        cart = (ShoppingCart) getIntent().getSerializableExtra("ShoppingCart");

        productOrderList = cart.getList();

        for (ProductOrder s : productOrderList) {
            Log.d("Basket", s.toString());
            Log.d("Size of list", String.valueOf(productOrderList.size()));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int j=0;j< productOrderList.size();j++){

            ProductOrder item = productOrderList.get(j);

            LinearLayout la = new LinearLayout(this);
            la.setOrientation(LinearLayout.HORIZONTAL);

            TextView tv = new TextView(this);
            tv.setText(" "+item.getItem()+" ");
            tv.setAllCaps(true);
            la.addView(tv);

            addStuff(item.getItem());
            TextView tv1 = new TextView(this);
            tv1.setText("€"+item.getPrice()+" ");
            tv.setAllCaps(true);
            la.addView(tv1);
            addStuff(item.getPrice());
            layout.addView(la);
        }

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    JSONObject obj = new JSONObject();
                    obj.put("svc", "notification");
                    obj.put("dev", model);
                    obj.put("msg", "Items ordered: (items: " + items);
                    new PostData().execute(obj.toString());

                }

                catch(JSONException e){
                    e.printStackTrace();
                    e.getMessage();
                }

            }
        });
    }
    public void addStuff(String s) {
        items.concat(s);
        items.concat(" : ");
    }



}
