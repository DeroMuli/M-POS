package com.muli.m_pos.ui.main;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;

import java.util.ArrayList;

public class UpdateImage extends AppCompatActivity {

    Uri imageuri;
    String product_name;
    private Spinner product_spinner;
    private static final ArrayList<String> products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);
        Toolbar toolbar = (Toolbar)findViewById(R.id.updateimg_toolbar);
        setSupportActionBar(toolbar);
        SeTUpSpinner seTUpSpinner = new SeTUpSpinner();
        seTUpSpinner.execute();
        this.setupproductimage();
    }

    private void setupproductimage(){
        ImageView imageView = (ImageView)findViewById(R.id.newproductimage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateImage.this);
                builder.setTitle("Upload or Take a picture");
                builder.setPositiveButton("Upload a picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent getpicfromgalery = new Intent(Intent.ACTION_GET_CONTENT);
                        getpicfromgalery.setType("image/*");
                        startActivityForResult(getpicfromgalery,1);
                    }
                });
                builder.setNegativeButton("Take a picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent getpicfromcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        getpicfromcamera.putExtra(MediaStore.EXTRA_OUTPUT, AccountInfoProvider.Online_Uri);
                        if (getpicfromcamera.resolveActivity(getPackageManager()) != null)
                            startActivityForResult(getpicfromcamera,2);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1){
            imageuri = data.getData();
            ImageView imageView = (ImageView)findViewById(R.id.newproductimage);
            imageView.setImageURI(imageuri);
        }
        if(resultCode == RESULT_OK && requestCode == 2){

        }
    }

    public void UpdateImage(View view){
        ContentValues values = new ContentValues();
        values.put(UpdateActivity.name, product_name);
        values.put(UpdateActivity.type,"img");
        values.put(UpdateActivity.var,imageuri.toString());
        Updater updater = new Updater(getContentResolver(),getBaseContext());
        updater.execute(values);
    }

    private class SeTUpSpinner extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            product_spinner = (Spinner)findViewById(R.id.image_spinner);
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