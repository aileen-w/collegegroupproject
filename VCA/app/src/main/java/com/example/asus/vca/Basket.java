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

/*Written by Jennifer Flynn
 */

public class Basket extends AppCompatActivity {

    String items = "";
    String model = Build.MODEL;
    LocationManager locationManager;
    String provider;
    Activity parent = this;

    List<ProductOrder> productOrderList;

    @Override
    public void onCreate(Bundle savedInstanceState) {       //loads basket activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //identifies main linear layout
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);

        //instantiates shopping cart
        ShoppingCart cart = new ShoppingCart();

        //identifies pay button
        final Button buttonPay = (Button)findViewById(R.id.pay);

        //casts cart as shopping cart serializable extra
        cart = (ShoppingCart) getIntent().getSerializableExtra("ShoppingCart");

        //sets productOrderList as carts list of items being ordered
        productOrderList = cart.getList();

        //loops through each item in order and adds to string
        for (ProductOrder s : productOrderList) {
            Log.d("Basket", s.toString());
            Log.d("Size of list", String.valueOf(productOrderList.size()));
        }

        //dynamically creates linear layout parameters that will be in linear layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //loops through list
        for (int j=0;j< productOrderList.size();j++){

            //gets each item in list
            ProductOrder item = productOrderList.get(j);

            //creates linearlayout and its orientation
            LinearLayout la = new LinearLayout(this);
            la.setOrientation(LinearLayout.HORIZONTAL);

            //creates textview, sets the item ordered in the textview, sets the text in caps and adds
            //the textview to the linear layout
            TextView tv = new TextView(this);
            tv.setText(" "+item.getItem()+" ");
            tv.setAllCaps(true);
            la.addView(tv);

            //adds the items to the string of items
            addStuff(item.getItem());

            //creates textview, sets the price in the textview of the item
            //sets the text in caps, adds the textview to the linear layout
            //adds the price to the string of prices and adds the linear layout to the view
            TextView tv1 = new TextView(this);
            tv1.setText("€"+item.getPrice()+" ");
            tv.setAllCaps(true);
            la.addView(tv1);
            addStuff(item.getPrice());
            layout.addView(la);
        }

        //sets listener on pay button which will create json object when clicked
        //json object sends order to database
        //uses a try catch incase there is no values in the json object
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    JSONObject obj = new JSONObject();
                    obj.put("svc", "notification");
                    obj.put("dev", model);
                    for(int i = 0; i < productOrderList.size(); i++) {
                        items += productOrderList.get(i).toString() + ", ";
                    }
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
