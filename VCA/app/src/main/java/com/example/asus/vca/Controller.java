package com.example.asus.vca;

import android.app.Activity;


import java.util.ArrayList;

public class Controller extends Activity {

    //controls the flow of data between shopping cart and products ordered
    public Controller(){

    }

    //creates product order arraylist myproducts
    private ArrayList<ProductOrder> myproducts = new ArrayList<>();

    //creates shopping cart mycart
    private ShoppingCart myCart = new ShoppingCart();

    //getter of products arraylist myproduct
    public ProductOrder getProducts(int position) { return myproducts.get(position);}

    //setter of myproducts arraylist
    public void setProducts(ProductOrder products){
        myproducts.add(products);
    }

    //getter of shopping cart myCart
    public ShoppingCart getCart(){
        return myCart;

    }

    //returns size of myproducts arraylist
    public int getProductArraylistsize(){
        return myproducts.size();
    }

}
