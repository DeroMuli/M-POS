package com.muli.m_pos.ui.login;

import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.muli.m_pos.R;
import com.muli.m_pos.Utility.Utility;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.OnlineServices;
import com.muli.m_pos.ui.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class LoginActivity extends LoginSignUpMasterClass {

    public EditText username ;
    public EditText password ;
    public Button loginbutton ;
    public ProgressBar progressBar ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginbutton = (Button)findViewById(R.id.login);
        progressBar = (ProgressBar)findViewById(R.id.loading);
        super.setup();
        Toolbar toolbar  = (Toolbar)findViewById(R.id.login_tool_bar);
        toolbar.setTitle(R.string.login_label);
        setSupportActionBar(toolbar);
    }

    @Override
    public EditText getUsernameEditText() {
        return  username;
    }

    @Override
    public EditText getPasswordEditText() {
        return password;
    }

    @Override
    public Button getLoginButton() {
        return loginbutton;
    }

    @Override
    public ProgressBar getLoadingProgressBar() {
        return progressBar;
    }

    public void register(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    public void Login(View view){
        String _username = username.getText().toString();
        String _password = password.getText().toString();
        Login login = new Login(_username,_password);
        login.execute();
    }

    private class  Login extends AsyncTask<Void,Void,String>{

        String username;
        String password;

        public Login(String username,String password){
            this.username = username;
            this.password = password;
        }
        //Update or write internal database
        @Override
        protected String doInBackground(Void... voids) {
            ContentValues values = new ContentValues();
            values.put(AccountInfoProvider.User_Name,username);
            String jsonresponse = OnlineServices.get(values,MainActivity.IPADRESS + "/Login.php");
            try {
                JSONObject jsonObject = new JSONObject(jsonresponse);
                String returnedpassword = jsonObject.getString(AccountInfoProvider.PassWord);
                if(returnedpassword.equals(password)) {
                    insertintoDB(jsonObject);
                    return "Success";
                }
                else
                    return "Password is wrong";
            } catch (JSONException e) {
                return "Account doesnt exist";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Success")){
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
        }
    }

    private void insertintoDB(JSONObject jsonObject){
        try {
            String username = jsonObject.getString(AccountInfoProvider.User_Name);
            String password = jsonObject.getString(AccountInfoProvider.PassWord);
            String imagetag = jsonObject.getString(AccountInfoProvider.Image);
            String selection = AccountInfoProvider.User_Name + " = ? ";
            Cursor c = getContentResolver().query(AccountInfoProvider.Offline_Uri,null,selection,new String[]{username},null);
            if(c.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(AccountInfoProvider.LOGGED,AccountInfoProvider.Logged_In);
                String select = AccountInfoProvider.User_Name + " = ? ";
                getContentResolver().update(AccountInfoProvider.Offline_Uri,values,select,new String[]{username});
                return;
            }
            else {
                StringTokenizer tokenizer = new StringTokenizer(imagetag,"/");
                tokenizer.nextToken();
                String location = getFilesDir().getAbsolutePath() + "/" + tokenizer.nextToken();
                OnlineServices.getonlinepic(MainActivity.IPADRESS + imagetag,location );
                ContentValues values = new ContentValues();
                values.put(AccountInfoProvider.Image,location);
                values.put(AccountInfoProvider.User_Name,username);
                values.put(AccountInfoProvider.PassWord,password);
                values.put(AccountInfoProvider.LOGGED,AccountInfoProvider.Logged_In);
                getContentResolver().insert(AccountInfoProvider.Offline_Uri,values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}