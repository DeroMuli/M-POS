package com.muli.m_pos.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.muli.m_pos.R;
import com.muli.m_pos.ui.main.MainActivity;

import java.util.List;

public class BuyingItemAdapter extends RecyclerView.Adapter<BuyingItemAdapter.ViewHolder> {

    private List<ItemCardData> buyingCardDataList;

    public BuyingItemAdapter(List<ItemCardData> buyingCardDataList){
        this.buyingCardDataList = buyingCardDataList;
    }

    //Class for handling the actions of individual cardviews ... set it's factory using this class's setter method
    public static abstract class CardViewHandler{

        protected CardView cardView;

        public void setcardview(CardView cardView){ this.cardView = cardView; }

        public abstract void plusbutton();

        public abstract void downbutton();

    }

    //factory used during binding
    public static interface CardViewHandlerFactory{
        public CardViewHandler getCardViewHandler();
    }

    private CardViewHandlerFactory cardViewHandlerFactory;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.bought_item_card,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.view;
        this.setupwidth(cardView);
        ImageView imageView = (ImageView) cardView.findViewById(R.id.itempic);
        TextView textView =(TextView) cardView.findViewById(R.id.itemname);
        TextView numofitems = (TextView)cardView.findViewById(R.id.itemcounter);

        int itemnumber = buyingCardDataList.get(position).getItemnumber();
        numofitems.setText(String.valueOf(itemnumber));
        textView.setText(buyingCardDataList.get(position).getName());
        Uri imageuri = Uri.parse(buyingCardDataList.get(position).getImageUri());
        imageView.setImageURI(imageuri);
        if(cardViewHandlerFactory != null)
            this.setupcardviewhandler(cardView,itemnumber);

    }

    @Override
    public int getItemCount() {
        return buyingCardDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView view ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = (CardView)itemView;
        }
    }

    public void setCardViewHandlerFactory(CardViewHandlerFactory cardViewHandlerFactory) {
        this.cardViewHandlerFactory = cardViewHandlerFactory;
    }

    private void setupwidth(CardView cardView){
        DisplayMetrics displayMetrics = MainActivity.ScreenDisplay;
        int screenwidth = displayMetrics.widthPixels;
        ImageView itempic = cardView.findViewById(R.id.itempic);
        int newdimen = screenwidth - 400;
        itempic.getLayoutParams().width = newdimen;
        cardView.getLayoutParams().height = newdimen + 10;

    }

    private void setupcardviewhandler(CardView cardView, int itemnumber){

        CardViewHandler cardViewHandler = cardViewHandlerFactory.getCardViewHandler();
        ImageButton plusbutton = (ImageButton)cardView.findViewById(R.id.additionbutton);
        ImageButton minusbutton = (ImageButton)cardView.findViewById(R.id.minusbutton);
        cardViewHandler.setcardview(cardView);

        Drawable minusdrawable;

        if(itemnumber == 1)
            minusdrawable = ContextCompat.getDrawable(cardView.getContext(), android.R.drawable.ic_menu_delete);
        else
            minusdrawable = ContextCompat.getDrawable(cardView.getContext(), android.R.drawable.btn_minus);

        minusbutton.setImageDrawable(minusdrawable);

        minusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewHandler.downbutton();
            }
        });
        plusbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewHandler.plusbutton();
            }
        });

    }

}
