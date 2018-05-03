package com.example.asus.vca;

public class Menu {

    private String item;
    private String price;


    public Menu(String item, String price){

        this.item = item;
        this.price=price;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public String getItem()
    {
        return item;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPrice()
    {
        return price;
    }

    public String toString(){
        return this.item+ " : " +this.price;
    }

}