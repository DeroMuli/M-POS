package com.muli.m_pos.ui.login;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.muli.m_pos.R;
import com.muli.m_pos.Utility.Utility;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.OnlineServices;
import com.muli.m_pos.ui.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class SignUpActivity extends LoginSignUpMasterClass {

    EditText username ;
    EditText password;
    Button login;
    ProgressBar progressBar;
    Uri imageuri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        progressBar = (ProgressBar)findViewById(R.id.loading);
        super.setup();
        Toolbar toolbar = (Toolbar) findViewById(R.id.sighn_up_toolbar);
        setSupportActionBar(toolbar);
        this.setupimagedisplay();
    }

    private void setupimagedisplay(){
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
                        getpicfromcamera.putExtra(MediaStore.EXTRA_OUTPUT,AccountInfoProvider.Online_Uri);
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
            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageURI(imageuri);
        }
        if(resultCode == RESULT_OK && requestCode == 2){

        }
    }

    public void register(View view) {
        String _username = username.getText().toString();
        String _password = password.getText().toString();
        Register register = new Register(_username,_password,imageuri.toString());
        register.execute();
    }

    public void loginpage(View view){
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public EditText getUsernameEditText() {
        return username;
    }

    @Override
    public EditText getPasswordEditText() {
        return password;
    }

    @Override
    public Button getLoginButton() {
        return login;
    }

    @Override
    public ProgressBar getLoadingProgressBar() {
        return progressBar;
    }


    private class Register extends AsyncTask<Void, Void, String> {

        String username;
        String password;
        String image;
        Register(String username,String password,String image){
            this.username = username;
            this.password = password;
            this.image = image;
        }

        @Override
        protected String doInBackground(Void... voids) {
            ContentValues values = new ContentValues();
            values.put(AccountInfoProvider.Image,image);
            values.put(AccountInfoProvider.LOGGED,AccountInfoProvider.Logged_In);
            values.put(AccountInfoProvider.User_Name,username);
            values.put(AccountInfoProvider.PassWord,password);
            Uri uri = getContentResolver().insert(AccountInfoProvider.Online_Uri,values);
            if(uri == null)
                return "Connect to the internet";
            else
                return "Success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("Success")){
               Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
               startActivity(intent);
            }
            Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
        }
    }

}