package com.muli.m_pos.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.OnlineServices;

import java.util.ArrayList;

public class DeleteProduct extends AppCompatActivity {

    String product_name;
    private Spinner product_spinner;
    private static final ArrayList<String> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);
        Toolbar toolbar = (Toolbar)findViewById(R.id.delete_toolbar);
        setSupportActionBar(toolbar);
        SeTUpSpinner seTUpSpinner = new SeTUpSpinner();
        seTUpSpinner.execute();
    }

    public void checker(View view){
        Intent intent = new Intent(this,Rand.class);
        startActivity(intent);
    }

    public void Delete(View view){
        Deleter deleter = new Deleter();
        deleter.execute(product_name);
    }

    private class Deleter extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            String select = AccountInfoProvider.Product_name + " = ? ";
            String name = strings[0];
            return getContentResolver().delete(AccountInfoProvider.Product_Uri,select,new String[]{name});
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            String msg;
            if(i < 0)
                msg = "FAIL";
            else
                msg = "Success";
            Toast.makeText(getBaseContext(),msg,Toast.LENGTH_LONG).show();
        }
    }

    private class SeTUpSpinner extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            product_spinner = (Spinner)findViewById(R.id.delete_name_spinner);
            Cursor c = getContentResolver().query(AccountInfoProvider.Product_Uri, new String[]{AccountInfoProvider.Product_name}, null, null, null);
            if (c.moveToFirst())
                do {
                    String name = c.getString(0);
                    products.add(name);
                } while (c.moveToNext());
            ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, products);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            product_spinner.setAdapter(arrayAdapter);
            product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    product_name = products.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return null;
        }
    }
}