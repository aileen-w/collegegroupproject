package com.example.asus.vca;

public class Menu {

    private String item;
    private String price;


    public Menu (String item, String price){

        this.item = item;
        this.price = price;
    }

    private void setItem(String item)
    {
        this.item = item;
    }

    private String getItem()
    {
        return item;
    }

    private void setPrice(String price)
    {
        this.price = price;
    }

    private String getPrice()
    {
        return price;
    }

}

