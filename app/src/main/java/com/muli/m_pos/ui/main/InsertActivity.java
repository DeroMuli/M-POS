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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.muli.m_pos.R;
import com.muli.m_pos.Utility.Utility;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.OnlineServices;
import com.muli.m_pos.ui.login.LoadingActivity;

import java.io.FileNotFoundException;

public class InsertActivity extends AppCompatActivity {

    Uri imageuri;
    private String category_name;
    private Spinner category_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        this.setupproductimage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.insert_toolbar);
        setSupportActionBar(toolbar);
        this.setupspinner();
    }

    private void setupproductimage(){
        ImageView imageView = (ImageView)findViewById(R.id.setproductimage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
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
            ImageView imageView = (ImageView)findViewById(R.id.setproductimage);
            imageView.setImageURI(imageuri);
        }
        if(resultCode == RESULT_OK && requestCode == 2){

        }
    }

    public void AddProduct(View view){
        EditText editname = (EditText)findViewById(R.id.nameofproduct);
        EditText editprice = (EditText)findViewById(R.id.priceofproduct);
        String name = editname.getText().toString();
        String price = editprice.getText().toString();
        ContentValues values = new ContentValues();
        values.put(AccountInfoProvider.Product_name,name);
        values.put(AccountInfoProvider.Category_name,category_name);
        values.put(AccountInfoProvider.Product_price,price);
        values.put(AccountInfoProvider.Product_image,imageuri.toString());
        ProductAdder productAdder = new ProductAdder();
        productAdder.execute(values);
    }

    private void setupspinner(){
        category_spinner = (Spinner)findViewById(R.id.categoryofproductspinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, LoadingActivity.Categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(arrayAdapter);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_name = LoadingActivity.Categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class ProductAdder extends AsyncTask<ContentValues,Void,String>{

        @Override
        protected String doInBackground(ContentValues... values) {
            ContentValues value = values[0];
            Uri uri = getContentResolver().insert(AccountInfoProvider.Product_Uri,value);
            return  uri.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    public void AddCategory(View view){
        EditText category = (EditText)findViewById(R.id.category_name);
        String categoryname = category.getText().toString();
        AddCategory addCategory = new AddCategory();
        addCategory.execute(categoryname);
    }

    private class AddCategory extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            ContentValues values = new ContentValues();
            values.put(AccountInfoProvider.Category_name,name);
            Uri uri = getContentResolver().insert(AccountInfoProvider.Category_Uri,values);
            return uri.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
        }

    }
}