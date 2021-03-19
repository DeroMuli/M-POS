package com.muli.m_pos.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.content.CursorLoader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.muli.m_pos.R;
import com.muli.m_pos.model.AccountInfoProvider;
import com.muli.m_pos.model.SetUpProductNCategories;
import com.muli.m_pos.ui.login.LoadingActivity;
import com.muli.m_pos.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    public static  DisplayMetrics ScreenDisplay = new DisplayMetrics();
    public static  String IPADRESS = "http://192.168.43.144";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setupnavigationbar();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(ScreenDisplay);
        this.setupprofile();
    }

    private void setupprofile(){
        String selection = AccountInfoProvider.LOGGED + " = ? ";
        CursorLoader loader = new CursorLoader(getBaseContext(), AccountInfoProvider.Offline_Uri,null,selection, new String[]{AccountInfoProvider.Logged_In},null);
        Cursor c = loader.loadInBackground();
        ProfileToolBarFactory.SetUpProfile(c);
    }

    private void setupnavigationbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.buy_items_menu_item){
                    Intent intent = new Intent(MainActivity.this,BuyingActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.log_out_menu_item){
                    ContentValues values = new ContentValues();
                    values.put(AccountInfoProvider.LOGGED,AccountInfoProvider.Logged_Out);
                    String selection = AccountInfoProvider.LOGGED + " = ? ";
                    int count = getContentResolver().update(AccountInfoProvider.Offline_Uri,values,selection,new String[]{AccountInfoProvider.Logged_In});
                    if(count > 0){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId() == R.id.insert_product){
                    Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.update_product){
                    Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.delete_product){
                    Intent intent = new Intent(MainActivity.this,DeleteProduct.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.Close);
        drawerLayout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.buy_order:
                Intent buyorder = new Intent(MainActivity.this,BuyingActivity.class);
                startActivity(buyorder);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }



}