package com.example.asus.vca;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SubOrderActivity extends AppCompatActivity {

    private String itemChosenFromTakeawayActivity;
    private String itemChosenPriceFromTakeawayActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {               //loads suborder activity
        super.onCreate(savedInstanceState);
        //string itemchosenfromtakeawayactivity is itemchosen
        this.itemChosenFromTakeawayActivity = (String) getIntent().getSerializableExtra("itemchosen");
        //string itemchosenpricefromtakeawayactivity is itemprice
        this.itemChosenPriceFromTakeawayActivity = (String) getIntent().getSerializableExtra("itemprice");
        setContentView(R.layout.activity_sub_order);
    }

    //returns string itemchosenfromtakeawayactivity
    public String getOrderedItemChosenFromTakeawayActivity() {
        return this.itemChosenFromTakeawayActivity;
    }

    //returns string itemchosenpricefromtakeawayactivity
    public String getOrderedItemChosenPriceFromTakeawayActivity() {
        return this.itemChosenPriceFromTakeawayActivity;
    }
}
