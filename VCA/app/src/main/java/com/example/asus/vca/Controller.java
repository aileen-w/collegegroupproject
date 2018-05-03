package com.example.asus.vca;

import android.app.Activity;


import java.util.ArrayList;

public class Controller extends Activity {

    public Controller(){}
    private ArrayList<ProductOrder> myproducts = new ArrayList<>();
    private ShoppingCart myCart = new ShoppingCart();
    public ProductOrder getProducts(int position) { return myproducts.get(position);}
    public void setProducts(ProductOrder products){
        myproducts.add(products);
    }

    public ShoppingCart getCart(){
        return myCart;

    }

    public int getProductArraylistsize(){
        return myproducts.size();
    }

}
