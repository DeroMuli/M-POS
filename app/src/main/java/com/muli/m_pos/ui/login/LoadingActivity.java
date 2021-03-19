package com.muli.m_pos.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.ItemCardData;
import com.muli.m_pos.model.SetUpProductNCategories;
import com.muli.m_pos.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadingActivity extends AppCompatActivity {

    public static  ArrayList<String> Categories = new ArrayList<>();
    public static HashMap<String,ArrayList> Products = new HashMap<>();
    public static HashMap<Integer,String> id_name_mapping = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        SetUp setUp = new SetUp();
        setUp.execute();
        Load load = new Load();
        load.execute();
    }

    private class Load extends AsyncTask<Void,Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            String selecion = AccountInfoProvider.LOGGED + " = ? ";
            Cursor c = getContentResolver().query(AccountInfoProvider.Offline_Uri,null,selecion,new String[]{AccountInfoProvider.Logged_In},null);
            if(c.moveToFirst())
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if(s == false){
                Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        }

    }

    private class SetUp extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor category_cursor = getContentResolver().query(AccountInfoProvider.Category_Uri, null, null, null, null);
            if (category_cursor.moveToFirst()) {
                do {
                    String category_name = category_cursor.getString(1);
                    int id = category_cursor.getInt(0);
                    Categories.add(category_name);
                    id_name_mapping.put(id, category_name);

                } while (category_cursor.moveToNext());
            }
            for (int i : id_name_mapping.keySet()) {
                ArrayList<ItemCardData> arrayList = new ArrayList<>();
                String select = AccountInfoProvider.Category_id + " = ? ";
                Cursor product_cursor = getContentResolver().query(AccountInfoProvider.Product_Uri, null, select, new String[]{String.valueOf(i)}, null);
                if (product_cursor.moveToFirst()) {
                    do {
                        String name = product_cursor.getString(1);
                        int price = product_cursor.getInt(2);
                        String image = product_cursor.getString(3);
                        ItemCardData itemCardData = new ItemCardData(image, name, price);
                        arrayList.add(itemCardData);
                    } while (product_cursor.moveToNext());
                }
                Products.put(id_name_mapping.get(i), arrayList);
            }
            return null;
        }
    }

}