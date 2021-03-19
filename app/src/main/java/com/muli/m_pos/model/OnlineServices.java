package com.muli.m_pos.model;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class OnlineServices {

    public static String post_insert(ContentValues values, String url){
      return contactserver(values,url);
    }

    public static String get(ContentValues values,String url){
        return contactserver(values,url);
    }

    private static String contactserver(ContentValues values,String stringurl){
        HashMap<String,String> postkeys = new HashMap<>();
        for(String key : values.keySet()){
            postkeys.put(key,values.getAsString(key));
        }
        String response = "nothing";
        try {
            URL url = new URL(stringurl);
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setConnectTimeout(20000);
            httpConnection.setReadTimeout(20000);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            OutputStream outputStream = httpConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            StringBuilder builder = new StringBuilder();
            boolean check = true;
            for (Map.Entry<String,String> m : postkeys.entrySet()){
                if(check)
                    check = false;
                else
                    builder.append("&");
                builder.append(URLEncoder.encode(m.getKey(),"UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(m.getValue(),"UTF-8"));
            }
            writer.write(builder.toString());
            writer.flush();
            writer.close();
            if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder responsebuilder = new StringBuilder();
                String temp;
                while((temp = bufferedReader.readLine()) != null)
                    responsebuilder.append(temp);
                response = responsebuilder.toString();
            }
            else
                response = httpConnection.getResponseMessage();
        }
        catch (MalformedURLException e) {
            response = e.getMessage();
        } catch (IOException e) {
            response = e.getMessage();
        }
        return response;
    }

    public static void getonlinepic(String onlineurl,String localurl){
        try {
            URL url = new URL(onlineurl);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream stream = httpConnection.getInputStream();
                BufferedInputStream inputStream = new BufferedInputStream(stream);
                FileOutputStream fout = new FileOutputStream(localurl);
                byte[] bytes = new byte[4 * 1024];
                int r = 0;
                do {
                    r = inputStream.read(bytes);
                    if (r >= 0) {
                        fout.write(bytes, 0, r);
                    }
                } while (r >= 0);
                inputStream.close();
                fout.close();
            }
            httpConnection.disconnect();

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }

}


