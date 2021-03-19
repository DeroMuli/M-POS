package com.muli.m_pos.model;

import java.util.ArrayList;

public class BuyingCardStore {

    private ArrayList<ItemCardData> store;
    private int totalprice;

    public  BuyingCardStore(){
        store = new ArrayList<>();
        totalprice = 0;
    }

    public ArrayList<ItemCardData> getStore() { return store; }

    public void addBuyingCardData(ItemCardData buyingCardData){
        for(ItemCardData storedcarddata : store)
            if(storedcarddata.equals(buyingCardData)) {
                storedcarddata.addnumofitems();
                return;
            }
        store.add(buyingCardData);
    }

    public void removeBuyingCardData(ItemCardData buyingCardData){

        for(ItemCardData storedcarddata: store)
            if(storedcarddata.equals(buyingCardData)){
                int numofitems = storedcarddata.getItemnumber();
                if(numofitems == 1){
                    store.remove(storedcarddata);
                    return;
                }
                storedcarddata.removenumofitems();
            }
    }

    public ItemCardData getbuyingCardData(String name){
        ItemCardData storedbuyingcarddata = null;
        for(ItemCardData buyingCardData : store)
            if(buyingCardData.getName().equals(name))
                storedbuyingcarddata = buyingCardData;
        return storedbuyingcarddata;
    }

    public int gettotalprice(){
        totalprice = 0;
        for(ItemCardData storedcarddata: store)
            totalprice += storedcarddata.getprice();
        return totalprice;
    }

    public void clear(){
        store.clear();
    }

}
