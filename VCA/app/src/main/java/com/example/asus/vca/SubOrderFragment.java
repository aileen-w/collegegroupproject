package com.example.asus.vca;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


/**
 * Written by Jennifer Flynn
 * A simple {@link Fragment} subclass.
 */
public class SubOrderFragment extends ListFragment {

    String[] subOrderItems;
    String[] itemPrices;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_order,container,false);  //inflates fragment sub order
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Create submenu instances item1 and item 2 which stores
        // item noodles and rice with their price values
        SubMenu item1 = new SubMenu("Noodles", "1.50");
        SubMenu item2 = new SubMenu("Rice", "0.50");
        //creates subMenuList of type SubMenu and addss item1
        //and item2 to the subMenuList array
        List<SubMenu> subMenuList = Arrays.asList(item1, item2);

        //creates adapter for subMenuList and uses a simple list item to display it
        ArrayAdapter<SubMenu> adapter = new ArrayAdapter<SubMenu>(getActivity(),android.R.layout.simple_list_item_1, subMenuList);
        //sets adapter for subMenuList
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(getActivity(),"Item "+position, Toast.LENGTH_SHORT).show();

        //SubOrderActivity is the activity which "owns" this fragment. We need to access it because it is holding
        //a String which contains the main item ordered in the TakeawayActivity and it's price.
        SubOrderActivity parentActivity  = (SubOrderActivity)getActivity();
        String orderedItem = parentActivity.getOrderedItemChosenFromTakeawayActivity();
        String orderedItemPrice = parentActivity.getOrderedItemChosenPriceFromTakeawayActivity();


        //creates intent to open TakeawayOrder Class
        Intent launchTakawayOrderActivity = new Intent(getActivity(), TakeawayOrder.class);
        //We need to use the extras of this intent to pass the selected item (i.e. Chicken, Prawn or Pork) to the SubOrderActivity
        launchTakawayOrderActivity.putExtra("mainitemchosen", orderedItem);
        launchTakawayOrderActivity.putExtra("mainitemprice", orderedItemPrice);
        //create instance of SubMenu and gets the item at its position
        //in the list array
        SubMenu test = (SubMenu)getListView().getItemAtPosition(position);
        //sets item as a string value
        String subItem = test.getItem();
       //sets item price as a string value
        String subPrice = test.getPrice();
        //puts extras of this intent to pass the selected item (i.e. Noodles, Rice) to the SubOrderActivity
        launchTakawayOrderActivity.putExtra("subItem", subItem);
        launchTakawayOrderActivity.putExtra("subPrice", subPrice);

        //run sub order activity intent
        startActivity(launchTakawayOrderActivity);
    }

}
