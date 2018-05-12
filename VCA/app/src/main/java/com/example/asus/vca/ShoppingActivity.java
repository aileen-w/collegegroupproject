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

/*Written by Jennifer Flynn
 */

public class ShoppingActivity extends AppCompatActivity {
    String totalPrice = "0";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {                           //loads shopping activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);          //identifies linear layout and is constant
        final Button buttonBasket = (Button)findViewById(R.id.basket);                    //identities button
        final Controller ct = new Controller();                                          //instantiates controller and is constant
        final TextView total = (TextView) findViewById(R.id.total);                     //identifies total textview and is constant
        int spinNumbers[] = {1,2,3,4,5};                                                //identifies quantity of each product
        ProductOrder products = null;                                                   //initializes product order
        List<Integer> spinnerArray = new ArrayList<>();                                 //creates list array for spinner numbers
        for (int s : spinNumbers) {
            spinnerArray.add(s);       //addes spinner numbers to list array
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(                               //creaters array adapter for list
                this,
                android.R.layout.simple_spinner_item,                                     //adds spinner array list to spinner item with adapter
                spinnerArray
        );

        String name[] = getResources().getStringArray(R.array.shoppingItems);          //adds shopping items to string array name
        String price[] = getResources().getStringArray(R.array.shoppingPrices);       //adds shopping prices to string array price

        for(int i=0; i<=2;i++){

            products = new ProductOrder(name[i], price[i]);     //adds product name and price to instance of product order
            ct.setProducts(products);                            //adds product object instance to myproducts array in a controller method
        }
        //gets the array list size
        int productsize = ct.getProductArraylistsize();
        //dynamically creates texview parameters for the food items i.e bread, milk or eggs, a spinner for number of items and a buttom adding the item or items ordered
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int j=0;j< productsize;j++){

            //product item is used for each food item in each product order instance,
            // from the myproducts array
            ProductOrder item = ct.getProducts(j);
            //creates linear layout which will contain textviews paramters from above
            LinearLayout la = new LinearLayout(this);
            //sets linear layout orientation
            la.setOrientation(LinearLayout.HORIZONTAL);
            //creates textview tv
            TextView tv = new TextView(this);
            //sets text of textview tv to item name
            tv.setText(" "+item.getItem()+" ");
            //adds textview to linear layout
            la.addView(tv);
            //creates textview tv1
            TextView tv1 = new TextView(this);
            //sets text of textview tv1 to item price
            tv1.setText("€"+item.getPrice()+" ");
            //adds textview to linear layout
            la.addView(tv1);
            //creates spinner and is constant
            final Spinner spinner = new Spinner(this);
            //sets spinner adapter
            spinner.setAdapter(adapter);
            //adds spinner to linear layout
            la.addView(spinner);

            //creates add to cart button
            final Button btn1 = new Button(this);

            //sets cart button id
            btn1.setId(j+1);
            btn1.setText("Add to Cart");

            //sets  the add to cart button layout and its text
            btn1.setLayoutParams(params);
            final int index = j;
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {                    //sets listener on the add to cart buttons
                    // TODO Auto-generated method stub
                    Log.i("TAG", "spinner value"+ spinner.getSelectedItem());              //gets selected item value
                    Log.i("TAG", "button text " + btn1.getText().toString());              //sets text on add to ca
                    ProductOrder productsObject = ct.getProducts(index);                            //gets shopping item basert button
                    Log.i("TAG", "index:"+index);                                              //gets index valued dynamically from j

                    //if no more products are in the shopping cart, do the following
                    if(!ct.getCart().CheckProductInCart(productsObject)){
                        btn1.setText("Item Added");                   //sets add to cart button text
                        int counter = Integer.valueOf(spinner.getSelectedItem().toString());     //gets items position and sets it as counter value

                        for (int s=1; s<=counter; s++) {
                            ct.getCart().setProducts(productsObject);   //adds shopping item to shopping cart
                        }

                        //shows toast when item is added to cart
                        Toast.makeText(getApplicationContext(), "New CartSize:" +ct.getCart().getCartsize(),Toast.LENGTH_LONG).show();
                        //gets items price
                        double price = Double.valueOf(productsObject.getPrice());
                        //gets quantity value of items
                        int multipler = Integer.valueOf(spinner.getSelectedItem().toString());
                        //multiplies value and cost price
                        double cal1 = price*multipler;
                        //stores total cost value
                        double all = Double.valueOf(totalPrice) + cal1;
                        //puts total cost value into string
                        totalPrice = String.valueOf(all);
                        //sets total cost value into textview
                        total.setText("Total : €" + String.valueOf(all));
                    }else{
                        //reminds customer item has already been added to basket
                        Toast.makeText(getApplicationContext(), "Products"+(index+1)+"Already Added",Toast.LENGTH_LONG ).show();
                    }
                }
            });
            //adds dynamic buttons to linear layout
            la.addView(btn1);
            //adds linear layout to view
            layout.addView(la);
        }
        //sets listener on basket button
        buttonBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //creates instance of shopping cart
                ShoppingCart cart = ct.getCart();
                //adds array list of products ordered to cart
                List<ProductOrder> productOrderList = cart.getList();
                for (int j=0;j< productOrderList.size();j++){

                }
                //creates basket class intent
                Intent in = new Intent(getBaseContext(),Basket.class);
                //adds shoppingcart extra cart
                in.putExtra("ShoppingCart", cart);
                //starts basket intent
                startActivity(in);
            }
        });
    }
}
