package com.muli.m_pos.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.muli.m_pos.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<ItemCardData> productList;

    public ProductsAdapter(List<ItemCardData> productList){
        this.productList = productList;
    }

    public static interface Card_Listerner{
        public void productcardclicked(ItemCardData productdata);
    }

    private Card_Listerner listerner;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card,viewGroup,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ItemCardData productdata = productList.get(i);
        CardView cardView = viewHolder.cardView;
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listerner != null)
                    listerner.productcardclicked(productdata);
            }
        });
        ImageView productimage = (ImageView)cardView.findViewById(R.id.productimage);
        TextView productprice = (TextView)cardView.findViewById(R.id.productprice);
        TextView productname = (TextView)cardView.findViewById(R.id.productname);
        Uri imageuri = Uri.parse(productdata.getImageUri());
        productimage.setImageURI(imageuri);
        productname.setText(productdata.getName());
        productprice.setText(String.valueOf(productdata.getprice()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
        }
    }

    public void setListerner(Card_Listerner listerner){this.listerner = listerner;}

}
