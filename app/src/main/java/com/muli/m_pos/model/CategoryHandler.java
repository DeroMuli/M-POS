package com.muli.m_pos.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.muli.m_pos.ui.login.LoadingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryHandler implements ResponseHandler {
    @Override
    public void handleresponse(String jsonresponse, RESTfulContentProvider resTfulContentProvider, String requesttag, Context context) {
        try {
            JSONArray jsonArray = new JSONArray(jsonresponse);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                String name = jsonObject.getString("cname");
                Boolean checker = false;
                for (String cname : LoadingActivity.Categories)
                    if(cname.equals(name))
                        checker = true;
                if(checker == false) {
                    SQLiteDatabase db = resTfulContentProvider.getDatabase();
                    ContentValues values = new ContentValues();
                    values.put(AccountInfoProvider.Category_name, name);
                    db.insert(AccountInfoProvider.Category_Table_name, null, values);
                    LoadingActivity.Categories.add(name);
                }
            }
            resTfulContentProvider.requestComplete(requesttag);
        } catch (JSONException e) {
            resTfulContentProvider.requestComplete(requesttag);
            return;
        }
    }
}
