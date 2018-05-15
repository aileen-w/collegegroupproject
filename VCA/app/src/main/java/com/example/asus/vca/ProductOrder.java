package com.example.asus.vca;

import java.io.Serializable;

public class ProductOrder implements Serializable {

    private String item;
    private String price;

    //constructor for productorder class
    public ProductOrder(String item, String price) {

        this.item = item;
        this.price = price;
    }

    //setter for product order item
    public void setItem(String item) {
        this.item = item;
    }

    //getter for product order item
    public String getItem() {
        return item;
    }

    //setter for the product order item price
    public void setPrice(String price) {
        this.price = price;
    }

    //getter for the product order item price
    public String getPrice() {
        return price;
    }

    //return productor order item and its price as string
    public String toString() {
        return this.item + " - " + this.price;
    }
}
