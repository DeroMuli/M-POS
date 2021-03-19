package com.muli.m_pos.model;

import android.content.ContentValues;
import android.content.Context;

import com.muli.m_pos.ui.main.MainActivity;

public  class UriRequestTask implements Runnable {

    RESTfulContentProvider siteProvider;
    String uniqueuri;
    Context appContext;

    public UriRequestTask(RESTfulContentProvider siteProvider,
                          String uniqueuri, Context appContext){
        this.siteProvider = siteProvider;
        this.uniqueuri = uniqueuri;
        this.appContext = appContext;
    }

    @Override
    public void run() {
        if(uniqueuri.equals(AccountInfoProvider.Category_Table_name))
            CategoryTask();
        else
            ProductTask();
    }

    private void CategoryTask(){
        ContentValues values = new ContentValues();
        String response = OnlineServices.get(values, MainActivity.IPADRESS + "/CategoryQuery.php");
        CategoryHandler categoryHandler = new CategoryHandler();
        categoryHandler.handleresponse(response,siteProvider,uniqueuri,appContext);
    }

    private void ProductTask(){
        ContentValues values = new ContentValues();
        String response = OnlineServices.get(values, MainActivity.IPADRESS + "/ProductQuery.php");
        ProductHandler productHandler = new ProductHandler();
        productHandler.handleresponse(response,siteProvider,uniqueuri,appContext);
    }
}
