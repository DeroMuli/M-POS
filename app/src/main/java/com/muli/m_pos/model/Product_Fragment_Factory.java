package com.muli.m_pos.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muli.m_pos.R;
import com.muli.m_pos.ui.login.LoadingActivity;
import com.muli.m_pos.ui.main.BuyingActivity;

import java.util.ArrayList;
import java.util.List;

public final class Product_Fragment_Factory {

    public static Products_Fragment GetProductFragment(String type){
            if(LoadingActivity.Products.isEmpty())
                return new Products_Fragment(new ArrayList<ItemCardData>());
            else {
                ArrayList<ItemCardData> arrayList = (ArrayList<ItemCardData>) LoadingActivity.Products.get(type);
                return new Products_Fragment(arrayList);
            }
    }

    public static class Products_Fragment extends Fragment {

        private ProductsAdapter productsAdapter;

        public Products_Fragment(List<ItemCardData> itemCardDataList){
            this.productsAdapter = new ProductsAdapter(itemCardDataList);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            RecyclerView root = (RecyclerView) inflater.inflate(R.layout.products_frag,container,false);
            productsAdapter.setListerner(new ProductsAdapter.Card_Listerner() {
                @Override
                public void productcardclicked(ItemCardData productdata) {
                    Intent intent = new Intent(getActivity(), BuyingActivity.class);
                    intent.putExtra(BuyingActivity.PASSED_CARD,productdata);
                    startActivity(intent);
                }
            });
            root.setAdapter(productsAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
            root.setLayoutManager(layoutManager);
            return root;
        }
    }

}
