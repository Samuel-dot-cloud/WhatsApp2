package com.studiofive.whatsapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;


import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
   @BindView(R.id.main_page_toolbar)
   Toolbar mToolbar;
   @BindView(R.id.main_tabs_pager)
    ViewPager mViewPager;
   @BindView(R.id.main_tabs)
    TabLayout mTabLayout;

   private TabsAccessorAdapter mTabsAccessorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String title = "WhatsApp 2";
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title);

        mTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mTabsAccessorAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


}