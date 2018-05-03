package com.example.asus.vca;





import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class SubOrderActivity extends AppCompatActivity {

    private String itemChosenFromTakeawayActivity;
    private String itemChosenPriceFromTakeawayActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.itemChosenFromTakeawayActivity = (String)getIntent().getSerializableExtra("itemchosen");
        this.itemChosenPriceFromTakeawayActivity = (String)getIntent().getSerializableExtra("itemprice");
        setContentView(R.layout.activity_sub_order);
    }

    public String getOrderedItemChosenFromTakeawayActivity(){
        return this.itemChosenFromTakeawayActivity;
    }

    public String getOrderedItemChosenPriceFromTakeawayActivity(){
        return this.itemChosenPriceFromTakeawayActivity;
    }
}
