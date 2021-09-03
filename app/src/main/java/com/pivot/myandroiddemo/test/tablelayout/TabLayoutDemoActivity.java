package com.pivot.myandroiddemo.test.tablelayout;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutDemoActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_demo);

        setToolbarTitle("TabLayoutDemo");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        initViewPager();
    }

    private void initViewPager() {
        List<String> tabs = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        tabs.add("推荐");tabs.add("体育");tabs.add("文化");tabs.add("科技");tabs.add("娱乐");tabs.add("电影");
        tabs.add("音乐");tabs.add("电竞");tabs.add("汽车");tabs.add("国内");tabs.add("民生");tabs.add("国外");
        
        for (int i = 0; i < tabs.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs.get(i)));
            fragments.add(TabDemoFragment.newInstance(tabs.get(i)));
        }
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs.get(position);
            }
        });
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }
}
