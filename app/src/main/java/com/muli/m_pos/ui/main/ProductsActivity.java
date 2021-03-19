package com.muli.m_pos.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.muli.m_pos.R;
import com.muli.m_pos.model.Product_Fragment_Factory;
import com.muli.m_pos.model.SetUpProductNCategories;
import com.muli.m_pos.ui.login.LoadingActivity;

public class ProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        this.setuptoolbar();
        this.setupviewpager();
    }

    private void setuptoolbar(){
        ProfileToolBarFactory.ProfileFrag profileFrag = ProfileToolBarFactory.getProfile();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.toolbar_holder,profileFrag);
        transaction.commit();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void setupviewpager(){

        ViewPager2 viewPager2 = (ViewPager2)findViewById(R.id.viewpager);
        SectionAdapter sectionAdapter = new SectionAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(sectionAdapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(LoadingActivity.Categories.isEmpty())
                    tab.setText("Add Categories and Products");
                else
                    tab.setText(LoadingActivity.Categories.get(position));
            }
        });
        mediator.attach();
    }


    private class SectionAdapter extends FragmentStateAdapter {

        public SectionAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if(LoadingActivity.Categories.isEmpty())
                return Product_Fragment_Factory.GetProductFragment("");
            else
                return Product_Fragment_Factory.GetProductFragment(LoadingActivity.Categories.get(position));
        }

        @Override
        public int getItemCount() {
            if(LoadingActivity.Categories.isEmpty())
                return 1;
            else
                return LoadingActivity.Categories.size();
        }
    }

}