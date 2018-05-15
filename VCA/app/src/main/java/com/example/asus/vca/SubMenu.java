package com.example.asus.vca;

public class SubMenu {

    private String item;
    private String price;

    //constructor of submeu class
    public SubMenu(String item, String price) {

        this.item = item;
        this.price = price;
    }

    ///setter of submenu item, i.e. noodles or rice
    public void setItem(String item) {
        this.item = item;
    }

    //getter of submenu item
    public String getItem() {
        return item;
    }

    //setter of submenu item price
    public void setPrice(String price) {
        this.price = price;
    }

    //getter of submenu item price
    public String getPrice() {
        return price;
    }

    //return submenu item and its price as string
    public String toString() {
        return this.item + " : " + this.price;
    }

}