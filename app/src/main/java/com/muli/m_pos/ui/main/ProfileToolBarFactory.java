package com.muli.m_pos.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;

public final class ProfileToolBarFactory {

    static String username;
    public static Uri imageuri;

    public static ProfileFrag getProfile(){
        return new ProfileFrag();
    }

    public static void SetUpProfile(Cursor c){
        if(c.moveToFirst()){
         String imageurl = c.getString(4);
         username = c.getString(1);
         if(imageurl == null)
             imageuri = null;
         else
             imageuri = Uri.parse(imageurl);
        }
    }

    public static class ProfileFrag extends Fragment{

        View root;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root =  inflater.inflate(R.layout.profile_layout,container,false);
            TextView profilename = (TextView)root.findViewById(R.id.profilename);
            ImageView profilepic = (ImageView)root.findViewById(R.id.profilepic);
            if(username != null)
                profilename.setText(username);
            if(imageuri != null)
                profilepic.setImageURI(imageuri);
            return root;
        }

    }


}
