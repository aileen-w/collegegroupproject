package com.example.asus.vca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class TakeawayOrder extends AppCompatActivity {


    private int total;
    private int mainPrice;
    private int subPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeaway_order);
        String symbol = "€";
        //itemchosen here will have been passed from the TakeAwayActivity -> SuborderActivity -> SuborderFragment -> HERE!
        String mainitemchosen = (String)getIntent().getSerializableExtra("mainitemchosen");
        String mainitemprice = (String)getIntent().getSerializableExtra("mainitemprice");
        String subItem  = (String) getIntent().getSerializableExtra("subItem");
        String subPrice = (String) getIntent().getSerializableExtra("subPrice");

        TextView item1 = (TextView) findViewById(R.id.mainItem);
        item1.setText(mainitemchosen);

        TextView itemprice = (TextView) findViewById(R.id.itemprice);
        itemprice.setText(symbol.concat(mainitemprice));

        TextView item2 = (TextView) findViewById(R.id.subItem);
        item2.setText(subItem);

        TextView item3 = (TextView) findViewById(R.id.subPrice);
        item3.setText(symbol.concat(subPrice));


        double mainPrice = Double.valueOf(mainitemprice);
        double itemPrice = Double.valueOf(subPrice);

        double total = mainPrice+itemPrice;
        String totalCombined = String.valueOf(total);

        TextView etTotal = (TextView) findViewById(R.id.etTotal);
        etTotal.setText(symbol.concat(totalCombined));
    }

}
