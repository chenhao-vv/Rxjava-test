package com.vivo.chmusicdemo.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mFragmentTitles;

    public FragmentPageAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments, ArrayList<String> titles) {
        super(fragmentManager);
        mFragmentList = fragments;
        mFragmentTitles = titles;
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem (int position) {
        if(mFragmentList != null) {
            return mFragmentList.get(position);
        } else {
            return null;
        }

    }

    @Override
    public int getCount() {
        if(mFragmentList != null) {
            return mFragmentList.size();
        } else {
            return -1;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mFragmentList != null) {
            return mFragmentTitles.get(position);
        } else {
            return null;
        }
    }
}
