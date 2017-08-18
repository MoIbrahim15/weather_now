package com.mohamedibrahim.weathernow.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.adapters.PagerAdapter;
import com.mohamedibrahim.weathernow.listeners.FragmentToActivityListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mohamed Ibrahim on 10/29/2016.
 **/

public class HomeFragment extends ParentFragment {

    @BindView(R.id.tabs_home)
    TabLayout tabLayout;
    @BindView(R.id.pager_home)
    ViewPager viewPager;
    private ArrayList<Fragment> FragmentArrayList;

    public static HomeFragment newInstance(FragmentToActivityListener fragmentToActivityListener) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setFragmentToActivityListener(fragmentToActivityListener);
        return homeFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initTabs();

        return view;
    }

    private void initTabs() {
        View tabMap = getActivity().getLayoutInflater().inflate(R.layout.tab_custom_views, null);
        View tabCities = getActivity().getLayoutInflater().inflate(R.layout.tab_custom_views, null);

        tabMap.findViewById(R.id.tab_map).setBackgroundResource(R.drawable.tab_map_selector);
        tabCities.findViewById(R.id.tab_cities).setBackgroundResource(R.drawable.tab_city_selector);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabMap));
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabCities));

        FragmentArrayList = new ArrayList<>();
        FragmentArrayList.add(MapFragment.newInstance());
        FragmentArrayList.add(CitiesFragment.newInstance());

        initPager();
        tabListener();

    }

    private void initPager() {
        viewPager.setAdapter(new PagerAdapter(FragmentArrayList, getFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void tabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment current = FragmentArrayList.get(tab.getPosition());
                if (fragmentToActivityListener != null) {
                    if (current instanceof MapFragment) {
                        fragmentToActivityListener.changeTitle(R.string.map);
                    } else {
                        fragmentToActivityListener.changeTitle(R.string.cities);
                        ((CitiesFragment) current).onResumeFragment();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
