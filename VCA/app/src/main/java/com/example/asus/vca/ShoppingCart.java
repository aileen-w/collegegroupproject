package com.example.asus.vca;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*Written by Jennifer Flynn
 */

public class ShoppingCart implements Serializable {

    //creates product order array list cartitems
    private ArrayList<ProductOrder> cartItems = new ArrayList<>();

    //returns a cart item from cartitems arraylist
    public ProductOrder getProduct(int position) {
        return cartItems.get(position);
    }

    //sets cart items in cartitems arraylist
    public void setProducts(ProductOrder item){
        cartItems.add(item);
    }

    //returns cartitems arraylist
    public List<ProductOrder> getList() {
        return cartItems;
    }

    //returns size of cartitems arraylist
    public int getCartsize(){
        return cartItems.size();
    }

    //checks if there are itens in the shopping cart
    public boolean CheckProductInCart(ProductOrder item)
    {
        return cartItems.contains(item);
    }



}

