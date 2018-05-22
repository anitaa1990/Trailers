package com.an.trailers.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.an.trailers.fragment.CommonFragment;

import java.util.ArrayList;
import java.util.List;

public class CommonPagerAdapter extends FragmentStatePagerAdapter {

    List<CommonFragment> fragments;

    public CommonPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
    }


    public CommonPagerAdapter(FragmentManager fm, List<CommonFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        CommonFragment fragment = fragments.get(position);
        return fragment;
    }

    public void setFragments(List<CommonFragment> commonFragments) {
        fragments.addAll(commonFragments);
        refreshAdapter();
    }

    public List<CommonFragment> getFragments() {
        return fragments;
    }

    public void addFragment(CommonFragment fragment) {
        fragments.add(fragment);
    }

    public void removeFragments() {
        fragments.clear();
        refreshAdapter();
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
