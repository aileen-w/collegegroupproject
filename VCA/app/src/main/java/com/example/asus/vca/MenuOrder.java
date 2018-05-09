package com.example.asus.vca;

import java.io.Serializable;

public class MenuOrder implements Serializable {

    private String item;
    private String price;

    //menu order constructor class for takeaway class
    public MenuOrder (String item, String price){

        this.item = item;
        this.price = price;
    }

    //setter of menu item
    public void setItem(String item)
    {
        this.item = item;
    }

    //getter of menu item
    public String getItem()
    {
        return item;
    }

    //setter of menu item price
    public void setPrice(String price)
    {
        this.price = price;
    }

    //getter of menu item price
    public String getPrice()
    {
        return price;
    }


    //returns menu item and its price as string
    public String toString(){
        return this.item+ " - " +this.price;
    }

}