/*
 * *********************************************************
 *   author   zhuxuetong
 *   company  telchina
 *   email    zhuxuetong123@163.com
 *   date     18-10-9 上午11:17
 * ********************************************************
 */

package com.pivot.myandroiddemo.adpter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 子页面 ViewPager适配器
 */
public class ChildPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> listFragment;
    private List<String>   listTitle;

    public ChildPagerAdapter(FragmentManager fm, List<Fragment> listFragment, List<String> listTitle) {
        super(fm);
        this.listFragment = listFragment;
        this.listTitle = listTitle;
    }

    @Override
    public int getCount() {
        return listFragment != null && listFragment.size() > 0 ? listFragment.size() : 0;
    }

    @Override
    public Fragment getItem(int arg0) {
        return listFragment.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle == null ? super.getPageTitle(position) : listTitle.get(position);
    }
}
