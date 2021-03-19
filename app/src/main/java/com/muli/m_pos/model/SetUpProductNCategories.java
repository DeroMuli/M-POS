package com.muli.m_pos.model;

import android.content.ContentResolver;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

public class SetUpProductNCategories implements Runnable {

    ContentResolver contentResolver;
    //public static  ArrayList<String> Categories = new ArrayList<>();
   // public static HashMap<String,ArrayList> Products = new HashMap<>();
   // public static HashMap<Integer,String> id_name_mapping = new HashMap<>();

    public SetUpProductNCategories(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    @Override
    public void run() {

    }
}
