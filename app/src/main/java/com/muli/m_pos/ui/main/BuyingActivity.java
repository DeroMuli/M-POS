package com.muli.m_pos.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.ItemCardData;
import com.muli.m_pos.model.BuyingCardStore;
import com.muli.m_pos.model.BuyingItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyingActivity extends AppCompatActivity {

    private Bought_Item_Fragment fragment = new Bought_Item_Fragment();
    private static final BuyingCardStore store = new BuyingCardStore();
    public static String PASSED_CARD = "Buying Card Data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        //Adding listerners to the main fragment button
        fragment.addreacttobutton(new Bought_Item_Fragment.ReactToButton() {
            @Override
            public void fabclicked() {
                fetchitemsfromproducts();
            }

            @Override
            public void buybuttonclicked() {
                buyitems();
            }
        });
        //Setting up the main fragement transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.buying_container,fragment);
        transaction.commit();

        //Setting up the toolbarfragment
        this.setuptoolbar();

        Intent intent = getIntent();
        ItemCardData send = intent.getParcelableExtra(PASSED_CARD);
        if(send != null)
            store.addBuyingCardData(send);
    }

    private void setuptoolbar(){
        ProfileToolBarFactory.ProfileFrag profileFrag = ProfileToolBarFactory.getProfile();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.toolbar_holder,profileFrag);
        transaction.commit();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //use on create view instead
    @Override
    protected void onStart() {
        super.onStart();
        updateview();
    }

    public void fetchitemsfromproducts() {
        Intent intent = new Intent(this,ProductsActivity.class);
        startActivity(intent);
    }

    public void buyitems() {
        store.clear();
        updateview();
        Toast toast = Toast.makeText(this,"You have bought items",Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateview(){
        //Updating the recycler view
        View root = fragment.getView();
        RecyclerView recyclerView = (RecyclerView)root.findViewById(R.id.buying_item_recycler);
        BuyingItemAdapter newadapter = new BuyingItemAdapter(store.getStore());
        newadapter.setCardViewHandlerFactory(new BuyingItemAdapter.CardViewHandlerFactory() {
            @Override
            public BuyingItemAdapter.CardViewHandler getCardViewHandler() {
                return new BuyingActivityCardViewHandler();
            }
        });
        recyclerView.swapAdapter(newadapter,true);
        //Updating the price button
        TextView priceview = (TextView)findViewById(R.id.total_price);
        priceview.setText(String.valueOf(store.gettotalprice()));
    }

    //Inner class for handling vews in the CardView Associated with the Recyclerview
    public class BuyingActivityCardViewHandler extends BuyingItemAdapter.CardViewHandler{

        @Override
        public void plusbutton() {
            if(super.cardView != null) {
                ItemCardData buyingCardData = getbuyingcarddata();
                store.addBuyingCardData(buyingCardData);
                updateview();
            }
        }

        @Override
        public void downbutton() {
            if(super.cardView != null) {
                ItemCardData buyingCardData = getbuyingcarddata();
                store.removeBuyingCardData(buyingCardData);
                updateview();
            }
        }

        private ItemCardData getbuyingcarddata(){
            TextView itemname =(TextView) cardView.findViewById(R.id.itemname);
            String name = itemname.getText().toString();
            return store.getbuyingCardData(name);
        }

    }

}