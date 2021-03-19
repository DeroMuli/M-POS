package com.muli.m_pos.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Temp {

    private  String login(String username,String password){
        HashMap<String,String> postkeys = new HashMap<>();
        postkeys.put("username",username);
        postkeys.put("Password",password);
        String response = "nothing";
        try {
            URL url = new URL("http://192.168.43.181/Login.php");
            HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setConnectTimeout(20000);
            httpConnection.setReadTimeout(20000);
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
}
