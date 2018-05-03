package com.example.asus.vca;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    String totalPrice = "0";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);
        final Button btn = (Button)findViewById(R.id.second);
        final Controller ct = new Controller();
        final TextView total = (TextView) findViewById(R.id.total);
        int spinNumbers[] = {1,2,3,4,5};
        ProductOrder products = null;
        List<Integer> spinnerArray = new ArrayList<>();
        for (int s : spinNumbers) {
            spinnerArray.add(s);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerArray
        );

        String name[] = getResources().getStringArray(R.array.shoppingItems);
        String price[] = getResources().getStringArray(R.array.shoppingPrices);

        for(int i=0; i<=2;i++){

            products = new ProductOrder(name[i], price[i]);
            ct.setProducts(products);
        }
        //gets the array list of the food items
        int productsize = ct.getProductArraylistsize();
        //dynamically lays out the food item i.e bread, milk or eggs, a spinner for number of items and a buttom adding the item or items
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int j=0;j< productsize;j++){

            //menu item is used for each food item in the products array, i.e. bread, milk, eggs
            ProductOrder item = ct.getProducts(j);
            LinearLayout la = new LinearLayout(this);
            la.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText(" "+item.getItem()+" ");
            la.addView(tv);
            TextView tv1 = new TextView(this);
            tv1.setText("€"+item.getPrice()+" ");
            la.addView(tv1);
            final Spinner spinner = new Spinner(this);
            spinner.setAdapter(adapter);
            la.addView(spinner);
            final Button btn1 = new Button(this);
            btn1.setId(j+1);
            btn1.setText("Add to Cart");
            btn1.setLayoutParams(params);
            final int index = j;
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("TAG", "spinner value"+ spinner.getSelectedItem());
                    Log.i("TAG", "button text " + btn1.getText().toString());
                    Log.i("TAG", "index:"+index);
                    ProductOrder productsObject = ct.getProducts(index);

                    //if no more products are in the shopping cart, do the following
                    if(!ct.getCart().CheckProductInCart(productsObject)){
                        btn1.setText("Item Added");
                        int counter = Integer.valueOf(spinner.getSelectedItem().toString());
                        for (int s=1; s<=counter; s++) {
                            ct.getCart().setProducts(productsObject);
                        }
                        Toast.makeText(getApplicationContext(), "New CartSize:" +ct.getCart().getCartsize(),Toast.LENGTH_LONG).show();
                        double price = Double.valueOf(productsObject.getPrice());
                        int multipler = Integer.valueOf(spinner.getSelectedItem().toString());
                        double cal1 = price*multipler;
                        double all = Double.valueOf(totalPrice) + cal1;
                        totalPrice = String.valueOf(all);
                        total.setText("Total : €" + String.valueOf(all));
                    }else{
                        Toast.makeText(getApplicationContext(), "Products"+(index+1)+"Already Added",Toast.LENGTH_LONG ).show();
                    }
                }
            });
            la.addView(btn1);
            layout.addView(la);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShoppingCart cart = ct.getCart();
                List<ProductOrder> productOrderList = cart.getList();
                for (int j=0;j< productOrderList.size();j++){

                }
                Intent in = new Intent(getBaseContext(),Basket.class);
                in.putExtra("ShoppingCart", cart);
                startActivity(in);
            }
        });
    }
}
