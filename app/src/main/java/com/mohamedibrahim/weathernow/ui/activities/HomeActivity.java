package com.mohamedibrahim.weathernow.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.ui.fragments.HomeFragment;
import com.mohamedibrahim.weathernow.ui.fragments.MapFragment;
import com.mohamedibrahim.weathernow.listeners.LocationSettingListener;

import butterknife.ButterKnife;

public class HomeActivity extends ParentActivity {

    private FragmentManager fm;
    private LocationSettingListener mLocationListener;
    public static final int LOCATION_SETTING_REQUEST = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        fm = getSupportFragmentManager();
        showHomeFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * Using beginTransaction to replace fragment and change Actionbar title
     *
     * @param fragment to Show
     */

    private void addFragment(@NonNull Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            Fragment existFragment = fm.findFragmentById(R.id.container);
            if (existFragment != null) {
                if (existFragment.getClass() != fragment.getClass()) {
                    replaceFragment(ft, fragment);
                }
            } else {
                replaceFragment(ft, fragment);
            }
            ft.commit();
        } else {
            throw new NullPointerException("Fragment parameter can not be null");
        }
    }

    private void replaceFragment(FragmentTransaction ft, Fragment fragment) {
        if (!(fragment instanceof HomeFragment)) {
            ft.addToBackStack(null);
        } else {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.replace(R.id.container, fragment);
    }

    @Override
    public void showHomeFragment() {
        addFragment(HomeFragment.newInstance(this));
        changeTitle(R.string.map);

    }

    public void setLocationSettingListener(LocationSettingListener mListener) {
        mLocationListener = mListener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST:
                if (mLocationListener != null) {
                    mLocationListener.onRequestResult(requestCode, resultCode, data);
                }
                break;
            case MapFragment.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                MapFragment.getChosenPlaceOnMap(data, this);
                break;
        }
    }
}
