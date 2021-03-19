package com.muli.m_pos.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.ui.login.LoadingActivity;

import java.util.ArrayList;

public class UpdateCategory extends AppCompatActivity  {

    private Spinner category_spinner;
    private Spinner product_spinner;
    private static  final ArrayList<String> categories = new ArrayList<>();
    private static final ArrayList<String> products = new ArrayList<>();
    private String category_name;
    private String product_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        Toolbar toolbar = (Toolbar)findViewById(R.id.updatecategory_toolbar);
        setSupportActionBar(toolbar);
        SetUpSpineer setUpSpineer = new SetUpSpineer();
        setUpSpineer.execute();
    }

    public void UpdateCategory(View view){
        ContentValues values = new ContentValues();
        values.put(UpdateActivity.type,"category");
        values.put(UpdateActivity.var, category_name);
        values.put(UpdateActivity.name,product_name);
        Updater updater = new Updater(getContentResolver(),getBaseContext());
        updater.execute(values);
    }

    private class SetUpSpineer extends AsyncTask<Void,Void,Void>  {

        @Override
        protected Void doInBackground(Void... voids) {
             category_spinner = (Spinner)findViewById(R.id.cspinner);
             product_spinner = (Spinner)findViewById(R.id.pspinner);
             setupspiner(category_spinner,AccountInfoProvider.Category_Uri,AccountInfoProvider.Category_name,categories);
             setupspiner(product_spinner,AccountInfoProvider.Product_Uri,AccountInfoProvider.Product_name,products);

             return null;
        }

        private void setupspiner(Spinner spinner, Uri uri,String column,ArrayList<String> list){
            Cursor c = getContentResolver().query(uri,new String[]{column},null,null,null);
            if(c.moveToFirst())
                do{
                    String name = c.getString(0);
                    list.add(name);
                }while (c.moveToNext());
            ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1, list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(uri.equals(AccountInfoProvider.Product_Uri))
                        product_name = list.get(position);
                    else
                        category_name = list.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }



}