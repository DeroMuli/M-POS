package com.muli.m_pos.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;

import java.util.ArrayList;

public class UpdatePrice extends AppCompatActivity {

    String product_name;
    private Spinner product_spinner;
    private static final ArrayList<String> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_price);
        Toolbar toolbar = (Toolbar)findViewById(R.id.updateprice_toolbar);
        setSupportActionBar(toolbar);
        SeTUpSpinner seTUpSpinner = new SeTUpSpinner();
        seTUpSpinner.execute();
    }

    public void UpdatePrice(View view){
        EditText priceedit = (EditText)findViewById(R.id.new_price);
        int price = Integer.valueOf(priceedit.getText().toString());
        ContentValues values = new ContentValues();
        values.put(UpdateActivity.type,"price");
        values.put(UpdateActivity.var,price);
        values.put(UpdateActivity.name,product_name);
        Updater updater = new Updater(getContentResolver(),getBaseContext());
        updater.execute(values);
    }

    private class SeTUpSpinner extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            product_spinner = (Spinner)findViewById(R.id.price_spinner);
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