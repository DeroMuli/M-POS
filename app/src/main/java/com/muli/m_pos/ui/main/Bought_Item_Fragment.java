package com.muli.m_pos.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muli.m_pos.R;
import com.muli.m_pos.model.ItemCardData;
import com.muli.m_pos.model.BuyingItemAdapter;

import java.util.ArrayList;

public class Bought_Item_Fragment extends Fragment {


    static interface ReactToButton{
        public void fabclicked();
        public void buybuttonclicked();
    }

    private ReactToButton reactToButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.item_bought_frag,container,false);
        FloatingActionButton floatingActionButton = root.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactToButton.fabclicked();
            }
        });
        Button buybutton = root.findViewById(R.id.buy_items_button);
        buybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactToButton.buybuttonclicked();
            }
        });

        ScrollView scrollView = root.findViewById(R.id.container);
        TextView total_price = (TextView)root.findViewById(R.id.total_price);
        DisplayMetrics displayMetrics = MainActivity.ScreenDisplay;
        int screenheight = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int scrolheight = screenheight - 300;
        scrollView.getLayoutParams().height = scrolheight;

        BuyingItemAdapter itemAdapter = new BuyingItemAdapter(new ArrayList<ItemCardData>());
        RecyclerView recyclerView = root.findViewById(R.id.buying_item_recycler);
        recyclerView.setAdapter(itemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL ,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        return root;
    }


    public void addreacttobutton(ReactToButton reactToButton){
        this.reactToButton = reactToButton;
    }

}
