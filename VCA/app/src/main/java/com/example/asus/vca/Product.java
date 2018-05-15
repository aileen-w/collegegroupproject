package com.example.asus.vca;

public class Product {

    private String item;
    private String price;

    //constructor for product class
    public Product(String item, String price) {

        this.item = item;
        this.price = price;
    }

    //setter for product item
    public void setItem(String item) {
        this.item = item;
    }

    //getter for product item
    public String getItem() {
        return item;
    }

    //setter for producct price
    public void setPrice(String price) {
        this.price = price;
    }

    //getter for product price
    public String getPrice() {
        return price;
    }

    //returns product item and its price as string
    public String toString() {
        return this.item + " : " + this.price;
    }

}




