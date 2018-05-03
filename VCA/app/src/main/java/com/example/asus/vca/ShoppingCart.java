package com.example.asus.vca;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Serializable {

    private ArrayList<ProductOrder> cartItems = new ArrayList<>();

    public ProductOrder getProduct(int position) {
        return cartItems.get(position);
    }

    public void setProducts(ProductOrder item){
        cartItems.add(item);
    }

    public List<ProductOrder> getList() {
        return cartItems;
    }
    public int getCartsize(){
        return cartItems.size();
    }

    public boolean CheckProductInCart(ProductOrder item)
    {
        return cartItems.contains(item);
    }



}

