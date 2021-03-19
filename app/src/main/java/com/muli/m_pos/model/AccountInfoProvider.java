package com.muli.m_pos.model;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Base64;

import androidx.annotation.Nullable;

import com.muli.m_pos.Utility.Utility;
import com.muli.m_pos.ui.login.LoadingActivity;
import com.muli.m_pos.ui.main.MainActivity;
import com.muli.m_pos.ui.main.UpdateActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AccountInfoProvider extends RESTfulContentProvider {

    public static String Authority = "com.mpos.data";
    public static String Online_URL = "content://" + Authority + "/Online";
    public static String Offline_URL = "content://" + Authority + "/Offline";
    public static String Category_URL = "content://" + Authority + "/category";
    public static String Product_URL = "content://" + Authority + "/product";
    public static Uri Offline_Uri = Uri.parse(Offline_URL);
    public static Uri Online_Uri = Uri.parse(Online_URL);
    public static Uri Category_Uri = Uri.parse(Category_URL);
    public static Uri Product_Uri = Uri.parse(Product_URL);
    public static String DataBase_Name = "Mpos_Db";
    public static int DataBase_Version = 9;
    public static String Users_Table_Name = "USER";
    public static String User_Name = "Username";
    public static  String PassWord = "Password";
    public static String LOGGED = "Logged";
    public static String Image = "Image";
    public static String Logged_In = "TRUE";
    public static String Logged_Out = "FALSE";
    public static String Category_Table_name = "category";
    public static String Category_id = "category_id";
    public static String Category_name = "cname";
    public static String Product_Table_name = "products";
    public static String Product_id = "products_id";
    public static String Product_name = "pname";
    public static String Product_price = "price";
    public static String Product_image = "img";
    public MposOpenHelper mposopenhelper;
    public final static int offline = 1;
    public final static int online = 2;
    static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Authority , "Online", online);
        uriMatcher.addURI(Authority,"Offline" , offline);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
      if(uri.equals(AccountInfoProvider.Product_Uri))
          return productdelete(uri,selection,selectionArgs);
      else
          return internaldelete(uri,selection,selectionArgs);
    }

    private  int internaldelete(Uri uri, String selection, String[] selectionArgs){
        String tablename = gettablename(uri);
        SQLiteDatabase db = mposopenhelper.getWritableDatabase();
        int count;
        count = db.delete(tablename,selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    private int productdelete(Uri uri, String selection, String[] selectionArgs){
        String name = selectionArgs[0];
        ContentValues values = new ContentValues();
        values.put(AccountInfoProvider.Product_name,name);
        String response = OnlineServices.get(values,MainActivity.IPADRESS + "/DeleteProduct.php");
        if(response.equals("Success"))
            return internaldelete(uri,selection,selectionArgs);
        return -1;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case offline:
                return "vnd.android.cursor.item/offline";
            case online:
                return "vnd.android.cursor.item/online";
            default:
                throw new RuntimeException("Uri not supported");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(uriMatcher.match(uri) == online)
            return online_inserter(uri,values);
        else if(uri.equals(AccountInfoProvider.Category_Uri))
            return category_inserter(uri,values);
        else if(uri.equals(AccountInfoProvider.Product_Uri))
            return Product_Inserter(uri,values);
        else
            return  internal_insert(uri,values);
    }

    private Uri Product_Inserter (Uri uri,ContentValues value){
        Uri imageuri = Uri.parse(value.getAsString(AccountInfoProvider.Product_image));
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageuri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String Imagename = imageuri.getLastPathSegment();
        byte[] image = Utility.getBitmapAsByteArray(bitmap);
        String convertimage = Base64.encodeToString(image,Base64.DEFAULT);
        value.remove(AccountInfoProvider.Product_image);
        value.put(AccountInfoProvider.Product_image,convertimage);
        value.put("imgname",Imagename);
        String response = OnlineServices.get(value,MainActivity.IPADRESS+"/InsertProduct.php");
        if(response.equals("Success")) {
            SQLiteDatabase db = mposopenhelper.getWritableDatabase();
            String cname = value.getAsString(AccountInfoProvider.Category_name);
            value.remove("imgname");
            value.remove(AccountInfoProvider.Product_image);
            value.put(AccountInfoProvider.Product_image,imageuri.toString());
            value.remove(AccountInfoProvider.Category_name);
            int id = 0;
            for(int i : LoadingActivity.id_name_mapping.keySet())
                if(cname.equals(LoadingActivity.id_name_mapping.get(i)))
                    id = i;
            ArrayList<ItemCardData> arrayList = LoadingActivity.Products.get(cname);
            if(arrayList != null ) {
                arrayList.add(new ItemCardData(imageuri.toString(), value.getAsString(AccountInfoProvider.Product_name), value.getAsInteger(AccountInfoProvider.Product_price)));
                LoadingActivity.Products.put(cname, arrayList);
            }
            value.put(AccountInfoProvider.Category_id,id);
            long d = db.insert(Product_Table_name,null,value);
            if(d < 0)
                return  Uri.parse("Internal DB error");
        }
        return Uri.parse(response);
    }

    private Uri category_inserter(Uri uri,ContentValues values){
        String response = OnlineServices.get(values, MainActivity.IPADRESS + "/InsertCategory.php");
        if(response.equals("Success")) {
            String name = values.getAsString(AccountInfoProvider.Category_name);
            LoadingActivity.Categories.add(name);
            return internal_insert(uri, values);
        }
        else
            return Uri.parse("Connect");
    }

    private Uri online_inserter(Uri uri,ContentValues values){
        ContentValues online = new ContentValues();
        online.put(AccountInfoProvider.LOGGED,values.getAsString(AccountInfoProvider.LOGGED));
        online.put(AccountInfoProvider.User_Name,values.getAsString(AccountInfoProvider.User_Name));
        online.put(AccountInfoProvider.PassWord,values.getAsString(AccountInfoProvider.PassWord));
        Uri imageuri = Uri.parse(values.getAsString(AccountInfoProvider.Image));
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageuri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] image = Utility.getBitmapAsByteArray(bitmap);
        String convertimage = Base64.encodeToString(image,Base64.DEFAULT);
        online.put(AccountInfoProvider.Image,convertimage);
        String response = OnlineServices.post_insert(online,MainActivity.IPADRESS + "/Mpos_User.php");
        if(!response.equals("Success"))
            return null;
        return  internal_insert(uri,values);
    }

    private Uri internal_insert(Uri uri,ContentValues values){
        SQLiteDatabase db = mposopenhelper.getWritableDatabase();
        String tablename = gettablename(uri);
        long id = db.insert(tablename, null, values);
        if (id > 0) {
            Uri sucess_uri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(sucess_uri, null);
            return sucess_uri;
        }
        return null;
    }

    private String gettablename (Uri uri){
        String tablename;
        if(uri.equals(AccountInfoProvider.Category_Uri))
            tablename = Category_Table_name;
        else if(uri.equals(AccountInfoProvider.Product_Uri))
            tablename = Product_Table_name;
        else
            tablename = Users_Table_Name;
        return tablename;
    }


    @Override
    public boolean onCreate() {
        mposopenhelper = new MposOpenHelper(getContext(),DataBase_Name,null,DataBase_Version);
        SQLiteDatabase db = mposopenhelper.getWritableDatabase();
        return db == null ? false : true ;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
            String tablename = gettablename(uri);
            if(tablename.equals(AccountInfoProvider.Category_Table_name))
                asyncQueryRequest(tablename);
            if(tablename.equals(AccountInfoProvider.Product_Table_name)){
                asyncQueryRequest(tablename);
            }
            SQLiteDatabase db = mposopenhelper.getReadableDatabase();
            Cursor c = db.query(tablename,projection,selection,selectionArgs,sortOrder,null,null);
            return  c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if(uri.equals(AccountInfoProvider.Product_Uri))
            return Product_Update(uri,values,selection,selectionArgs);
        else
            return internal_update(uri,values,selection,selectionArgs);
    }

    private int Product_Update(Uri uri, ContentValues value, String selection,
                               String[] selectionArgs){
        if(value.getAsString(UpdateActivity.type).equals("pname"))
            return UpdateName(uri,value);
        else if(value.getAsString(UpdateActivity.type).equals("img"))
            return UpdateImage(uri,value);
        else if(value.getAsString(UpdateActivity.type).equals("category"))
            return UpdateCategory(uri,value);
        else
            return UpdatePrice(uri,value);
    }

    private int UpdateCategory(Uri uri,ContentValues value){
        value.put("imgname"," ");
        String response = OnlineServices.get(value,MainActivity.IPADRESS + "/UpdateProduct.php");
        if(response.equals("Success")){
            ContentValues newvalues = new ContentValues();
            String select = AccountInfoProvider.Product_name + " = ? ";
            String cname = value.getAsString(UpdateActivity.var);
            for(int i : LoadingActivity.id_name_mapping.keySet())
                if(LoadingActivity.id_name_mapping.get(i).equals(cname))
                {
                    newvalues.put(AccountInfoProvider.Category_id,i);
                    return internal_update(uri,newvalues,select,new String[]{value.getAsString(UpdateActivity.name)});
                }
        }
        return -1;
    }

    private int UpdateName(Uri uri,ContentValues value){
        value.put("imgname"," ");
        String response = OnlineServices.get(value,MainActivity.IPADRESS + "/UpdateProduct.php");
        if(response.equals("Success")){
            ContentValues newvalues = new ContentValues();
            String name = value.getAsString(UpdateActivity.name);
            String newname = value.getAsString(UpdateActivity.var);
            String select = AccountInfoProvider.Product_name + " = ? ";
            newvalues.put(AccountInfoProvider.Product_name,newname);
            return internal_update(uri,newvalues,select,new String[]{name});
        }
        return -1;
    }

    private int UpdateImage(Uri uri,ContentValues values){
        Uri imageuri =Uri.parse(values.getAsString(UpdateActivity.var));
        values.remove(UpdateActivity.var);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageuri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] image = Utility.getBitmapAsByteArray(bitmap);
        String convertimage = Base64.encodeToString(image,Base64.DEFAULT);
        values.put(UpdateActivity.var,convertimage);
        String imagename = imageuri.getLastPathSegment();
        values.put("imgname",imagename);
        String response = OnlineServices.get(values,MainActivity.IPADRESS + "/UpdateProduct.php");
        if(response.equals("Success")){
            ContentValues newvalues = new ContentValues();
            newvalues.put(AccountInfoProvider.Product_image,imageuri.toString());
            String select = AccountInfoProvider.Product_name + " = ? ";
            String name = values.getAsString(UpdateActivity.name);
            return internal_update(uri,newvalues,select,new String[]{name});
        }
        return -1;
    }

    private int UpdatePrice(Uri uri,ContentValues value){
        value.put("imgname"," ");
        String response = OnlineServices.get(value,MainActivity.IPADRESS + "/UpdateProduct.php");
        if(response.equals("Success")){
            ContentValues newvalues = new ContentValues();
            String name = value.getAsString(UpdateActivity.name);
            int price = value.getAsInteger(UpdateActivity.var);
            String select = AccountInfoProvider.Product_name + " = ? ";
            newvalues.put(AccountInfoProvider.Product_price,price);
            return  internal_update(uri,newvalues,select,new String[]{name});
        }
        return -1;
    }

    private int internal_update(Uri uri, ContentValues values, String selection,
                                String[] selectionArgs){
        String tablename = gettablename(uri);
        SQLiteDatabase db = mposopenhelper.getWritableDatabase();
        int count;
        count = db.update(tablename, values, selection, selectionArgs);
        return count;
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return this.mposopenhelper.getWritableDatabase();
    }

    public static class MposOpenHelper extends SQLiteOpenHelper {

        public MposOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + Users_Table_Name + "(" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + User_Name + " TEXT NOT NULL," + LOGGED + " TEXT NOT NULL, "+ PassWord + " TEXT NOT NULL, " + Image + " TEXT ); ";
            String sql2 = " CREATE TABLE " + Category_Table_name + "(" + Category_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + Category_name  + " TEXT NOT NULL);";
            String sql3 = "CREATE TABLE " + Product_Table_name + "(" + Product_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + Product_name + " TEXT NOT NULL," + Product_price + " INTEGER NOT NULL," + Product_image + " TEXT NOT NULL, " + Category_id + " INTEGER NOT NULL , FOREIGN KEY ("+Category_id+") REFERENCES "+Category_Table_name+" ("+Category_id+") );";
            db.execSQL(sql);
            db.execSQL(sql2);
            db.execSQL(sql3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Users_Table_Name);
            db.execSQL("DROP TABLE IF EXISTS " + Category_Table_name);
            db.execSQL("DROP TABLE IF EXISTS " + Product_Table_name);
            onCreate(db);
        }
    }

}