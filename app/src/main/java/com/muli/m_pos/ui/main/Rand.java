package com.muli.m_pos.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.ItemCardData;
import com.muli.m_pos.model.OnlineServices;
import com.muli.m_pos.model.SetUpProductNCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Rand extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rand);
    }

    public void ble(View view){
        b ba = new b();
        ba.execute();
    }

    private class b extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            ContentValues values = new ContentValues();
            values.put(UpdateActivity.type,"category");
            values.put(UpdateActivity.var,"Pizza");
            values.put(UpdateActivity.name,"latte");
            values.put("imgname"," ");
            String response = OnlineServices.get(values,MainActivity.IPADRESS + "/UpdateProduct.php");
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
        }
    }
}