package com.example.asus.vca;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Basket extends AppCompatActivity {

    String items = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);
        ShoppingCart cart = new ShoppingCart();
        final Button btn = (Button)findViewById(R.id.second);
        cart = (ShoppingCart) getIntent().getSerializableExtra("ShoppingCart");
        List<ProductOrder> productOrderList = cart.getList();
        for (ProductOrder s : productOrderList) {
            Log.d("Basket", s.toString());
            Log.d("Size of list", String.valueOf(productOrderList.size()));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
    }
    public void addStuff(String s) {
        items.concat(s);
        items.concat(" : ");
    }

}
