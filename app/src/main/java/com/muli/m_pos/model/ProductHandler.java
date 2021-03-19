package com.muli.m_pos.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.muli.m_pos.ui.login.LoadingActivity;
import com.muli.m_pos.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ProductHandler implements ResponseHandler {

    @Override
    public void handleresponse(String jsonresponse, RESTfulContentProvider resTfulContentProvider, String requesttag, Context context) {
        try {
                        SQLiteDatabase db = resTfulContentProvider.getDatabase();
                        JSONArray jsonArray = new JSONArray(jsonresponse);
                        Cursor c = db.query(AccountInfoProvider.Product_Table_name,null,null,null,null,null,null);
                        inserter(jsonArray, db, context);
                        deleter(jsonArray, db);
                        updater(jsonArray, db, context,c);
        } catch (JSONException e) {
            resTfulContentProvider.requestComplete(requesttag);
            return;
        }
    }

    private void inserter(JSONArray jsonArray,SQLiteDatabase db,Context context) throws JSONException {
        for(int i = 0 ; i < jsonArray.length() ; i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            Boolean checker = false;
            for(ArrayList<ItemCardData> arrayList : LoadingActivity.Products.values())
            for(ItemCardData itemCardData : arrayList)
                if(itemCardData.getName().equals(jsonObject.getString("pname")))
                    checker = true;
             if(checker == false){
                 inserttproduct(jsonObject,db,context);
             }
        }
    }

    private void inserttproduct(JSONObject jsonObject,SQLiteDatabase db,Context context) throws JSONException {
            String cname = jsonObject.getString("cname");
            String pname = jsonObject.getString("pname");
            int price = jsonObject.getInt("price");
            String img = jsonObject.getString("img");
            StringTokenizer tokenizer = new StringTokenizer(img, "/");
            tokenizer.nextToken();
            String location = context.getFilesDir().getAbsolutePath() + "/" + tokenizer.nextToken();
            OnlineServices.getonlinepic(MainActivity.IPADRESS + img, location);
            File file = new File(location);
            Uri imageuri = Uri.fromFile(file);
            ContentValues values = new ContentValues();
            values.put(AccountInfoProvider.Product_price, price);
            values.put(AccountInfoProvider.Product_name, pname);
            values.put(AccountInfoProvider.Product_image, imageuri.toString());
            int id = 0;
            for (int i : LoadingActivity.id_name_mapping.keySet())
                if (LoadingActivity.id_name_mapping.get(i).equals(cname))
                    id = i;

            values.put(AccountInfoProvider.Category_id, id);
            db.insert(AccountInfoProvider.Product_Table_name, null, values);
            ItemCardData itemCardData = new ItemCardData(imageuri.toString(), pname, price);
            ArrayList<ItemCardData> arrayList = LoadingActivity.Products.get(cname);
            arrayList.add(itemCardData);
            LoadingActivity.Products.put(cname, arrayList);
    }

    private void deleter(JSONArray jsonArray,SQLiteDatabase db) throws JSONException {
            for (ArrayList<ItemCardData> arrayList : LoadingActivity.Products.values()) {
                for (int k = 0; k < arrayList.size(); k++) {
                    ItemCardData itemCardData = arrayList.get(k);
                    Boolean checker = false;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String pname = jsonObject.getString("pname");
                        if (pname.equals(itemCardData.getName()))
                            checker = true;
                    }
                    if (checker == false) {
                        arrayList.remove(k);
                        String select = AccountInfoProvider.Product_name + " = ? ";
                        db.delete(AccountInfoProvider.Product_Table_name, select, new String[]{itemCardData.getName()});
                    }
                }
            }
    }

    private void updater(JSONArray jsonArray,SQLiteDatabase db,Context context,Cursor c) throws JSONException {
        UpdatePrice(jsonArray,db,c);
        UpdateCategory(jsonArray,db,c);
        UpdateImage(jsonArray,db,context,c);
    }

    private void UpdatePrice(JSONArray jsonArray,SQLiteDatabase db,Cursor c) throws JSONException {
          if(c.moveToFirst())
              do{
                  String offline_name = c.getString(1);
                  for(int i = 0 ; i < jsonArray.length() ; i++){
                      JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                      String pname = jsonObject.getString("pname");
                      if(pname.equals(offline_name)){
                          int offline_price = c.getInt(2);
                          int onnline_price = jsonObject.getInt("price");
                          if(offline_price != onnline_price){
                              String select = AccountInfoProvider.Product_name + " = ? ";
                              ContentValues values = new ContentValues();
                              values.put(AccountInfoProvider.Product_price,onnline_price);
                              db.update(AccountInfoProvider.Product_Table_name,values,select,new String[]{offline_name});
                          }
                      }
                  }

              }while (c.moveToNext());
    }

    private void UpdateCategory(JSONArray jsonArray,SQLiteDatabase db,Cursor c) throws JSONException {
        if (c.moveToFirst())
            do {
                String offline_name = c.getString(1);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String pname = jsonObject.getString("pname");
                    if (pname.equals(offline_name) ){
                        String online_category = jsonObject.getString("cname");
                        String selection = AccountInfoProvider.Category_id + " = ? ";
                        int id = c.getInt(4);
                        Cursor cursor = db.query(AccountInfoProvider.Category_Table_name,null,selection,new String[]{String.valueOf(id)},null,null,null);
                        cursor.moveToFirst();
                        String offline_category = cursor.getString(1);
                        if (!offline_category.equals(online_category)) {
                                String select2 = AccountInfoProvider.Category_name + " = ? ";
                                Cursor cursor1 = db.query(AccountInfoProvider.Category_Table_name,null,select2,new String[]{online_category},null,null,null);
                                cursor1.moveToFirst();
                                int new_id = cursor1.getInt(0);
                                String select = AccountInfoProvider.Product_name + " = ? ";
                                ContentValues value = new ContentValues();
                                value.put(AccountInfoProvider.Category_id, new_id);
                                db.update(AccountInfoProvider.Product_Table_name, value, select, new String[]{offline_name});
                            }
                        }
                    }
            } while (c.moveToNext());
    }


    private void UpdateImage(JSONArray jsonArray,SQLiteDatabase db,Context context,Cursor c) throws JSONException {
        if(c.moveToFirst())
            do {
                String offline_name = c.getString(1);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String pname = jsonObject.getString("pname");
                    if (pname.equals(offline_name)) {
                        String string_uri = c.getString(3);
                        Uri imageuri = Uri.parse(string_uri);
                        String path = jsonObject.getString(AccountInfoProvider.Product_image);
                        String offline_img = imageuri.getLastPathSegment();
                        StringTokenizer tokenizer = new StringTokenizer(path, "/");
                        tokenizer.nextToken();
                        String onnline_img = tokenizer.nextToken();
                        if (offline_img != onnline_img) {
                            String location = context.getFilesDir().getAbsolutePath() + "/" + onnline_img;
                            OnlineServices.getonlinepic(MainActivity.IPADRESS + path, location);
                            File file = new File(location);
                            Uri oflline_uri = Uri.fromFile(file);
                            String select = AccountInfoProvider.Product_name + " = ? ";
                            ContentValues value = new ContentValues();
                            value.put(AccountInfoProvider.Product_image, oflline_uri.toString());
                            db.update(AccountInfoProvider.Product_Table_name, value, select, new String[]{offline_name});
                        }
                    }
                }
            }while (c.moveToNext());
    }
}
