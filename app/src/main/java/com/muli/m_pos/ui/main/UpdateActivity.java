package com.muli.m_pos.ui.main;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.muli.m_pos.R;
import com.muli.m_pos.Utility.Utility;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.OnlineServices;

import java.io.FileNotFoundException;

public class UpdateActivity extends AppCompatActivity {

    public static  String type = "type";
    public static  String var = "var";
    public static  String name = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar)findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
    }

    public void GoToPrice(View view){
         Intent intent = new Intent(UpdateActivity.this,UpdatePrice.class);
        startActivity(intent);
    }

    public void GoToImage(View view){
        Intent intent = new Intent(UpdateActivity.this,UpdateImage.class);
        startActivity(intent);
    }

    public void GoToName(View view){
        Intent intent = new Intent(UpdateActivity.this,UpdateName.class);
        startActivity(intent);
    }

    public void GoToCategory(View view){
        Intent intent = new Intent(UpdateActivity.this,UpdateCategory.class);
        startActivity(intent);
    }

}