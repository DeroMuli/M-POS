package com.muli.m_pos.ui.main;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.muli.m_pos.model.AccountInfoProvider;

public class Updater extends AsyncTask<ContentValues,Void,Integer> {

    ContentResolver contentResolver;
    Context context;

    public Updater (ContentResolver contentResolver, Context context){
        this.contentResolver = contentResolver;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(ContentValues... values) {
        ContentValues value = values[0];
        return contentResolver.update(AccountInfoProvider.Product_Uri,value,null,null);
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        String msg;
        if(i < 0)
            msg = "FAILURE";
        else
            msg = "SUCCESS";
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

}
